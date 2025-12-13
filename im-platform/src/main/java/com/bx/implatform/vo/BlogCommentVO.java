package com.bx.implatform.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BlogCommentVO {
    private Long id;
    private Long postId;
    private Long authorId;
    private Long parentId;
    private String content;
    private Date createdTime;
    private List<BlogCommentVO> children = new ArrayList<>();
    // 新增：作者用户名与昵称
    private String authorUserName;
    private String authorNickName;
}