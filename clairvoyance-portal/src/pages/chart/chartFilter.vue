<template>
  <div class="all-filter-container">
    <div class="filter-container">
      筛选器
      <a-collapse v-model="activeFilter">
        <a-collapse-panel v-for="(filter, index) in filters" :key="index" :header="filter.title">
          <p>...</p>
        </a-collapse-panel>
      </a-collapse>
    </div>
    <div class="inner-filter-container">图内筛选器</div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      activeFilter: ''
    }
  },
  computed: {
        ...mapGetters('chart',[
      'fields',
      'filters',
    ]),
  }
}
</script>

<style lang="less">
.all-filter-container {
  display: flex;
  flex-direction: column;
}
</style>