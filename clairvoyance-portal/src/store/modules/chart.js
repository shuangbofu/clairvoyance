import axios from "@/utils/request.js";
import Vue from 'vue'

export default {
  namespaced: true,
  state: {
    drillLevel: 0,
    chartId: '',
    chart: {},

    workSheet: {},
    chartData: [],

    // 编辑筛选项
    editingFilter: null,
    rangeData: [],
    drillValues: []
  },
  getters: {
    workSheet: state => state.workSheet,
    fields: state => state.workSheet.fields,

    chartData: state => state.chartData,
    chart: state => state.chart,
    layers: (state, getters) => getters.sqlConfig.layers,
    chartLayer: (state, getters) => getters.layers[state.drillLevel],

    // 下钻字段，图层中使用
    drillLevel: state => state.drillLevel,
    drillFields: (state, getters) => getters.sqlConfig.drillFields,
    drillField: (state, getters) => getters.drillFields[state.drillLevel],

    drillValues: state => state.drillValues,

    sqlConfig: state => state.chart.sqlConfig,
    filters: (state, getters) => getters.sqlConfig.filters,
    innerFilters: (state, getters) => getters.sqlConfig.innerFilters,

    editingFilter: state => state.editingFilter,
    rangeData: state => state.rangeData,
  },
  mutations: {
    CLEAR_CHART(state) {
      state.chartData = []
      state.workSheet = {}
      state.drillLevel = 0
      state.drillValues = []
    },
    INIT_CHART(state, data) {
      state.chart = data
      state.chartData = []
    },
    INIT_CHART_DATA(state, data) {
      state.chartData = data
    },
  },
  actions: {
    initChart({ dispatch, commit }, chartId) {
      axios.get(`/chart?chartId=${chartId}`).then(data => {
        commit('INIT_CHART', data)
        dispatch('initWorkSheet', data.workSheetId)
        return dispatch('fetchChartData', chartId)
      });
    },
    fetchChartData({ state, commit }, chartId) {
      axios
        .post(`/chart/data/${chartId}`, {
          level: state.drillLevel,
          values: state.drillValues
        }, {
          timeout: 10000000
        })
        .then(data => {
          commit('INIT_CHART_DATA', data)
        });
    },
    initWorkSheet({ state }, workSheetId) {
      if (state.workSheet.title) {
        return
      }
      axios.get(`/workSheet?id=${workSheetId}`).then(data => {
        state.workSheet = data
      });
    },
    saveChart({ dispatch, state }, notInit) {
      axios.post("/chart", state.chart).then(b => {
        if (b && !notInit) {
          dispatch('initChart', state.chart.chartId);
        }
      });
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
    saveFilter({ getters, dispatch }) {
      const filters = getters.filters
      const editingFilter = getters.editingFilter
      const target = filters.find(i => i.id === editingFilter.id)
      if (target) {
        Object.assign(target, editingFilter)
      } else {
        filters.push(editingFilter)
      }
      return dispatch('saveChart')
    },
    newInnerFilter({ getters, dispatch }, data) {
      getters.innerFilters.push({
        filterType: 'inner',
        range: [],
        ...data
      })
      return dispatch('saveChart')
    },
    initInnerFilterRange({ getters }, id) {
      const targ = getters.innerFilters.find(i => i.id === id)
      const filter = getters.filters.find(i => i.id === id)
      if (filter && filter.included) {
        Vue.set(targ, 'rangeData', filter.range)
        return
      }
      axios.post('/workSheet/range', {
        workSheetId: getters.workSheet.workSheetId,
        fieldId: id
      }, { timeout: 10000000 }).then(data => {
        let rangeData = disinct(data.range)
        if (filter && !filter.included) {
          rangeData = rangeData.filter(i => !filter.range.includes(i))
        }
        Vue.set(targ, 'rangeData', rangeData)
      })
    },
    setInnerFilterRnage({ getters, dispatch }, { id, value }) {
      getters.innerFilters.find(i => i.id === id).range = value ? [value] : []
      return dispatch('saveChart')
    },
    removeFilter({ getters, dispatch }, filter) {
      const filters = getters.filters
      const index = filters.findIndex(i => i.id === filter.id)
      if (index !== -1) {
        filters.splice(index, 1)
        return dispatch('saveChart')
      }
    },
    removeInnerFilter({ getters, dispatch }, filter) {
      const filters = getters.innerFilters
      const index = filters.findIndex(i => i.id === filter.id)
      if (index !== -1) {
        filters.splice(index, 1)
        return dispatch('saveChart')
      }
    },
    addField({ getters, dispatch }, { name, index, data }) {
      if (name === 'drill') {
        getters.drillFields.push(data)
        console.log('add', {
          x: [{ id: data.id }],
          y: getters.chartLayer.y,
          sort: getters.chartLayer.sort,
          chartType: getters.chartLayer.chartType
        })
        if (getters.drillFields.length > 1) {
          console.log(data)
          getters.layers.push({
            x: [{ id: data.id }],
            y: getters.chartLayer.y,
            sort: getters.chartLayer.sort,
            chartType: getters.chartLayer.chartType
          })
        }
      } else {
        getters.chartLayer[name].splice(index, 0, data)
      }
      return dispatch('saveChart')
    },
    moveField({ dispatch, getters }, { newIndex, oldIndex, name }) {
      arraymove(getters.chartLayer[name], oldIndex, newIndex)
      dispatch('saveChart')
    },
    removeDrillField({ dispatch, getters, state }, index) {
      getters.drillFields.splice(index, 1)
      getters.layers.splice(index, 1)
      if (getters.drillFields.length <= 1) {
        getters.sqlConfig.drillFields = []
        getters.sqlConfig.layers.splice(1, getters.sqlConfig.layers.length - 1)
      }
      if (getters.drillLevel >= getters.drillFields.length && getters.drillLevel !== 0) {
        state.drillLevel = getters.drillFields.length - 1
      }
      return dispatch('saveChart')
    },
    // 下钻
    drill({ getters, state, dispatch }, value) {
      if (state.drillLevel + 1 < getters.drillFields.length) {
        state.drillValues.push(value)
        state.drillLevel += 1
        return dispatch('fetchChartData', state.chart.chartId)
      }
    },
    // 上卷
    rollUp({ state, dispatch }, level) {
      if (level >= state.drillLevel) {
        return;
      }
      state.drillValues = state.drillValues.slice(0, level)
      state.chartData = []
      state.drillLevel = level
      dispatch('fetchChartData', state.chart.chartId)
    },
    openDrill({ getters, dispatch }, id) {
      getters.drillFields.push({ id })
      dispatch('saveChart')
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

function arraymove(arr, fromIndex, toIndex) {
  let element = arr[fromIndex];
  arr.splice(fromIndex, 1);
  arr.splice(toIndex, 0, element);
}