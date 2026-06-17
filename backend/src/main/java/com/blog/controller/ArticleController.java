package com.blog.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.dto.ArticleDTO;
import com.blog.dto.ArticlePageDTO;
import com.blog.dto.Result;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.service.ArticleService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final UserMapper userMapper;

    public ArticleController(ArticleService articleService, UserMapper userMapper) {
        this.articleService = articleService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public Result<Page<ArticlePageDTO>> getArticles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Result.success(articleService.getArticles(page, size));
    }

    @GetMapping("/{id}")
    public Result<ArticlePageDTO> getArticleById(@PathVariable Long id) {
        try {
            return Result.success(articleService.getArticleById(id));
        } catch (RuntimeException e) {
            return Result.error(404, e.getMessage());
        }
    }

    @PostMapping
    public Result<Void> createArticle(@RequestBody ArticleDTO dto) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            User user = userMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                            .eq(User::getUsername, userDetails.getUsername()));
            articleService.createArticle(dto, user.getId());
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Void> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO dto) {
        try {
            articleService.updateArticle(id, dto);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteArticle(@PathVariable Long id) {
        try {
            articleService.deleteArticle(id);
            return Result.success();
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
