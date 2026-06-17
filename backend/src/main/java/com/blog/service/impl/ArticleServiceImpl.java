package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.ArticleDTO;
import com.blog.dto.ArticlePageDTO;
import com.blog.entity.Article;
import com.blog.entity.Category;
import com.blog.entity.User;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.CategoryMapper;
import com.blog.mapper.UserMapper;
import com.blog.service.ArticleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;

    public ArticleServiceImpl(ArticleMapper articleMapper, CategoryMapper categoryMapper, UserMapper userMapper) {
        this.articleMapper = articleMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Page<ArticleDTO> getArticlePage(ArticlePageDTO pageDTO) {
        Page<Article> page = new Page<>(pageDTO.getPage(), pageDTO.getSize());
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getIsPublished, 1);
        if (StringUtils.hasText(pageDTO.getKeyword())) {
            wrapper.and(w -> w.like(Article::getTitle, pageDTO.getKeyword())
                    .or().like(Article::getSummary, pageDTO.getKeyword()));
        }
        if (pageDTO.getCategoryId() != null) {
            wrapper.eq(Article::getCategoryId, pageDTO.getCategoryId());
        }
        wrapper.orderByDesc(Article::getIsTop, Article::getCreateTime);
        Page<Article> result = articleMapper.selectPage(page, wrapper);

        Page<ArticleDTO> dtoPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        dtoPage.setRecords(result.getRecords().stream().map(this::toDTO).collect(Collectors.toList()));
        return dtoPage;
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }
        article.setViewCount(article.getViewCount() + 1);
        articleMapper.updateById(article);
        return toDTO(article);
    }

    @Override
    public ArticleDTO createArticle(ArticleDTO dto, Long authorId) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setSummary(dto.getSummary());
        article.setCoverImage(dto.getCoverImage());
        article.setCategoryId(dto.getCategoryId());
        article.setAuthorId(authorId);
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setCommentCount(0);
        article.setIsTop(dto.getIsTop() != null ? dto.getIsTop() : 0);
        article.setIsPublished(dto.getIsPublished() != null ? dto.getIsPublished() : 0);
        article.setStatus(1);
        article.setDeleted(0);
        articleMapper.insert(article);
        return toDTO(article);
    }

    @Override
    public ArticleDTO updateArticle(Long id, ArticleDTO dto) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setSummary(dto.getSummary());
        article.setCoverImage(dto.getCoverImage());
        article.setCategoryId(dto.getCategoryId());
        if (dto.getIsTop() != null) article.setIsTop(dto.getIsTop());
        if (dto.getIsPublished() != null) article.setIsPublished(dto.getIsPublished());
        articleMapper.updateById(article);
        return toDTO(article);
    }

    @Override
    public void deleteArticle(Long id) {
        articleMapper.deleteById(id);
    }

    private ArticleDTO toDTO(Article article) {
        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setContent(article.getContent());
        dto.setSummary(article.getSummary());
        dto.setCoverImage(article.getCoverImage());
        dto.setCategoryId(article.getCategoryId());
        dto.setAuthorId(article.getAuthorId());
        dto.setViewCount(article.getViewCount());
        dto.setLikeCount(article.getLikeCount());
        dto.setCommentCount(article.getCommentCount());
        dto.setIsTop(article.getIsTop());
        dto.setIsPublished(article.getIsPublished());
        dto.setCreateTime(article.getCreateTime());
        dto.setUpdateTime(article.getUpdateTime());

        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) dto.setCategoryName(category.getName());
        }
        User author = userMapper.selectById(article.getAuthorId());
        if (author != null) dto.setAuthorName(author.getNickname());

        dto.setTagIds(Collections.emptyList());
        dto.setTagNames(Collections.emptyList());
        return dto;
    }
}
