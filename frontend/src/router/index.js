import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('../views/Login.vue')
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('../views/Register.vue')
    },
    {
      path: '/',
      component: () => import('../views/Home.vue'),
      redirect: '/posts',
      children: [
        {
          path: 'posts',
          name: 'PostList',
          component: () => import('../views/PostList.vue')
        },
        {
          path: 'post/:id',
          name: 'PostDetail',
          component: () => import('../views/PostDetail.vue')
        },
        {
          path: 'create',
          name: 'CreatePost',
          component: () => import('../views/CreatePost.vue')
        },
        {
          path: 'user/:id',
          name: 'UserCenter',
          component: () => import('../views/UserCenter.vue')
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem('token')
  if (to.path !== '/login' && to.path !== '/register' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
