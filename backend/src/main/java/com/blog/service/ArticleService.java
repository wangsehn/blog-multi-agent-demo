package com.blog.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.ArticleDTO;
import com.blog.dto.ArticlePageDTO;

public interface ArticleService {
    Page<ArticlePageDTO> getArticles(int page, int size);
    ArticlePageDTO getArticleById(Long id);
    void createArticle(ArticleDTO dto, Long authorId);
    void updateArticle(Long id, ArticleDTO dto);
    void deleteArticle(Long id);
}
