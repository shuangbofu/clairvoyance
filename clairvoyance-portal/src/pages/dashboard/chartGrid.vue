<template>
  <div>
    <grid-layout
      :layout.sync="layouts"
      :col-num="12"
      :row-height="30"
      :is-draggable="true"
      :is-resizable="true"
      :is-mirrored="false"
      :vertical-compact="true"
      :margin="[5, 5]"
      :use-css-transforms="true"
      @layout-updated="layoutUpdatedEvent"
    >
      <grid-item
        v-for="item in layouts"
        :x="item.x"
        :y="item.y"
        :w="item.w"
        :h="item.h"
        :i="item.i"
        :key="item.i.id"
      >
        <chart-item ref="chartItem" :chart="charts.find(i=>i.chartId=== item.i)" />
      </grid-item>
    </grid-layout>
  </div>
</template>

<script>
import ChartItem from "./chartItem";
import VueGridLayout from 'vue-grid-layout';
export default {
  props: ['layouts','charts'],
  data() {
    return {
    }
  },
  computed: {

  },
  components: {
    GridLayout: VueGridLayout.GridLayout,
    GridItem: VueGridLayout.GridItem,
    ChartItem,
  },
  methods: {
    layoutUpdatedEvent(newLayout) {
      console.log(newLayout)
      this.$emit('update',newLayout)
      this.$refs.chartItem[0].resize()
    }
  }
}
</script>

<style>
</style>