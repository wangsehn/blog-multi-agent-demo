package com.blog.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticlePageDTO {
    private Long id;
    private String title;
    private String summary;
    private String coverImage;
    private Long categoryId;
    private String categoryName;
    private String authorName;
    private Integer viewCount;
    private Integer status;
    private Integer isTop;
    private LocalDateTime createTime;
    private List<String> tags;
}
