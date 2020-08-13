<template>
  <div class="chart-container">
    <div class="chart-header">
      <div class="title">
        <a-tooltip placement="bottomLeft" :title="drillTip">
          <a-icon class="drill-tip" type="block" v-if="chart.sqlConfig.drillFields.length > 0" />
        </a-tooltip>
        {{chart.name === '' ? '未命名图表' : chart.name}}
      </div>
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
            <a-menu-item key="5">移动图表</a-menu-item>
            <a-menu-item key="6">复制图表</a-menu-item>
            <a-menu-item key="7">删除</a-menu-item>
          </a-menu>
          <a-icon class="button" type="more" />
        </a-dropdown>
      </div>
    </div>
    <div :style="{
      height: `calc(100% - ${drillParam.values.length > 0 ? 50: 29}px)`
    }">
      <chart-box
        ref="chartBox"
        @click="onClick"
        style="position: relative; width: calc(100% - 10px); height: 100%;"
        :chart-layer="chart.sqlConfig.layers[drillParam.level]"
        :data="chartData"
      />
    </div>
    <drill-crumbs
      slot="footer"
      v-if="drillParam.values.length > 0"
      :first="chart.sqlConfig.drillFields[0].realAliasName"
      :arr="drillParam.values"
      @click="rollUp"
    />
  </div>
</template>

<script>
import ChartBox from "../components/chartBox";
import DrillCrumbs from '../components/drillCrumbs'
export default {
  props: ["chart"],
  data() {
    return {
      chartData: [],
      drillParam: {
        level: 0,
        values:[]
      }
    };
  },
  created() {
    this.fetchData();
  },
  computed: {
    fillInnerFilter() {
      return this.chart.sqlConfig.innerFilters.filter(i=>i.range.length > 0).length > 0
    },
    drillTip() {
      const tip = this.chart.sqlConfig.drillFields.map(i=> {
        return i.realAliasName + ' > '
      }).toString().replace(/,/g,'')
      if(tip) {
        return tip.substring(0, tip.length - 3)
      }
      return ''
    }
  },
  components: {
    ChartBox,
    DrillCrumbs
  },
  methods: {
    fetchData() {
      this.$axios.post(`/chart/data/${this.chart.chartId}`,this.drillParam,{
          timeout: 10000000
        }).then(data => {
        this.chartData = data;
      });
    },
    link2Editor() {
      this.$store.commit('chart/CLEAR_CHART')
      this.$router.push({name: '编辑图表', query:{chartId: this.chart.chartId}})
    },
    // 和chart.js vuex中 下钻和上卷执行操作相同
    onClick(e) {
      if(this.drillParam.level + 1 < this.chart.sqlConfig.drillFields.length) {
        this.drillParam.values.push(e.name)
        this.drillParam.level+=1
        this.fetchData()
      }
    },
    rollUp(index) {
      if (index >= this.drillParam.level) {
        return;
      }
      this.drillParam.values = this.drillParam.values.slice(0, index)
      this.chartData = []
      this.drillParam.level = index
      this.fetchData()
    },
    resize() {
      if(this.$refs.chartBox) {
        this.$refs.chartBox.resize()
      }
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
      .drill-tip {
        color: #4876ff;
        font-weight: 300;
        cursor: pointer;
      }
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