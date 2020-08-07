import axios from "@/utils/request.js";
export default {
  namespaced: true,
  state: {
    chartId: '',
    chart: {},
    workSheet: {},
    chartData: []
  },
  getters: {
    sqlConfig: state => state.chart.sqlConfig,
    chart: state => state.chart,
    chartData: state => state.chartData,
    workSheet: state => state.workSheet,
    fields: state => state.workSheet.fields,
    filters: (state, getters) => getters.sqlConfig.filters
  },
  mutations: {
    INIT_CHART(state, data) {
      state.chart = data
      state.workSheet = {}
      state.chartData = []
    },
    INIT_CHART_DATA(state, data) {
      state.chartData = data
    },
    INIT_WORKSHEET(state, data) {
      state.workSheet = data
    },
    CHOOSE_FIELD(state, { mode, data }) {
      state.chart.sqlConfig[mode] = data
    }
  },
  actions: {
    initChart({ dispatch, commit }, chartId) {
      axios.get(`/chart?chartId=${chartId}`).then(data => {
        commit('INIT_CHART', data)
        dispatch('initWorkSheet', data.workSheetId)
        return dispatch('fetchChartData', chartId).then(() => {

        })
      });
    },
    fetchChartData({ commit }, chartId) {
      axios
        .get(`/chart/data?chartId=${chartId}`, {
          timeout: 100000
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
    chooseField({ dispatch, commit }, data) {
      commit('CHOOSE_FIELD', data)
      dispatch('saveChart')
    }
  },
}