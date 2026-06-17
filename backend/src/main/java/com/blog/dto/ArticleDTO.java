package com.blog.dto;

import lombok.Data;
import java.util.List;

@Data
public class ArticleDTO {
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Long categoryId;
    private List<Long> tagIds;
    private Integer status;
    private Integer isTop;
}
