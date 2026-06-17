import api from './index'

export function getArticles(params) {
  return api.get('/articles', { params })
}

export function getArticle(id) {
  return api.get(`/articles/${id}`)
}

export function createArticle(data) {
  return api.post('/articles', data)
}

export function updateArticle(id, data) {
  return api.put(`/articles/${id}`, data)
}

export function deleteArticle(id) {
  return api.delete(`/articles/${id}`)
}
