package com.blog.dto;

import lombok.Data;

@Data
public class ArticlePageDTO {
    private Integer page = 1;
    private Integer size = 10;
    private String keyword;
    private Long categoryId;
    private Long tagId;
}
