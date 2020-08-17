<template>
  <div class="filters-container">
    <div class="left-list">
      <nestedFilter v-model="globalFilters" />
    </div>
    <div class="right-filter-detail">
      <div class="main-container">
        <div class="check-container">
          <div style="margin-bottom: 20px;">
            <div class="title">筛选器相关的图标</div>
            <a-checkbox-group
              v-model="activeFilter.selectedCharts"
              :options="chartsOption"
              @change="onCheckChart"
            />
          </div>
          <div>
            <div class="title">请选择作为筛选器的字段</div>
            <div
              style="margin-bottom: 5px;"
              v-for="sheet in selectedSheets"
              :key="sheet.workSheetId"
            >
              {{sheet.title}}
              <a-select
                v-model="activeFilter.sheetFieldMap[sheet.workSheetId]"
                @change="changeField"
                size="small"
                style="width: 150px;"
              >
                <a-select-option
                  v-for="field in sheet.fields.filter(i=>i.type === 'text')"
                  :key="field.id"
                >{{field.title}}</a-select-option>
              </a-select>
            </div>
          </div>
        </div>
      </div>
      <div class="field-show">
        <div class="field-item" v-for="(value, index) in fieldRange" :key="index">{{value}}</div>
      </div>
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
    ...mapGetters('dashboard',['charts','activeFilter','workSheets','fieldRange']),
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
      const ids = this.charts.filter(i=>this.activeFilter.selectedCharts.includes(i.chartId)).map(i=>i.workSheetId)
      return this.workSheets.filter(i=>ids.includes(i.workSheetId))
    }
  },
  methods: {
    onCheckChart() {

    },
    changeField() {
      this.$store.dispatch('dashboard/getRange')
    }
  }
}
</script>

<style lang="less">
.filters-container {
  display: flex;
  .left-list {
    background: #fafafa;
    width: 200px;
    height: 100%;
  }
  .right-filter-detail {
    padding: 0 20px;
    width: 100%;
    display: flex;
    .main-container,
    .field-show {
      .title {
        border-left: 10px solid #4876ff;
        margin-bottom: 10px;
        padding-left: 10px;
      }
    }
    .main-container {
      font-size: 13px;
    }
    .field-show {
      width: 180px;
      height: 250px;
      overflow: auto;
      border: 1px solid #e6e6e6;
      padding: 20px;
      .field-item {
        margin-bottom: 4px;
        font-size: 13px;
        color: #777;
      }
    }
  }
}
</style>