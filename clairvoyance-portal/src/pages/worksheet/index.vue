<template>
  <div>
    <catalogue item="workSheet" @choose="chooseWorkSheet" ref="catRef">
      <template slot="right-container">
        <work-sheet-detail v-if="workSheet" :data="workSheet" />
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
      workSheet: null
    };
  },
  components: {
    Catalogue,
    WorkSheetDetail
  },
  methods: {
    chooseWorkSheet(id) {
      this.$axios.get(`/workSheet?workSheetId=${id}`).then(data => {
        this.workSheet = data;
        this.$refs.catRef.loaded();
      });
    }
  }
};
</script>

<style>
</style>