<template>
  <div class="filters-container">
    <div class="left-list">
      <nestedFilter v-model="globalFilters" />
    </div>
    <div class="right-filter-detail">
      <div class="main-container">
        <div class="check-container">
          <div>
            <div>筛选器相关的图标</div>
            <a-checkbox-group
              v-model="activeFilter.selectedCharts"
              :options="chartsOption"
              @change="onCheckChart"
            />
          </div>
          <div>请选择作为筛选器的字段</div>
        </div>
      </div>
      <div class="field-show">ddd</div>
    </div>
  </div>
</template>

<script>
import NestedFilter from './nestedFilter'
import { mapGetters } from 'vuex'
export default {
  components: {
    NestedFilter
  },
  computed: {
    ...mapGetters('dashboard',['charts','activeFilter']),
    globalFilters: {
      get() {
        return this.$store.getters['dashboard/globalFilters']
      },
      set(value) {
        this.$store.dispatch('dashboard/updateFitlers', value)
      }
    },
    chartsOption() {
        return this.charts.map(i=> {
          return {
            label: i.name,
            value: i.chartId
          }
        })
    },
    selectedSheets() {
      return this.charts.filter(i=>this.activeFilter.selectedCharts.includes(i.chartId)).map(i=>i.workSheetId)
    }
  },
  methods: {
    onCheckChart() {

    }
  }
}
</script>

<style lang="less">
.filters-container {
  display: flex;
  .left-list {
    background: #e6e6e6;
    width: 200px;
    height: 100%;
  }
  .right-filter-detail {
    padding: 0 20px;
    width: 100%;
    display: flex;
    .main-container {
    }
    .field-show {
    }
  }
}
</style>