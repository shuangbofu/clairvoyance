import axios from "@/utils/request.js";
export default {
  namespaced: true,
  state: {
    dashboard: null,
    filterParams: {},

    activeFilterId: 0,
    // 打开全局过滤器时的工作表
    workSheets: [],
    fieldRange: [],

  },
  getters: {
    dashboard: state => state.dashboard,
    globalFilters: state => state.dashboard.globalFilters,
    flatFilters: (state, getters) => {
      const arr = []
      flatFilterDeep(getters.globalFilters, arr)
      return arr
    },
    charts: state => state.dashboard.charts,
    activeFilterId: state => state.activeFilterId,
    activeFilter: (state, getters) => getters.flatFilters.find(i => i.id === state.activeFilterId),
    workSheets: state => state.workSheets,
    fieldRange: state => state.fieldRange,
    filterParams: state => state.filterParams,
    globalFilterParams: state => {
      const filterParams = state.filterParams
      return Object.keys(filterParams).map(i => Number(i))
        .filter(i => filterParams[i] != null && filterParams[i] !== undefined)
        .map(key => {
          return {
            dashboardFilterId: key,
            range: [filterParams[key]]
          }
        })
    }
  },
  mutations: {
  },
  actions: {
    initDashboard({ state }, id) {
      return axios.get(`/dashboard?dashboardId=${id}`).then(data => {
        const dashboard = data;
        // TODO 
        if (dashboard.layoutConfig.positions.length == 0) {
          dashboard.layoutConfig.positions = dashboard.charts.map(i => {
            return {
              w: 4, h: 3, x: 0, y: 0, i: i.chartId
            }
          })
        }
        const filters = dashboard.globalFilters
        if (filters.length > 0) {
          state.activeFilterId = filters[0].id
        }
        state.dashboard = dashboard
      });
    },
    changeActiveFilter({ state, dispatch }, id) {
      state.activeFilterId = id
      return dispatch('getRange')
    },
    updateFitlers({ state }, value) {
      state.dashboard.globalFilters = value
    },
    initWorkSheets({ state }) {
      return axios.get(`/dashboard/workSheet?dashboardId=${state.dashboard.dashboardId}`).then(data => {
        state.workSheets = data
      })
    },
    getRange({ state, getters }) {
      const map = getters.activeFilter.sheetFieldMap
      const reqForm = Object.keys(map).map(i => {
        return {
          workSheetId: i,
          fieldId: map[i]
        }
      })
      return axios.post('/workSheet/ranges', reqForm).then(data => {
        state.fieldRange = data.range
      })
    }
  },
}

function flatFilterDeep(filters, arr) {
  filters.forEach(filter => {
    arr.push(filter)
    flatFilterDeep(filter.children, arr)
  })
}