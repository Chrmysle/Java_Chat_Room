package com.bx.implatform.vo;

import lombok.Data;

import java.util.Date;

@Data
public class BlogPostVO {
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private Date createdTime;
    // 新增：作者用户名与昵称
    private String authorUserName;
    private String authorNickName;
}