<template>
  <div ref="container" class="container"></div>
</template>

<script>
// const data = [
//   { type: '未知', value: 654, percent: 0.02 },
//   { type: '17 岁以下', value: 654, percent: 0.02 },
//   { type: '18-24 岁', value: 4400, percent: 0.2 },
//   { type: '25-29 岁', value: 5300, percent: 0.24 },
//   { type: '30-39 岁', value: 6200, percent: 0.28 },
//   { type: '40-49 岁', value: 3300, percent: 0.14 },
//   { type: '50 岁以上', value: 1500, percent: 0.06 },
// ];

// import debounce from 'lodash/debounce'
// import { addListener, removeListener } from 'resize-detector'
import {Chart, Option} from './g2.ts'
export default {
  props: {
    options: Object,
    theme: String,
    data: Array,
    chartLayer: Object,
    chartLayoutConfig: Object
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      if(this.chart) {
        return
      }
      const option = new Option('default', this.chartLayoutConfig.chartType).setXY(this.chartLayer.x.map(i=>i.realAliasName), this.chartLayer.y.map(i=>i.realAliasName))
      const chart = new Chart(this.$el, option)
      // this.__resizeHanlder = debounce(() => {
      //   this.chart.resize()
      // }, 100, { leading: true })
      // addListener(this.$el, this.__resizeHanlder)
      this.chart =  chart
      chart.render(this.data)
    },
  },
  destroy () {
    this.chart = null
    // removeListener(this.$el, this.__resizeHanlder)
  },
}
</script>

<style lang="less">
.container {
  height: 100%;
  width: 100%;
}
</style>