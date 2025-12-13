import http from './httpRequest.js'

const BlogApi = {
  createPost(dto) {
    return http({
      url: '/blog/post',
      method: 'post',
      data: JSON.stringify(dto),
      headers: {
        'Content-Type': 'application/json; charset=utf-8'
      }
    })
  },
  listPosts(page = 1, size = 10) {
    return http({
      url: `/blog/post/list?page=${page}&size=${size}`,
      method: 'get'
    })
  },
  getPost(id) {
    return http({
      url: `/blog/post/${id}`,
      method: 'get'
    })
  },
  addComment(dto) {
    return http({
      url: '/blog/comment',
      method: 'post',
      data: JSON.stringify(dto),
      headers: {
        'Content-Type': 'application/json; charset=utf-8'
      }
    })
  },
  listComments(postId) {
    return http({
      url: `/blog/comment/list?postId=${postId}`,
      method: 'get'
    })
  }
}

export default BlogApi