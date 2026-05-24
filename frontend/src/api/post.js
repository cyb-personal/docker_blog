import request from '../utils/request'

export function getPostList(page = 1, size = 10) {
  return request({
    url: '/post/list',
    method: 'get',
    params: { page, size }
  })
}

export function getPostDetail(id) {
  return request({
    url: `/post/${id}`,
    method: 'get'
  })
}

export function getUserPosts(userId) {
  return request({
    url: `/post/user/${userId}`,
    method: 'get'
  })
}

export function createPost(data) {
  return request({
    url: '/post/create',
    method: 'post',
    data
  })
}

export function updatePost(data) {
  return request({
    url: '/post/update',
    method: 'put',
    data
  })
}

export function deletePost(id) {
  return request({
    url: `/post/${id}`,
    method: 'delete'
  })
}
