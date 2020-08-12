<template>
  <div class="chart-box-container">
    <slot name="header" />
    <div v-if="chartLayer.chartType === 'UNKNOWN'" class="unknown-chart">未选择图表类型</div>
    <div v-else-if="chartLayer.chartType === 'C2'" class="value-card">
      <div style="margin: auto;">
        <div class="label">{{valueCard.label}}</div>
        <div class="value">{{valueCard.value}}</div>
      </div>
    </div>
    <template v-else>
      <v-chart
        @click="onClick"
        ref="chart"
        theme="macarons"
        style="padding: 0;"
        :options="chartOption"
      />
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
export default {
  props: ["data", "chartLayer"],
  data() {
    return {};
  },
  watch: {
    data() {
      this.resize()
    }
  },
  computed: {
    chartOption() {
      try {
        return getChartOption(this.chartLayer, this.data);
      } catch(msg) {
        this.$message.eror(msg)
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
      console.log('resize')
      const ref =  this.$refs.chart
        if(ref) {
          ref.resize()
        }
    },
    onClick(e) {
      console.log(this.chartLayer.x.length, this.chartLayer.y.length)
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
    padding-top: 12%;
    .label {
      font-size: 24px;
      margin-right: 5%;
    }
    .value {
      font-size: 112px;
    }
  }
}
</style>