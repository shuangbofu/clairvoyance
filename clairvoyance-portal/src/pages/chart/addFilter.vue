<template>
  <div class="add-filter-container">
    <a-radio-group
      style="margin-bottom: 10px;"
      v-model="filter.filterType"
      button-style="solid"
      @change="changeFilterType"
    >
      <template v-for="filterType in filterTypes">
        <a-radio-button
          :key="filterType.name"
          :value="filterType.name"
          v-if="!filterType.in || filterType.in.includes(field.type)"
        >{{filterType.label}}</a-radio-button>
      </template>
    </a-radio-group>
    <div class="exact-container" v-if="filter.filterType === 'exact'">
      <div style="margin-bottom: 20px;">
        <span
          style="font-weight: 500; color: #333; margin-right: 5px; padding: 10px;"
        >{{field.title}}</span>是否包含下列选项
        <a-switch v-model="filter.included" />
      </div>
      <a-select
        show-search
        :filter-option="filterOption"
        v-model="filter.range"
        mode="multiple"
        style="width: 100%;"
      >
        <a-select-option v-for="(value, index) in rangeData" :key="index" :value="value">{{value}}</a-select-option>
      </a-select>
    </div>
  </div>
</template>

<script>
const filterTypes = [{
  name: 'exact',
  label: '精确筛选',
  in: ['text']
},{
  name: 'condition',
  label: '条件筛选'
},{
  name: 'expression',
  label: '表达式'
}]
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      filterTypes,
    }
  },
  computed: {
        ...mapGetters('chart',[
      'editingFilter',
      'rangeData'
    ]),
    field() {
      return this.filter
    },
    filter() {
      return this.editingFilter
    },
  },
  created() {
  },
  methods: {
    changeFilterType() {
      if(this.rangeData==null) {
        this.$store.dispatch('chart/fetchRangeData')
      }
    },
    filterOption(input, option) {
      return (
        option.componentOptions.children[0].text
          .toLowerCase()
          .indexOf(input.toLowerCase()) >= 0
      );
    }
  }
}
</script>

<style lang="less">
.add-filter-container {
  .exact-container {
    padding: 10px 0;
  }
}
</style>