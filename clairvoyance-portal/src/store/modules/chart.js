import axios from "@/utils/request.js";
import Vue from 'vue'
export default {
  namespaced: true,
  state: {
    chartId: '',
    chart: {},
    workSheet: {},
    chartData: [],

    // 编辑筛选项
    editingFilter: null,
    rangeData: [],
  },
  getters: {
    sqlConfig: state => state.chart.sqlConfig,
    chart: state => state.chart,
    chartData: state => state.chartData,
    workSheet: state => state.workSheet,
    fields: state => state.workSheet.fields,
    filters: (state, getters) => getters.sqlConfig.filters,
    editingFilter: state => state.editingFilter,
    rangeData: state => state.rangeData,
    innerFilters: (state, getters) => getters.sqlConfig.innerFilters,
  },
  mutations: {
    CLEAR_CHART(state) {
      state.workSheet = {}
      state.chartData = []
    },
    INIT_CHART(state, data) {
      state.chart = data
      this.commit('chart/CLEAR_CHART')
    },
    INIT_CHART_DATA(state, data) {
      state.chartData = data
    },
    INIT_WORKSHEET(state, data) {
      state.workSheet = data
    },
    SAVE_FILTER(state) {
      const filters = state.chart.sqlConfig.filters
      const editingFilter = state.editingFilter
      const target = filters.find(i => i.id === editingFilter.id)
      if (target) {
        Object.assign(target, editingFilter)
      } else {
        filters.push(editingFilter)
      }
    }
  },
  actions: {
    initChart({ dispatch, commit }, chartId) {
      axios.get(`/chart?chartId=${chartId}`).then(data => {
        commit('INIT_CHART', data)
        dispatch('initWorkSheet', data.workSheetId)
        return dispatch('fetchChartData', chartId)
      });
    },
    fetchChartData({ commit }, chartId) {
      axios
        .get(`/chart/data?chartId=${chartId}`, {
          timeout: 10000000
        })
        .then(data => {
          commit('INIT_CHART_DATA', data)
        });
    },
    initWorkSheet({ commit }, workSheetId) {
      axios.get(`/workSheet?id=${workSheetId}`).then(data => {
        commit('INIT_WORKSHEET', data)
      });
    },
    saveChart({ dispatch, state }, notInit) {
      axios.post("/chart", state.chart).then(b => {
        if (b && !notInit) {
          dispatch('initChart', state.chart.chartId);
        }
      });
    },
    chooseField({ dispatch, state }, { mode, data }) {
      state.chart.sqlConfig[mode] = data
      return dispatch('saveChart')
    },
    editFilter({ state, dispatch }, data) {
      let filter;
      if (!data.filterType) {
        filter = {
          ...data,
          included: false,
          range: [],
          filterType: data.type === 'text' ? 'exact' : 'condition'
        }
      } else {
        filter = {
          ...data,
          ...state.workSheet.fields.find(i => i.id === data.id)
        }
      }
      state.editingFilter = filter
      if (filter.filterType === 'exact') {
        dispatch('fetchRangeData')
      }
    },
    fetchRangeData({ state }) {
      state.rangeData = []
      axios.post('/workSheet/range', {
        workSheetId: state.workSheet.workSheetId,
        fieldId: state.editingFilter.id
      }, { timeout: 10000000 }).then(data => {
        state.rangeData = disinct(data.range)
      })

    },
    newFilter({ dispatch }, data) {
      dispatch('editFilter', data)
    },
    saveFilter({ commit, dispatch }) {
      commit('SAVE_FILTER')
      return dispatch('saveChart')
    },
    newInnerFilter({ state, dispatch }, data) {
      state.chart.sqlConfig.innerFilters.push({
        filterType: 'inner',
        range: [],
        ...data
      })
      return dispatch('saveChart')
    },
    initInnerFilterRange({ state }, id) {
      const targ = state.chart.sqlConfig.innerFilters.find(i => i.id === id)
      const filter = state.chart.sqlConfig.filters.find(i => i.id === id)
      if (filter && filter.included) {
        Vue.set(targ, 'rangeData', filter.range)
        return
      }
      axios.post('/workSheet/range', {
        workSheetId: state.workSheet.workSheetId,
        fieldId: id
      }, { timeout: 10000000 }).then(data => {
        let rangeData = disinct(data.range)
        if (filter && !filter.included) {
          rangeData = rangeData.filter(i => !filter.range.includes(i))
        }
        Vue.set(targ, 'rangeData', rangeData)
      })
    },
    setInnerFilterRnage({ state, dispatch }, { id, value }) {
      state.chart.sqlConfig.innerFilters.find(i => i.id === id).range = value ? [value] : []
      return dispatch('saveChart')
    },
    removeFilter({ state, dispatch }, filter) {
      const filters = state.chart.sqlConfig.filters
      const index = filters.findIndex(i => i.id === filter.id)
      if (index !== -1) {
        filters.splice(index, 1)
        return dispatch('saveChart')
      }
    },
    removeInnerFilter({ state, dispatch }, filter) {
      const filters = state.chart.sqlConfig.innerFilters
      const index = filters.findIndex(i => i.id === filter.id)
      if (index !== -1) {
        filters.splice(index, 1)
        return dispatch('saveChart')
      }
    }
  },
}

function disinct(arr) {
  const res = []
  arr.forEach(value => {
    if (!res.includes(value)) {
      res.push(value)
    }
  })
  return res
}