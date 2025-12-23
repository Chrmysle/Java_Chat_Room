package com.bx.implatform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bx.implatform.entity.BlogPost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogPostMapper extends BaseMapper<BlogPost> {
}