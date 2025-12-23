<template>
  <div class="blog-page">
    <div class="blog-container">
      <div class="page-bar">
        <div class="page-title">帖子广场</div>
        <el-button type="primary" @click="showCreate = true">发布帖子</el-button>
      </div>

      <el-dialog title="发布帖子" :visible.sync="showCreate" width="600px">
        <el-input v-model="form.title" placeholder="请输入帖子标题" maxlength="50" class="blog-title-input"></el-input>
        <el-input type="textarea" v-model="form.content" :rows="6" placeholder="请输入帖子内容" class="blog-content-input"></el-input>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showCreate = false">取消</el-button>
          <el-button type="primary" @click="onCreatePost">确定发布</el-button>
        </span>
      </el-dialog>

      <el-divider content-position="center">帖子列表</el-divider>

      <div v-if="posts.length">
        <div class="blog-list">
          <el-card v-for="(post, idx) in posts" :key="post.id || post.postId || idx" class="blog-item-card" shadow="hover" @click="openPost(post)">
            <div class="blog-item-title">{{ post.title }}</div>
            <div class="blog-item-meta">
              <span>作者：{{ post.authorUserName || post.authorNickName || post.username || post.userName || post.showNickName || post.nickName }}</span>
              <span>时间：{{ formatTime(post.createdTime || post.createTime || post.createDate || post.createdAt || post.createTimeStr) }}</span>
              <span v-if="post.likeCount !== undefined">点赞：{{ post.likeCount }}</span>
              <span v-if="post.commentCount !== undefined">评论：{{ post.commentCount }}</span>
            </div>
            <div class="blog-item-content">{{ post.content }}</div>
          </el-card>
        </div>
        <div class="blog-pagination">
          <el-button size="mini" :disabled="page <= 1" @click="changePage(page - 1)">上一页</el-button>
          <span class="page-text">第 {{ page }} 页</span>
          <el-button size="mini" :disabled="posts.length < size" @click="changePage(page + 1)">下一页</el-button>
        </div>
      </div>
      <el-empty description="暂无帖子" v-else></el-empty>

      <el-drawer title="帖子详情" :visible.sync="showDetail" :with-header="true" size="50%" :append-to-body="true">
        <div v-if="current" class="blog-detail">
          <div class="blog-detail-title">{{ current.title }}</div>
          <div class="blog-detail-meta">
            <span>作者：{{ (current && (current.authorUserName || current.authorNickName || current.username || current.userName || current.showNickName || current.nickName)) || '' }}</span>
            <span>时间：{{ (current && formatTime(current.createdTime || current.createTime || current.createDate || current.createdAt || current.createTimeStr)) || '' }}</span>
          </div>
          <div class="blog-detail-content">{{ current.content }}</div>
          <div class="blog-comment-editor">
            <el-input type="textarea" v-model="commentContent" :rows="3" maxlength="200" placeholder="写下你的评论..."></el-input>
            <div class="comment-actions">
              <el-button type="primary" size="mini" @click="submitComment">发表评论</el-button>
              <el-button size="mini" v-if="replyParentId" @click="replyParentId=null">取消回复</el-button>
            </div>
          </div>
          <div class="blog-comment-list" v-if="comments.length">
            <div v-for="(co, i) in comments" :key="co.id" class="comment-item">
              <div class="comment-main">
                <div class="comment-author">{{ co.authorUserName || co.authorNickName || co.username || co.userName || co.showNickName || co.nickName }}</div>
                <div class="comment-meta">{{ formatTime(co.createdTime || co.createTime || co.createDate || co.createdAt || co.createTimeStr) }}</div>
              </div>
              <div class="comment-content">{{ co.content }}</div>
              <div class="comment-actions">
                <el-button type="text" size="mini" @click="replyTo(co)">回复</el-button>
                <span v-if="replyParentId===co.id" class="reply-tip">正在回复该评论，直接在上方输入框输入内容</span>
              </div>
            </div>
          </div>
          <el-empty description="暂无评论" v-else></el-empty>
        </div>
      </el-drawer>
    </div>
  </div>
</template>

<script>
import BlogApi from '../api/blogApi'

