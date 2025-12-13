package com.bx.implatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bx.implatform.dto.BlogCommentDTO;
import com.bx.implatform.entity.BlogComment;
import com.bx.implatform.entity.User;
import com.bx.implatform.mapper.BlogCommentMapper;
import com.bx.implatform.service.BlogCommentService;
import com.bx.implatform.service.UserService;
import com.bx.implatform.session.SessionContext;
import com.bx.implatform.session.UserSession;
import com.bx.implatform.util.BeanUtils;
import com.bx.implatform.util.SensitiveFilterUtil;
import com.bx.implatform.vo.BlogCommentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogCommentServiceImpl extends ServiceImpl<BlogCommentMapper, BlogComment> implements BlogCommentService {

    private final SensitiveFilterUtil sensitiveFilterUtil;
    private final UserService userService;

    @Override
    public BlogCommentVO addComment(BlogCommentDTO dto) {
        UserSession session = SessionContext.getSession();
        BlogComment c = new BlogComment();
        c.setPostId(dto.getPostId());
        c.setParentId(dto.getParentId());
        c.setAuthorId(session.getUserId());
        c.setContent(sensitiveFilterUtil.filter(dto.getContent()));
        c.setCreatedTime(new Date());
        this.save(c);
        BlogCommentVO vo = BeanUtils.copyProperties(c, BlogCommentVO.class);
        // 填充作者信息
        vo.setAuthorUserName(session.getUserName());
        vo.setAuthorNickName(session.getNickName());
        log.info("发表评论，作者:{}, 帖子:{}, 父评论:{}", session.getUserId(), dto.getPostId(), dto.getParentId());
        return vo;
    }

    @Override
    public List<BlogCommentVO> listComments(Long postId) {
        QueryWrapper<BlogComment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId).orderByAsc("id");
        List<BlogComment> list = this.list(wrapper);
        // 构建树
        Map<Long, BlogCommentVO> map = new HashMap<>();
        List<BlogCommentVO> roots = new ArrayList<>();
        for (BlogComment c : list) {
            BlogCommentVO vo = BeanUtils.copyProperties(c, BlogCommentVO.class);
            // 填充作者信息
            User user = userService.getById(c.getAuthorId());
            if (user != null) {
                vo.setAuthorUserName(user.getUserName());
                vo.setAuthorNickName(user.getNickName());
            }
            map.put(vo.getId(), vo);
        }
        for (BlogComment c : list) {
            BlogCommentVO vo = map.get(c.getId());
            Long parentId = c.getParentId();
            if (parentId == null) {
                roots.add(vo);
            } else {
                BlogCommentVO parent = map.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(vo);
                } else {
                    roots.add(vo); // 容错：父评论不存在时视为根
                }
            }
        }
        return roots;
    }
}