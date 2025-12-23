package com.bx.implatform.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("im_blog_comment")
public class BlogComment {
    @TableId
    private Long id;
    private Long postId;
    private Long authorId;
    private Long parentId; // 为空表示直接评论帖子，否则为回复某条评论
    private String content;
    private Date createdTime;
}