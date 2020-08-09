<template>
  <div style="editor-container">
    <a-page-header title="编辑图表" @back="saveChart(true); $router.go(-1);" />
    <div class="editor-main" v-if="sqlConfig">
      <div class="left-container">
        <div class="top-desc" @click="previewVisible = true;">
          <div style="margin-left: 10px;">工作表</div>
          <div class="top-title">
            {{workSheet.title}}
            <a-icon type="swap"></a-icon>
          </div>
        </div>
        <field-list />
      </div>
      <!-- <div style="width: 0; position:relative;">
        <div class="hidden-button">
          <a-icon class="icon" type="right" />
        </div>
      </div>-->
      <div class="chart-main">
        <div class="chart-args">
          <field-choose mode="x" class="arg" />
          <field-choose mode="y" class="arg" />
        </div>
        <div style="height: calc(100vh - 230px); display:flex;">
          <chart-filter class="chart-filter-container" />
          <chart-box class="chart-box" :chart="chart" :data="chartData">
            <div slot="header" style="display:flex; align-items: center;">
              <a-icon style="font-size: 20px; color: #d6d6d6; margin-right: 10px;" type="filter" />
              <div
                style="margin-right: 10px;"
                v-for="(innerFilter,index) in innerFilters"
                :key="index"
              >
                <!-- <span style="margin-right: 6px;">{{innerFilter.title}}:</span> -->
                <a-select
                  :placeholder="innerFilter.title"
                  v-model="innerFilter.range[0]"
                  :allowClear="true"
                  show-search
                  :filter-option="filterOption"
                  @change="value => $store.dispatch('chart/setInnerFilterRnage',{id: innerFilter.id, value})"
                  @dropdownVisibleChange="open => {if(open) {$store.dispatch('chart/initInnerFilterRange',innerFilter.id)}}"
                  style="width: 120px;"
                >
                  <template v-if="innerFilter.rangeData">
                    <a-select-option
                      v-for="(value, index) in innerFilter.rangeData"
                      :key="index"
                      :value="value"
                    >{{value}}</a-select-option>
                  </template>
                </a-select>
              </div>
            </div>
          </chart-box>
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
import ChartFilter from './chartFilter'
import FieldList from './fieldList'
import DataPreview from "../components/preview";
import RightController from "./rightController";
import ChartBox from "../components/chartBox";
import FieldChoose from "./fieldChoose";
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      workSheets: [],
      previewVisible: false,
    };
  },
  computed: {
    ...mapGetters('chart',[
      'workSheet',
      'chart',
      'sqlConfig',
      'chartData',
      'innerFilters',
      'innnerFilterRangeData'
    ])
  },
  created() {
    const chartId = this.$route.query.chartId;
    this.getWorkSheets();
    this.$store.dispatch('chart/initChart',chartId)
  },
  components: {
    FieldChoose,
    ChartBox,
    RightController,
    DataPreview,
    FieldList,
    ChartFilter,
  },
  methods: {
    getWorkSheets() {
      this.$axios.get("/workSheet/list").then(data => {
        this.workSheets = data;
      });
    },
    saveChart(notInit) {
      this.$store.dispatch('chart/saveChart',notInit)
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
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
    width: 240px;
    height: calc(100vh - 79px);
    background: #fff;
    border-right: 1px solid #e6e6e6;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    .top-desc {
      padding: 12px 0;
      border-bottom: 1px solid #e6e6e6;
      .top-title {
        cursor: pointer;
        padding: 0 16px;
        margin-top: 10px;
        color: #888;
        line-height: 32px;
        height: 32px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        text-align: center;
        font-size: 14px;
        font-weight: 500;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        &:hover {
          background: #f6f6f6;
        }
      }
    }
  }
  .hidden-button {
    position: absolute;
    width: 15px;
    height: 35px;
    background: #e3e7e8;
    top: 50%;
    cursor: pointer;
    .icon {
      padding: 8px 0;
      color: #666;
      cursor: pointer;
    }
  }
  .chart-main {
    padding-left: 15px;
    position: relative;
    margin-top: 10px;
    height: calc(100vh - 87px);
    width: calc(100% - 255px);
    .chart-args {
      .arg {
        margin-bottom: 8px;
        &:last-child {
          margin: 0;
        }
      }
    }
    .chart-filter-container {
      width: 210px;
      // padding: 10px;
      // border: 1px solid #e6e6e6;
      border-right: 1px solid #e6e6e6;
      height: calc(100vh - 187px);
      overflow: auto;
      margin-right: 15px;
    }
    .chart-box {
      margin-top: 15px;
      box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1),
        0 16px 24px 0 rgba(81, 129, 228, 0.1);
      padding: 20px;
      background: #fff;
      width: 100%;
      height: calc(100vh - 202px);
      width: calc(100% - 210px);
    }
  }
}
</style>