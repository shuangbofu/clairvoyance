import Vue from 'vue'
import Vuex from 'vuex'
import setting from './modules/setting'
import basic from './modules/basic'
import chart from './modules/chart'

Vue.use(Vuex)

export default new Vuex.Store({
  modules: {
    setting,
    basic,
    chart
  }
})
