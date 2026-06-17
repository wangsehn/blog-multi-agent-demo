package com.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.ArticleDTO;
import com.blog.dto.ArticlePageDTO;

public interface ArticleService {
    Page<ArticleDTO> getArticlePage(ArticlePageDTO pageDTO);
    ArticleDTO getArticleById(Long id);
    ArticleDTO createArticle(ArticleDTO dto, Long authorId);
    ArticleDTO updateArticle(Long id, ArticleDTO dto);
    void deleteArticle(Long id);
}
