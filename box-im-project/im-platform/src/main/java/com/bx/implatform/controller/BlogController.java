package com.bx.implatform.controller;

import com.bx.implatform.dto.BlogPostDTO;
import com.bx.implatform.dto.BlogCommentDTO;
import com.bx.implatform.result.Result;
import com.bx.implatform.result.ResultUtils;
import com.bx.implatform.service.BlogPostService;
import com.bx.implatform.service.BlogCommentService;
import com.bx.implatform.vo.BlogPostVO;
import com.bx.implatform.vo.BlogCommentVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "博客功能")
@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogPostService blogPostService;
    private final BlogCommentService blogCommentService;

    @PostMapping("/post")
    @Operation(summary = "发布帖子", description = "创建一篇帖子")
    public Result<BlogPostVO> createPost(@Valid @RequestBody BlogPostDTO dto) {
        return ResultUtils.success(blogPostService.createPost(dto));
    }

    @GetMapping("/post/list")
    @Operation(summary = "帖子列表", description = "分页查询帖子列表")
    public Result<List<BlogPostVO>> listPosts(@RequestParam(required = false) Long page,
                                              @RequestParam(required = false) Long size) {
        return ResultUtils.success(blogPostService.listPosts(page, size));
    }

    @GetMapping("/post/{id}")
    @Operation(summary = "帖子详情", description = "根据id查询帖子")
    public Result<BlogPostVO> getPost(@NotNull @PathVariable("id") Long id) {
        return ResultUtils.success(blogPostService.getPost(id));
    }

    @PostMapping("/comment")
    @Operation(summary = "发表评论或回复", description = "对帖子发表评论或回复某条评论")
    public Result<BlogCommentVO> addComment(@Valid @RequestBody BlogCommentDTO dto) {
        return ResultUtils.success(blogCommentService.addComment(dto));
    }

    @GetMapping("/comment/list")
    @Operation(summary = "评论列表", description = "查询帖子的评论树")
    public Result<List<BlogCommentVO>> listComments(@NotNull @RequestParam Long postId) {
        return ResultUtils.success(blogCommentService.listComments(postId));
    }
}