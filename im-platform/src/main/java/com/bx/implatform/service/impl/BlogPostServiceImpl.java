package com.bx.implatform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bx.implatform.dto.BlogPostDTO;
import com.bx.implatform.entity.BlogPost;
import com.bx.implatform.entity.User;
import com.bx.implatform.mapper.BlogPostMapper;
import com.bx.implatform.service.BlogPostService;
import com.bx.implatform.service.UserService;
import com.bx.implatform.session.SessionContext;
import com.bx.implatform.session.UserSession;
import com.bx.implatform.util.BeanUtils;
import com.bx.implatform.util.SensitiveFilterUtil;
import com.bx.implatform.vo.BlogPostVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl extends ServiceImpl<BlogPostMapper, BlogPost> implements BlogPostService {

    private final SensitiveFilterUtil sensitiveFilterUtil;
    private final UserService userService;

    @Override
    public BlogPostVO createPost(BlogPostDTO dto) {
        UserSession session = SessionContext.getSession();
        BlogPost post = new BlogPost();
        post.setAuthorId(session.getUserId());
        post.setTitle(dto.getTitle());
        post.setContent(sensitiveFilterUtil.filter(dto.getContent()));
        post.setCreatedTime(new Date());
        this.save(post);
        BlogPostVO vo = BeanUtils.copyProperties(post, BlogPostVO.class);
        // 填充作者信息
        vo.setAuthorUserName(session.getUserName());
        vo.setAuthorNickName(session.getNickName());
        log.info("创建博客帖子，作者:{}, 标题:{}", session.getUserId(), dto.getTitle());
        return vo;
    }

    @Override
    public List<BlogPostVO> listPosts(Long page, Long size) {
        page = page != null && page > 0 ? page : 1;
        size = size != null && size > 0 ? size : 10;
        long stIdx = (page - 1) * size;
        QueryWrapper<BlogPost> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id").last("limit " + stIdx + "," + size);
        List<BlogPost> list = this.list(wrapper);
        return list.stream().map(m -> {
            BlogPostVO vo = BeanUtils.copyProperties(m, BlogPostVO.class);
            User user = userService.getById(m.getAuthorId());
            if (user != null) {
                vo.setAuthorUserName(user.getUserName());
                vo.setAuthorNickName(user.getNickName());
            }
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public BlogPostVO getPost(Long id) {
        BlogPost post = this.getById(id);
        BlogPostVO vo = BeanUtils.copyProperties(post, BlogPostVO.class);
        User user = userService.getById(post.getAuthorId());
        if (user != null) {
            vo.setAuthorUserName(user.getUserName());
            vo.setAuthorNickName(user.getNickName());
        }
        return vo;
    }
}