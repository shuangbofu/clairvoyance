<template>
  <div>
    <catalogue item="dashboard" @choose="chooseDashboard" ref="catRef">
      <template slot="right-container">
        <div class="dashboard-main" v-if="dashboard">
          <div class="dashboard-header">
            <div class="title">
              {{dashboard.name}}
              <div class="remarks">{{dashboard.remarks}}</div>
            </div>
            <div class="button-list">
              <a-button icon="bar-chart" @click="addChart">添加图表</a-button>
              <a-button icon="highlight" disabled>设计</a-button>
              <a-button icon="filter" @click="openGlobalFilter">全局筛选</a-button>
              <a-button icon="fullscreen" disabled>全屏</a-button>
              <a-button icon="share-alt" disabled>分享</a-button>
              <a-button icon="more" disabled>更多</a-button>
            </div>
          </div>
          <div style="padding: 0 5px; margin-bottom: 5px;">
            <a-icon
              v-if="flatFilters.length > 0"
              style="font-size: 18px; color: #4876FF; margin-right: 10px;"
              type="filter"
            />
            <a-select
              v-model="filterParams[filter.id]"
              v-for="(filter, index) in flatFilters"
              :key="index"
              :allowClear="true"
              show-search
              :filter-option="filterOption"
              style="width: 150px; margin-right: 10px;"
              :placeholder="filter.name"
              @dropdownVisibleChange="open => onFilterSelectDropdown(open, filter.id)"
              @change="value => changeFilterValue(value, filter.id)"
            >
              <a-select-option
                v-for="(value, index) in selectRange[filter.id]"
                :value="value"
                :key="index"
              >{{value}}</a-select-option>
            </a-select>
          </div>
          <div class="charts-container">
            <template v-if="charts.length ===0">
              <div class="empty-block" @click="addChart">
                <div style="margin: 30px;">
                  <a-icon class="icon" type="plus" />
                  <div>添加图表</div>
                </div>
              </div>
            </template>
            <template v-else>
              <chart-grid
                ref="chartGrid"
                @update="updateLayout"
                :layouts="dashboard.layoutConfig.positions"
                :charts="charts"
              />
            </template>
          </div>
        </div>
      </template>
    </catalogue>
    <a-modal
      title="添加图表"
      :visible="visible"
      :confirm-loading="confirmLoading"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="() => {visible = false; form ={};}"
    >
      <a-form-model-item label="选择工作表">
        <a-select
          v-model="form.workSheetId"
          style="width: 100%;"
          show-search
          :filter-option="filterOption"
        >
          <a-select-option
            v-for="sheet in workSheets"
            :key="sheet.workSheetId"
            :value="sheet.workSheetId"
          >{{sheet.title}}</a-select-option>
        </a-select>
      </a-form-model-item>
    </a-modal>
    <a-modal
      width="800px"
      v-if="dashboard"
      title="全局筛选"
      :visible="setGlobalFilterVisible"
      :confirm-loading="confirmLoading2"
      :destroyOnClose="true"
      @ok="handleSetGlobalFilterOk"
      @cancel="() => {setGlobalFilterVisible = false;}"
    >
      <dashboard-filter />
    </a-modal>
  </div>
</template>

<script>
import ChartGrid from './chartGrid'
import Catalogue from "../components/catalogue";
import DashboardFilter from './dashboardFilter'
import { mapGetters } from 'vuex'

function findParentFilters(id,filters,arr) {
  const filter = filters.find(i=> i.id=== id)
  if(filter) {
    arr.push(filter)
    findParentFilters(filter.parentId, filters, arr)
  }
}

export default {
  data() {
    return {
      visible: false,
      confirmLoading: false,
      confirmLoading2: false,
      workSheets: [],
      selectRange: {},
      form: {},
      setGlobalFilterVisible: false,
    };
  },
  components: {
    Catalogue,
    ChartGrid,
    DashboardFilter
  },
  computed: {
    ...mapGetters('dashboard',['charts','dashboard','flatFilters','filterParams','globalFilterParams']),
  },
  methods: {
    chooseDashboard(id) {
      this.$store.dispatch('dashboard/initDashboard',id).then(() => {
        this.$refs.catRef.loaded();
      })
    },
    addChart() {
      this.visible = true;
      this.$axios.get("/workSheet/list").then(data => {
        this.workSheets = data;
      });
    },
    handleOk() {
      this.confirmLoading = true;
      this.$axios
        .post("/chart", {
          ...this.form,
          dashboardId: this.dashboard.dashboardId
        })
        .then(chartId => {
          this.$message.success("创建成功！");
          this.visible = false;
          this.confirmLoading = false;
          this.$emit("refresh");
          this.$store.commit('chart/CLEAR_CHART')
          this.$router.push({
            name: "编辑图表",
            query: { chartId }
          });
        })
        .catch(() => {
          this.confirmLoading = false;
        });
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    },
    updateLayout(layout) {
      this.$axios.put('/dashboard',{
        dashboardId: this.dashboard.dashboardId,
        layoutConfig: {
          positions: layout
        }
      })
    },
    handleSetGlobalFilterOk() {

    },
    openGlobalFilter() {
      this.setGlobalFilterVisible = true;
      this.$store.dispatch('dashboard/initWorkSheets')
      this.$store.dispatch('dashboard/getRange')
    },
    onFilterSelectDropdown(open, dashboardFilterId) {
      if(!open){return}
      const params = this.getFilterParentParams(dashboardFilterId)
      this.$axios.post(`/dashboard/filter/range/${dashboardFilterId}`,params).then(data => {
        this.selectRange[dashboardFilterId] = data.range
        const ranges = {...this.selectRange}
        ranges[dashboardFilterId] = data.range
        this.selectRange = ranges
      })
    },
    changeFilterValue(value, dashboardFilterId) {
     const filter = this.flatFilters.find(i=>i.id === dashboardFilterId)
     if(filter) {
        this.$refs.chartGrid.refresh(filter.selectedCharts)
     }
    },
    getFilterParentParams(id) {
      let res  = []
      findParentFilters(id, this.flatFilters, res)
      res = res.filter(i=>i.id !== id)
      const ids  = res.map(i=>i.id)
      return this.globalFilterParams.filter(i=>ids.includes(i.dashboardFilterId))
    }
  }
};
</script>

<style lang="less">
.dashboard-main {
  .dashboard-header {
    display: flex;
    justify-content: space-between;
    .title {
      font-size: 28px;
      color: #4876ff;
      .remarks {
        font-size: 14px;
        color: #777;
      }
    }
    button {
      margin-left: -1px;
    }
  }
  .charts-container {
    .empty-block {
      padding: 40px;
      border: 1px solid #e6e6e6;
      width: 240px;
      height: 240px;
      text-align: center;
      cursor: pointer;
      background: #fff;
      .icon {
        font-size: 60px;
        margin: auto;
      }
      color: #888;
      &:hover {
        color: #4876ff;
        border: 1px solid #4876ff;
      }
    }
    .chart-container {
      height: 100%;
    }
  }
}
</style>