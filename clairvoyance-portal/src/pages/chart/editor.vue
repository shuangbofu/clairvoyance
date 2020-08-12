<template>
  <div style="editor-container">
    <a-page-header title="编辑图表" @back="saveChart(true); $router.go(-1);" />
    <div class="editor-main" v-if="sqlConfig">
      <div class="left-container" v-if="workSheet">
        <div class="top-desc" @click="previewVisible = true;">
          <div style="margin-left: 10px;">工作表</div>
          <div class="top-title">
            {{workSheet.title}}
            <a-icon type="swap"></a-icon>
          </div>
        </div>
        <field-list />
      </div>
      <div class="chart-right">
        <div class="chart-args">
          <field-choose mode="drill" class="arg" v-if="hasDrill" />
          <field-choose mode="x" class="arg" />
          <field-choose mode="y" class="arg" />
        </div>
        <div class="chart-main" :style="{
          'height': chartMainheight
        }">
          <chart-filter class="chart-filter-container" />
          <chart-box @click="onClick" class="chart-box" :chart-layer="chartLayer" :data="chartData">
            <div slot="header" style="display:flex; align-items: center;">
              <a-icon
                v-if="innerFilters.length > 0"
                style="font-size: 18px; color: #4876ff; margin-right: 10px;"
                type="filter"
              />
              <div
                style="margin-right: 10px;"
                v-for="(innerFilter,index) in innerFilters"
                :key="index"
              >
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
            <div slot="footer" class="chart-footer" v-if="drillValues.length>0">
              <drill-crumbs
                :first="drillFields[0].realAliasName"
                :arr="drillValues"
                @click="rollUp"
              />
            </div>
          </chart-box>
        </div>
      </div>
      <right-controller @refresh="saveChart" />
    </div>
    <a-modal
      v-if="workSheet"
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
import DrillCrumbs from '../components/drillCrumbs'
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
      'chartLayer',
      'drillFields',
      'sqlConfig',
      'chartData',
      'innerFilters',
      'innnerFilterRangeData',
      'drillValues'
    ]),
    hasDrill() {
      return this.drillFields.length > 0
    },
    chartMainheight() {
      let minus = 167
      if(this.hasDrill) {
        minus += 48
      }
      console.log(minus)
      return 'calc(100vh - ' + minus + 'px)'
    }
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
    DrillCrumbs
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
    },
    onClick(e) {
      this.$store.dispatch('chart/drill', e.name)
    },
    rollUp(index) {
      this.$store.dispatch('chart/rollUp',index)
    }
  }
};
</script>

<style lang="less">
.editor-main {
  position: relative;
  display: flex;
  background: #f6f6f6;
  // background: #fff;
  height: calc(100vh - 67px);
  overflow: hidden;
  .left-container {
    width: 240px;
    height: calc(100vh - 67px);
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
  .chart-right {
    // padding-left: 15px;
    position: relative;
    margin-top: 10px;
    height: calc(100vh - 87px);
    width: calc(100% - 255px);
    .chart-args {
      padding-left: 15px;
      .arg {
        margin-bottom: 8px;
        &:last-child {
          margin: 0;
        }
      }
    }
    .chart-main {
      // height: calc(100vh - 215px);
      display: flex;
      .chart-filter-container {
        padding-left: 15px;
        width: 210px;
        border-right: 1px solid #e6e6e6;
        height: 100%;
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
        height: calc(100% - 27px);
        .echarts {
          height: calc(100% - 55px);
        }
      }
      .chart-footer {
        padding: 10px;
      }
    }
  }
}
</style>