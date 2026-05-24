import request from '../utils/request'

export function doLike(data) {
  return request({
    url: '/like/do',
    method: 'post',
    data
  })
}

export function undoLike(data) {
  return request({
    url: '/like/undo',
    method: 'post',
    data
  })
}

export function getLikeCount(postId) {
  return request({
    url: `/like/count/${postId}`,
    method: 'get'
  })
}

export function getLikeStatus(postId, userId) {
  return request({
    url: '/like/status',
    method: 'get',
    params: { postId, userId }
  })
}
