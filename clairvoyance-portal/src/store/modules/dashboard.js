import axios from "@/utils/request.js";
export default {
  namespaced: true,
  state: {
    dashboard: null,
    activeFilterId: 0
  },
  getters: {
    dashboard: state => state.dashboard,
    globalFilters: state => state.dashboard.globalFilters,
    charts: state => state.dashboard.charts,
    activeFilterId: state => state.activeFilterId,
    activeFilter: (state, getters) => {
      return findFilterDeep(getters.globalFilters, state.activeFilterId)
    }
  },
  mutations: {
    changeActiveFilter(state, id) {
      state.activeFilterId = id
    }
  },
  actions: {
    initDashboard({ state }, id) {
      return axios.get(`/dashboard?id=${id}`).then(data => {
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
    updateFitlers({ state }, value) {
      state.dashboard.globalFilters = value
    }
  },
}

function findFilterDeep(filters, id) {
  let res = null;
  filters.every(filter => {
    if (filter.id === id) {
      res = filter
    } else if (filter.children.length > 0) {
      res = findFilterDeep(filter.children, id)
      return res != null
    }
  })
  return res;
}