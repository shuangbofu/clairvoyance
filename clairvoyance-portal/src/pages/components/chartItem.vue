<template>
  <div class="chart-container">
    <div class="chart-header">
      <div class="title">{{chart.name}}</div>
      <div class="button-list">
        <a-icon class="button" type="edit" @click="link2Editor" />
        <a-icon class="button" type="redo" @click="fetchData" />
        <a-icon class="button" type="fullscreen" />
        <a-icon
          :theme="fillInnerFilter ? 'filled': 'outlined'"
          class="button"
          :style="{
            color: fillInnerFilter ? '#4876FF': ''
          }"
          type="filter"
        />
        <a-dropdown>
          <a-menu slot="overlay">
            <a-menu-item key="3">导出图片</a-menu-item>
            <a-sub-menu key="4" title="导出数据">
              <a-menu-item key="4-1">导出Excel</a-menu-item>
              <a-menu-item key="4-2">快照工作表</a-menu-item>
            </a-sub-menu>
            <a-menu-item key="5">移动图标</a-menu-item>
            <a-menu-item key="6">复制图标</a-menu-item>
            <a-menu-item key="7">删除</a-menu-item>
          </a-menu>
          <a-icon class="button" type="more" />
        </a-dropdown>
      </div>
    </div>
    <chart-box
      style="position: relative; width: calc(100% - 10px); height: 300px;"
      :chart="chart"
      :data="chartData"
    />
  </div>
</template>

<script>
import ChartBox from "../components/chartBox";
export default {
  props: ["chart"],
  data() {
    return {
      chartData: []
    };
  },
  created() {
    this.fetchData();
  },
  computed: {
    fillInnerFilter() {
      return this.chart.sqlConfig.innerFilters.filter(i=>i.range.length > 0).length > 0
    }
  },
  components: {
    ChartBox
  },
  methods: {
    fetchData() {
      this.$axios.get(`/chart/data?chartId=${this.chart.chartId}`,{
          timeout: 10000000
        }).then(data => {
        this.chartData = data;
      });
    },
    link2Editor() {
      this.$store.commit('chart/CLEAR_CHART')
      this.$router.push({name: '编辑图表', query:{chartId: this.chart.chartId, workSheetId: this.chart.workSheetId}})
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