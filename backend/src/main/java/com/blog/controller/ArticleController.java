package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.ArticleDTO;
import com.blog.dto.ArticlePageDTO;
import com.blog.dto.Result;
import com.blog.entity.User;
import com.blog.service.ArticleService;
import com.blog.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final AuthService authService;

    public ArticleController(ArticleService articleService, AuthService authService) {
        this.articleService = articleService;
        this.authService = authService;
    }

    @GetMapping
    public Result<Page<ArticleDTO>> list(ArticlePageDTO pageDTO) {
        return Result.ok(articleService.getArticlePage(pageDTO));
    }

    @GetMapping("/{id}")
    public Result<ArticleDTO> detail(@PathVariable Long id) {
        try {
            return Result.ok(articleService.getArticleById(id));
        } catch (Exception e) {
            return Result.error(404, e.getMessage());
        }
    }

    @PostMapping
    public Result<ArticleDTO> create(@RequestBody ArticleDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = authService.getUserByUsername(auth.getName());
        if (user == null) {
            return Result.error(401, "Not authenticated");
        }
        return Result.ok(articleService.createArticle(dto, user.getId()));
    }

    @PutMapping("/{id}")
    public Result<ArticleDTO> update(@PathVariable Long id, @RequestBody ArticleDTO dto) {
        try {
            return Result.ok(articleService.updateArticle(id, dto));
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            return Result.ok();
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }
}
