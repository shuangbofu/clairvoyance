<template>
  <div style="editor-container">
    <a-page-header
      style="border: 1px solid rgb(235, 237, 240)"
      title="编辑图表"
      @back="saveChart(true); $router.go(-1);"
    />
    <div class="editor-main" v-if="chart">
      <div class="left-container">
        <div style="width: 100%;display:flex;">
          <a-button class="top-title" @click="previewVisible = true;">{{workSheet.title}}</a-button>
          <a-button icon="swap"></a-button>
        </div>
        <field-list :fields="workSheet.fields" />
      </div>
      <div class="chart-main">
        <div class="chart-args">
          <field-choose
            mode="x"
            class="arg"
            :fields="workSheet.fields"
            :arr-data="chart.sqlConfig.x"
            :sql-config="chart.sqlConfig"
            @save="saveChart"
          />
          <field-choose
            mode="y"
            class="arg"
            :fields="workSheet.fields"
            :arr-data="chart.sqlConfig.y"
            :sql-config="chart.sqlConfig"
            @save="saveChart"
          />
        </div>
        <div style="height: calc(100vh - 230px); display:flex;">
          <div class="chart-filter-container"></div>
          <chart-box class="chart-box" :chart="chart" :data="chartData" />
        </div>
      </div>
      <right-controller :chart="chart" @refresh="saveChart" />
    </div>
    <a-modal
      :destroyOnClose="true"
      :visible="previewVisible"
      width="80%"
      @cancel="() => {previewVisible = false; form ={};}"
      :footer="null"
      :title="`查看数据 - ${workSheet.title}`"
    >
      <data-preview :work-sheet="workSheet" />
    </a-modal>
  </div>
</template>

<script>
import FieldList from './fieldList'
import DataPreview from "../components/preview";
import RightController from "./rightController";
import ChartBox from "../components/chartBox";
import FieldChoose from "./fieldChoose";
export default {
  data() {
    return {
      chartId: "",
      chart: null,
      workSheets: [],
      chartData: [],
      workSheet: {},
      workSheetId: "",
      previewVisible: false
    };
  },
  created() {
    this.chartId = this.$route.query.chartId;
    this.workSheetId = this.$route.query.workSheetId;

    this.getWorkSheets();
    this.initChart();
    this.getWorkSheet();
  },
  components: {
    FieldChoose,
    ChartBox,
    RightController,
    DataPreview,
    FieldList
  },
  methods: {
    initChart() {
      this.$axios.get(`/chart?chartId=${this.chartId}`).then(data => {
        this.chart = data;
        this.fetchChartData();
      });
    },
    getWorkSheets() {
      this.$axios.get("/workSheet/list").then(data => {
        this.workSheets = data;
      });
    },
    getWorkSheet() {
      this.$axios.get(`/workSheet?id=${this.workSheetId}`).then(data => {
        this.workSheet = data;
      });
    },
    fetchChartData() {
      this.$axios
        .get(`/chart/data?chartId=${this.chart.chartId}`,{
          timeout: 100000
        })
        .then(data => {
          this.chartData = data;
        });
    },
    saveChart(notInit) {
      this.chart.chartType = this.chart.chartType || "UNKNOWN";
      this.$axios.post("/chart", this.chart).then(b => {
        if (b && !notInit) {
          this.initChart();
        }
      });
    }
  }
};
</script>

<style lang="less">
.editor-main {
  position: relative;
  display: flex;
  background: #f6f6f6;
  height: calc(100vh - 67px);
  overflow: hidden;
  .left-container {
    padding: 10px;
    width: 240px;
    height: calc(100vh - 109px);
    .top-title {
      width: 188px;
      margin-right: 10px;
      text-align: center;
      font-weight: 500;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .field-list {
      margin-top: 10px;
      // border: 1px solid #e6e6e6;
      background: #fff;
      padding: 0 20px;
      height: 100%;
      overflow: auto;
      .field-item {
        padding: 10px;
      }
      .ant-tabs-nav .ant-tabs-tab {
        margin-right: 10px;
      }
    }
  }
  .chart-main {
    position: relative;
    margin-top: 10px;
    height: calc(100vh - 87px);
    width: calc(100% - 250px);
    .chart-args {
      padding: 20px;
      // border: 1px solid #e6e6e6;
      background: #fff;
      margin-bottom: 10px;
      .arg {
        margin-bottom: 10px;
        &:last-child {
          margin: 0;
        }
      }
    }
    .chart-filter-container {
      width: 210px;
      padding: 20px;
      // border: 1px solid #e6e6e6;
      background: #fff;
      height: calc(100vh - 227px);
      overflow: auto;
      margin-right: 10px;
    }
    .chart-box {
      margin-top: 40px;
      box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1),
        0 16px 24px 0 rgba(81, 129, 228, 0.1);
      padding: 20px;
      background: #fff;
      width: 100%;
      height: calc(100vh - 267px);
      width: calc(100% - 210px);
    }
  }
}
// }
</style>