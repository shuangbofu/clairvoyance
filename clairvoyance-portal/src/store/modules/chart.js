import axios from "@/utils/request.js";
export default {
  namespaced: true,
  state: {
    chartId: '',
    chart: {},
    workSheet: {},
    chartData: [],

    // 编辑筛选项
    editingFilter: null,
    rangeData: []
  },
  getters: {
    sqlConfig: state => state.chart.sqlConfig,
    chart: state => state.chart,
    chartData: state => state.chartData,
    workSheet: state => state.workSheet,
    fields: state => state.workSheet.fields,
    filters: (state, getters) => getters.sqlConfig.filters,
    editingFilter: state => state.editingFilter,
    rangeData: state => state.rangeData
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
      // filter['@type'] = filter.filterType
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
        const rangeData = []
        data.range.forEach(value => {
          if (!rangeData.includes(value)) {
            rangeData.push(value)
          }
        })
        state.rangeData = rangeData
      })

    },
    newFilter({ dispatch }, data) {
      console.log(data)
      dispatch('editFilter', data)
    },
    saveFilter({ commit, dispatch }) {
      commit('SAVE_FILTER')
      return dispatch('saveChart')
    }
  },
}