import request from '../utils/request'

export function getComments(postId) {
  return request({
    url: `/comment/post/${postId}`,
    method: 'get'
  })
}

export function addComment(data) {
  return request({
    url: '/comment/add',
    method: 'post',
    data
  })
}

export function deleteComment(id) {
  return request({
    url: `/comment/${id}`,
    method: 'delete'
  })
}

export function getCommentCount(postId) {
  return request({
    url: `/comment/count/${postId}`,
    method: 'get'
  })
}
