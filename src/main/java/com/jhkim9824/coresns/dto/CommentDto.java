package com.jhkim9824.coresns.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long commentId;
    private Long postId;
    private Long userId;
    private String userNickname;

    @NotBlank(message = "Comment content cannot be blank")
    @Size(max = 300, message = "Comment content must not exceed 300 characters")
    private String content;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
