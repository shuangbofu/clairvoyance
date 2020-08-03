import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './style/theme.less'
import Antd from 'ant-design-vue'
import store from './store'
import axios from "@/utils/request.js";

import ECharts from 'vue-echarts' // 在 webpack 环境下指向 components/ECharts.vue

// 手动引入 ECharts 各模块来减小打包体积
import 'echarts/lib/chart/bar'
import 'echarts/lib/chart/line'
import 'echarts/lib/chart/pie'
import 'echarts/lib/component/tooltip'

Vue.prototype.$axios = axios
Vue.use(Antd)
Vue.config.productionTip = false

// 注册组件后即可使用
Vue.component('v-chart', ECharts)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
