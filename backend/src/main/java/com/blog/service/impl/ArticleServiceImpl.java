package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.ArticleDTO;
import com.blog.dto.ArticlePageDTO;
import com.blog.entity.Article;
import com.blog.entity.Category;
import com.blog.entity.Tag;
import com.blog.entity.User;
import com.blog.mapper.ArticleMapper;
import com.blog.mapper.CategoryMapper;
import com.blog.mapper.TagMapper;
import com.blog.mapper.UserMapper;
import com.blog.service.ArticleService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final JdbcTemplate jdbcTemplate;

    public ArticleServiceImpl(ArticleMapper articleMapper,
                              CategoryMapper categoryMapper,
                              UserMapper userMapper,
                              JdbcTemplate jdbcTemplate) {
        this.articleMapper = articleMapper;
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<ArticlePageDTO> getArticles(int page, int size) {
        Page<Article> articlePage = articleMapper.selectPage(
                new Page<>(page, size),
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getDeleted, 0)
                        .eq(Article::getIsPublished, 1)
                        .orderByDesc(Article::getIsTop)
                        .orderByDesc(Article::getCreateTime)
        );

        Page<ArticlePageDTO> result = new Page<>(page, size, articlePage.getTotal());
        List<ArticlePageDTO> records = articlePage.getRecords().stream()
                .map(this::convertToPageDTO)
                .collect(Collectors.toList());
        result.setRecords(records);
        return result;
    }

    @Override
    public ArticlePageDTO getArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null || article.getDeleted() == 1) {
            throw new RuntimeException("Article not found");
        }
        article.setViewCount(article.getViewCount() + 1);
        articleMapper.updateById(article);
        return convertToPageDTO(article);
    }

    @Override
    @Transactional
    public void createArticle(ArticleDTO dto, Long authorId) {
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
        article.setIsPublished(dto.getStatus() != null ? dto.getStatus() : 0);
        article.setStatus(1);
        article.setDeleted(0);
        articleMapper.insert(article);

        saveArticleTags(article.getId(), dto.getTagIds());
    }

    @Override
    @Transactional
    public void updateArticle(Long id, ArticleDTO dto) {
        Article article = articleMapper.selectById(id);
        if (article == null || article.getDeleted() == 1) {
            throw new RuntimeException("Article not found");
        }
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setSummary(dto.getSummary());
        article.setCoverImage(dto.getCoverImage());
        article.setCategoryId(dto.getCategoryId());
        if (dto.getIsTop() != null) article.setIsTop(dto.getIsTop());
        if (dto.getStatus() != null) article.setIsPublished(dto.getStatus());
        articleMapper.updateById(article);

        jdbcTemplate.update("DELETE FROM article_tag WHERE article_id = ?", id);
        saveArticleTags(id, dto.getTagIds());
    }

    @Override
    public void deleteArticle(Long id) {
        Article article = articleMapper.selectById(id);
        if (article == null) {
            throw new RuntimeException("Article not found");
        }
        article.setDeleted(1);
        articleMapper.updateById(article);
    }

    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return;
        for (Long tagId : tagIds) {
            jdbcTemplate.update("INSERT INTO article_tag (article_id, tag_id) VALUES (?, ?)", articleId, tagId);
        }
    }

    private ArticlePageDTO convertToPageDTO(Article article) {
        ArticlePageDTO dto = new ArticlePageDTO();
        dto.setId(article.getId());
        dto.setTitle(article.getTitle());
        dto.setSummary(article.getSummary());
        dto.setCoverImage(article.getCoverImage());
        dto.setCategoryId(article.getCategoryId());
        dto.setViewCount(article.getViewCount());
        dto.setStatus(article.getIsPublished());
        dto.setIsTop(article.getIsTop());
        dto.setCreateTime(article.getCreateTime());

        if (article.getCategoryId() != null) {
            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) dto.setCategoryName(category.getName());
        }
        if (article.getAuthorId() != null) {
            User author = userMapper.selectById(article.getAuthorId());
            if (author != null) dto.setAuthorName(author.getNickname());
        }

        List<String> tagNames = jdbcTemplate.queryForList(
                "SELECT t.name FROM tag t INNER JOIN article_tag at ON t.id = at.tag_id WHERE at.article_id = ?",
                String.class, article.getId());
        dto.setTags(tagNames);

        return dto;
    }
}
