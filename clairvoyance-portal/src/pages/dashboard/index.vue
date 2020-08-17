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
              <a-button icon="filter" @click="setGlobalFilterVisible = true;">全局筛选</a-button>
              <a-button icon="fullscreen" disabled>全屏</a-button>
              <a-button icon="share-alt" disabled>分享</a-button>
              <a-button icon="more" disabled>更多</a-button>
            </div>
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
      width="60%"
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
export default {
  data() {
    return {
      visible: false,
      confirmLoading: false,
      confirmLoading2: false,
      workSheets: [],
      form: {},
      setGlobalFilterVisible: false
    };
  },
  components: {
    Catalogue,
    ChartGrid,
    DashboardFilter
  },
  computed: {
    ...mapGetters('dashboard',['charts','dashboard']),
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