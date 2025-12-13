package com.bx.implatform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BlogCommentDTO {
    @NotNull(message = "帖子id不能为空")
    private Long postId;
    private Long parentId; // 可为空，表示直接评论帖子
    @NotBlank(message = "内容不能为空")
    private String content;
}