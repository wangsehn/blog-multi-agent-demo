package com.blog.controller;

import com.blog.dto.Result;
import com.blog.entity.Tag;
import com.blog.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public Result<List<Tag>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }
}
