import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './style/theme.less'
import Antd from 'ant-design-vue'
import store from './store'
import axios from "@/utils/request.js";
import ECharts from '@/plugins/echarts.js';

Vue.prototype.$axios = axios
Vue.use(Antd)
Vue.config.productionTip = false
Vue.component('v-chart', ECharts)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
