package com.bx.implatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bx.implatform.dto.BlogCommentDTO;
import com.bx.implatform.entity.BlogComment;
import com.bx.implatform.vo.BlogCommentVO;

import java.util.List;

public interface BlogCommentService extends IService<BlogComment> {
    BlogCommentVO addComment(BlogCommentDTO dto);
    List<BlogCommentVO> listComments(Long postId);
}