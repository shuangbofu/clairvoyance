import Vue from 'vue'
import VueRouter from 'vue-router'
import BaseView from '@/layout/BaseView'
// import DefaultView from '@/layout/DefaultView'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: BaseView,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: '仪表盘',
        icon: 'dashboard',
        component: () => import('@/pages/dashboard/index.vue'),
      },
      {
        path: '/worksheet',
        name: '工作表',
        icon: 'table',
        component: () => import('@/pages/worksheet/index.vue')
      }, {
        path: '/datasource',
        name: '数据源',
        icon: 'database',
        component: () => import('@/pages/datasource/index.vue')
      }
    ]
  },
  {
    path: '/chart/editor',
    name: '编辑图表',
    component: () => import('@/pages/chart/editor.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
