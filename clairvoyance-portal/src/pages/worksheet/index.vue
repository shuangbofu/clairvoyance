<template>
  <div>
    <catalogue item="workSheet" @choose="chooseWorkSheet" ref="catRef">
      <template slot="right-container">
        <work-sheet-detail :data="workSheet" />
      </template>
    </catalogue>
  </div>
</template>

<script>
import Catalogue from "../components/catalogue";
import WorkSheetDetail from "./detail/index";
export default {
  data() {
    return {
      workSheet: {}
    };
  },
  components: {
    Catalogue,
    WorkSheetDetail
  },
  methods: {
    chooseWorkSheet(id) {
      this.$axios.get(`/workSheet?id=${id}`).then(data => {
        this.workSheet = data;
        this.$refs.catRef.loaded();
      });
    }
  }
};
</script>

<style>
</style>