package com.bx.implatform.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("im_blog_post")
public class BlogPost {
    @TableId
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private Date createdTime;
}