<template>
  <div style="editor-container">
    <a-page-header
      style="border: 1px solid rgb(235, 237, 240)"
      title="编辑图表"
      @back="saveChart(); $router.go(-1);"
    />
    <div class="editor-main" v-if="chart">
      <div class="left-container">
        <div style="width: 100%;">
          <a-select v-model="chart.workSheetId" style="width: 188px;margin-right:10px;">
            <a-select-option
              v-for="sheet in workSheets"
              :key="sheet.workSheetId"
              :value="sheet.workSheetId"
            >{{sheet.title}}</a-select-option>
          </a-select>
          <a-button icon="eye"></a-button>
        </div>
        <div class="field-list">
          <a-tabs size="small" style="margin-top: 10px;" default-active-key="1">
            <a-tab-pane key="1" tab="字段">
              <div class="field-origin-list">
                <div class="field-item" v-for="field in fields" :key="field.name">{{field.title}}</div>
              </div>
            </a-tab-pane>
            <a-tab-pane key="2" tab="参数"></a-tab-pane>
          </a-tabs>
        </div>
      </div>
      <div class="chart-main">
        <div class="chart-args">
          <field-choose
            mode="x"
            class="arg"
            :fields="fields"
            :arr-data="chart.sqlConfig.x"
            :sql-config="chart.sqlConfig"
            @save="saveChart"
          />
          <field-choose
            mode="y"
            class="arg"
            :fields="fields"
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
  </div>
</template>

<script>
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
      fields: [],
      workSheetId: ""
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
    RightController
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
        this.fields = data.fields;
      });
    },
    fetchChartData() {
      this.$axios
        .get(`/chart/data?chartId=${this.chart.chartId}`)
        .then(data => {
          this.chartData = data;
        });
    },
    saveChart() {
      this.chart.chartType = this.chart.chartType || "UNKNOWN";
      this.$axios.post("/chart", this.chart).then(b => {
        if (b) {
          this.initChart();
        }
      });
    }
  }
};
</script>

<style lang="less">
// .editor-container {
//   position: relative;
//   height: 100%;
.editor-main {
  position: relative;
  display: flex;
  background: #f6f6f6;
  height: calc(100vh - 68px);
  overflow: hidden;
  .left-container {
    padding: 10px;
    width: 250px;
    height: calc(100vh - 111px);
    .field-list {
      margin-top: 10px;
      border: 1px solid #e6e6e6;
      background: #fff;
      padding: 0 20px 20px 20px;
      height: 100%;
      overflow: auto;
      .field-item {
        padding: 10px;
      }
    }
  }
  .chart-main {
    position: relative;
    margin-top: 10px;
    height: calc(100vh - 90px);
    width: calc(100% - 260px);
    .chart-args {
      padding: 20px;
      border: 1px solid #e6e6e6;
      background: #fff;
      margin-bottom: 10px;
      .arg {
        margin-bottom: 10px;
      }
    }
    .chart-filter-container {
      width: 250px;
      padding: 20px;
      border: 1px solid #e6e6e6;
      background: #fff;
      height: calc(100vh - 240px);
      overflow: auto;
      margin-right: 10px;
    }
    .chart-box {
      padding: 20px;
      border: 1px solid #e6e6e6;
      background: #fff;
      width: 100%;
      height: calc(100vh - 240px);
      width: calc(100% - 260px);
    }
  }
}
// }
</style>