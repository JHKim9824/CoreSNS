package com.jhkim9824.coresns.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private Long userId;
    private String content;
    private String location;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer likeCount;
    private Integer commentCount;
    private String mainImageUrl;
}
