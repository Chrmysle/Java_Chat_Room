package com.bx.implatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bx.implatform.dto.BlogPostDTO;
import com.bx.implatform.entity.BlogPost;
import com.bx.implatform.vo.BlogPostVO;

import java.util.List;

public interface BlogPostService extends IService<BlogPost> {
    BlogPostVO createPost(BlogPostDTO dto);
    List<BlogPostVO> listPosts(Long page, Long size);
    BlogPostVO getPost(Long id);
}