export default {
  name: 'Blog',
  data() {
    return {
      showCreate: false,
      form: { title: '', content: '' },
      posts: [],
      page: 1,
      size: 10,
      showDetail: false,
      current: null,
      comments: [],
      commentContent: '',
      replyParentId: null
    }
  },
  created() {
    this.loadPosts()
  },
  methods: {
    async loadPosts() {
      try {
        this.posts = await BlogApi.listPosts(this.page, this.size)
      } catch (e) {
        console.error('加载帖子失败', e)
        this.$message.error('加载帖子失败')
      }
    },
    changePage(p) {
      if (p < 1) return
      this.page = p
      this.loadPosts()
    },
    formatTime(val) {
      if (!val) return ''
      const d = (val instanceof Date) ? val : new Date(val)
      if (isNaN(d.getTime())) return String(val)
      const pad = n => (n < 10 ? '0' + n : '' + n)
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}`
    },
    async onCreatePost() {
      if (!this.form.title || !this.form.content) {
        this.$message.error('标题和内容不能为空')
        return
      }
      try {
        const vo = await BlogApi.createPost(this.form)
        this.$message.success('发布成功')
        this.showCreate = false
        this.form = { title: '', content: '' }
        // 刷新列表到第一页
        this.page = 1
        this.loadPosts()
        // 打开详情
        this.openPost(vo)
      } catch (e) {
        console.error('发布帖子失败', e)
        this.$message.error('发布失败，请稍后重试')
      }
    },
    async openPost(p) {
       const postId = p.id || p.postId || p.postID
       // 先打开抽屉，提升交互体验
       this.current = p
       this.showDetail = true
       if (!postId) return
       try {
         const detail = await BlogApi.getPost(postId)
         if (detail) this.current = detail
         await this.loadComments(postId)
       } catch (e) {
         console.error('加载帖子详情失败', e)
         this.$message.error('加载帖子详情失败')
       }
     },
    async loadComments(postId) {
      try {
        const pid = postId || (this.current ? (this.current.id || this.current.postId || this.current.postID) : null)
        if (!pid) return
        this.comments = await BlogApi.listComments(pid)
      } catch (e) {
        console.error('加载评论失败', e)
        this.$message.error('加载评论失败')
      }
    },
    replyTo(c) {
      this.replyParentId = c.id
      this.$message.info('正在回复该评论，直接在输入框输入内容然后点击发表评论即可')
    },
    async submitComment() {
      if (!this.current) return
      if (!this.commentContent) {
        this.$message.error('评论内容不能为空')
        return
      }
      const cid = this.current.id || this.current.postId || this.current.postID
      const dto = { postId: cid, parentId: this.replyParentId, content: this.commentContent }
      try {
        await BlogApi.addComment(dto)
        this.$message.success('评论成功')
        this.commentContent = ''
        this.replyParentId = null
        await this.loadComments(cid)
      } catch (e) {
        console.error('发表评论失败', e)
        this.$message.error('评论失败，请稍后重试')
      }
    }
  }
}
</script>

<style scoped>
.blog-page { padding: 16px; }
.blog-container { width: 980px; max-width: calc(100vw - 220px); margin: 0 auto; }

.page-bar { display: flex; align-items: center; justify-content: space-between; margin-bottom: 10px; }
.page-title { font-size: 18px; font-weight: 700; color: #303133; }

.blog-title-input, .blog-content-input { margin-bottom: 12px; }
.blog-actions { text-align: right; }

.blog-list { display: grid; grid-template-columns: 1fr; gap: 12px; }
.blog-item-card { cursor: pointer; }
.blog-item-title { font-size: 18px; font-weight: 700; color: #303133; margin-bottom: 6px; }
.blog-item-meta { color: #909399; font-size: 12px; display: flex; flex-wrap: wrap; gap: 12px; margin-bottom: 8px; }
.blog-item-content { color: #606266; line-height: 1.6; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; }

.blog-pagination { display: flex; align-items: center; justify-content: center; gap: 8px; margin-top: 14px; }
.page-text { color: #606266; font-size: 12px; }

.blog-detail { padding: 10px 16px; }
.blog-detail-title { font-size: 20px; font-weight: 700; margin-bottom: 6px; }
.blog-detail-meta { color: #909399; font-size: 12px; display: flex; gap: 14px; margin-bottom: 10px; }
.blog-detail-content { color: #606266; line-height: 1.8; white-space: pre-wrap; margin-bottom: 12px; }

.comment-item { padding: 8px 0; border-bottom: 1px solid #ebeef5; }
.comment-main { display: flex; align-items: center; justify-content: space-between; }
.comment-author { font-weight: 600; color: #303133; }
.comment-meta { color: #909399; font-size: 12px; }
.comment-content { margin: 6px 0 8px; color: #606266; }
.reply-tip { color: #909399; font-size: 12px; margin-left: 8px; }
</style>