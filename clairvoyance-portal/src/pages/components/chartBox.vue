<template>
  <div class="chart-box-container">
    <slot name="header" />
    <div v-if="chartLayoutConfig.chartType === 'UNKNOWN'" class="unknown-chart">未选择图表类型</div>
    <div v-else-if="chartLayoutConfig.chartType === 'C2'" class="value-card">
      <div style="margin: auto;">
        <div class="label">{{valueCard.label}}</div>
        <div class="value">{{valueCard.value}}</div>
      </div>
    </div>
    <template v-else>
      <div id="chart-box"></div>
      <v-chart
        @click="onClick"
        ref="chart"
        theme="test"
        style="padding: 0;"
        :options="chartOption"
      />
      <!-- {{chartLayer.x.map(i=>i.realAliasName)}} -->
      <!-- {{chartLayer.y.map(i=>i.realAliasName)}} -->
      <!-- <vue-antv-chart :data="data" :chartLayer="chartLayer" :chartLayoutConfig="chartLayoutConfig" /> -->
    </template>
    <slot name="footer" />
  </div>
</template>

<script>
import "echarts/theme/forest";
import "echarts/theme/eduardo";
import "echarts/theme/macarons";
import "echarts/theme/macarons2";
import "echarts/theme/roma";
import "echarts/theme/shine";
import "echarts/theme/infographic";
import "echarts/theme/dark";
import { getChartOption } from "./chartPaint.js";
// import VueAntvChart from './vue-antv-chart';
export default {
  props: ["data", "chartLayer",'chartLayoutConfig'],
  data() {
    return {};
  },
  watch: {
    data() {
      this.resize()
    }
  },
  components: {
    // VueAntvChart
  },
  computed: {
    chartOption() {
      try {
        return getChartOption(this.chartLayoutConfig,this.chartLayer, this.data);
      } catch(msg) {
        this.$message.error(msg)
        return {}
      }
    },
    valueCard() {
      let res = {};
      const dataValue = this.data[0];
      for (let key in dataValue) {
        res = {
          label: key,
          value: dataValue[key]
        };
      }
      return res;
    }
  },
  mounted() {
    window.addEventListener('resize', () => {
        this.resize()
    }, false)
  },
  methods: {
    resize() {
      const ref =  this.$refs.chart
        if(ref) {
          console.log('resize')
          ref.resize()
        }
    },
    onClick(e) {
      console.log(e)
      this.$emit('click',e)
    }
  }
};
</script>

<style lang="less">
.echarts {
  position: relative;
  width: 100%;
  height: 100%;
}
.chart-box-container {
  position: relative;
  .unknown-chart {
    padding-top: 100px;
    text-align: center;
    font-size: 50px;
    color: #d6d6d6;
  }
  .value-card {
    height: 100%;
    text-align: center;
    padding-top: 10%;
    .label {
      font-size: 120%;
      margin-right: 5%;
    }
    .value {
      font-size: 360%;
    }
  }
}
</style>