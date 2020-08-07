<template>
  <div class="chart-container">
    <div class="chart-header">
      <div class="title">{{conf.name}}</div>
      <div class="button-list">
        <a-icon
          class="button"
          type="edit"
          @click="$router.push({name: '编辑图表', 
          query:{chartId: conf.chartId, workSheetId: conf.workSheetId}
           })"
        ></a-icon>
        <a-icon class="button" type="redo" @click="fetchData"></a-icon>
        <a-icon class="button" type="more"></a-icon>
      </div>
    </div>
    <chart-box
      style="position: relative; width: calc(100% - 10px); height: 300px;"
      :chart="conf"
      :data="chartData"
    />
  </div>
</template>

<script>
import ChartBox from "../components/chartBox";
export default {
  props: ["conf"],
  data() {
    return {
      chartData: []
    };
  },
  created() {
    this.fetchData();
  },
  components: {
    ChartBox
  },
  methods: {
    fetchData() {
      this.$axios.get(`/chart/data?chartId=${this.conf.chartId}`,{
          timeout: 100000
        }).then(data => {
        this.chartData = data;
      });
    }
  }
};
</script>

<style lang="less">
.chart-container {
  border: 1px solid #e6e6e6;
  padding: 20px;
  flex: calc(50% - 20px);
  cursor: default;
  background: #fff;
  &:hover {
    box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1), 0 16px 24px 0 rgba(0, 0, 0, 0.1);
  }
  .chart-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
    .title {
      font-size: 16px;
      font-weight: 500;
    }
    .button-list {
      .button {
        margin-right: 10px;
        cursor: pointer;
        color: #666;
      }
    }
  }
}
</style>