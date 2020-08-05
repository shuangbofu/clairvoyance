<template>
  <div class="chart-box-container">
    <div v-if="chart.chartType === 'UNKNOWN'" class="unknown-chart">未选择图表类型</div>
    <div v-else-if="chart.chartType === 'C2'" class="value-card">
      <div style="margin: auto;">
        <div class="label">{{valueCard.label}}</div>
        <div class="value">{{valueCard.value}}</div>
      </div>
    </div>
    <v-chart theme="macarons" v-else style="padding: 0;" :options="chartOption" />
  </div>
</template>

<script>
import "echarts/theme/macarons";
import { getChartOption } from "./chartPaint.js";
export default {
  props: ["data", "chart"],
  data() {
    return {};
  },
  computed: {
    chartOption() {
      return getChartOption(this.chart, this.data);
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