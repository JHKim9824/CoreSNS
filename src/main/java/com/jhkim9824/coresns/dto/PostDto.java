package com.jhkim9824.coresns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Content cannot be blank")
    @Size(min = 1, max = 300, message = "Content must be between 1 and 300 characters")
    private String content;

    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer likeCount;
    private Integer commentCount;
    private String mainImageUrl;
}
