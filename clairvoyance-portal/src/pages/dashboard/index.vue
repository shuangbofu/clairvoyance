<template>
  <div>
    <catalogue item="dashboard" @choose="chooseDashboard" ref="catRef">
      <template slot="right-container">
        <chart-list
          @refresh="chooseDashboard(dashboard.dashboardId)"
          v-if="dashboard"
          :dashboard="dashboard"
        />
      </template>
    </catalogue>
  </div>
</template>

<script>
import Catalogue from "../components/catalogue";
import ChartList from "./chartList";
export default {
  data() {
    return {
      dashboard: null
    };
  },
  components: {
    Catalogue,
    ChartList
  },
  methods: {
    chooseDashboard(id) {
      this.$axios.get(`/dashboard?id=${id}`).then(data => {
        this.dashboard = data;
        this.$refs.catRef.loaded();
      });
    }
  }
};
</script>

<style>
</style>