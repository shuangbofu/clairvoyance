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
        <chart-item
          :ref="`chartItem_${item.i}`"
          :globalFilterParams="globalFilterParams"
          :chart="charts.find(i=>i.chartId=== item.i)"
        />
      </grid-item>
    </grid-layout>
  </div>
</template>

<script>
import ChartItem from "./chartItem";
import VueGridLayout from 'vue-grid-layout';
export default {
  props: ['layouts','charts','globalFilterParams'],
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
      Object.values(this.$refs).forEach(refs => {
        const ref = refs[0]
        if(ref) {
          ref.resize()
        }
      })
    },
    refresh(chartIds) {
      chartIds.forEach(i=> {
        const ref = this.$refs[`chartItem_${i}`][0]
        if(ref) {
          ref.fetchData()
        }
      })
    }
  }
}
</script>

<style>
</style>