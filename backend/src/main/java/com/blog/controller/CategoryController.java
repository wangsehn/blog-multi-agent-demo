package com.blog.controller;

import com.blog.dto.Result;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public Result<List<Category>> list() {
        return Result.ok(categoryService.listAll());
    }
}
