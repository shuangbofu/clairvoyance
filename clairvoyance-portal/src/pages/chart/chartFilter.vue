<template>
  <div class="all-filter-container">
    <draggable
      class="filter-container"
      v-model="filters"
      group="field"
      ghostClass="new-field"
      :sort="false"
    >
      筛选器
      <a-collapse>
        <a-collapse-panel v-for="(filter, index) in filters" :key="index" :header="filter.title">
          <a-icon class="icon" type="setting" slot="extra" v-on:click.stop="editFilter(filter)" />
          <div
            style="color: #666; font-weight: 500;margin-bottom:4px;"
          >{{!filter.included ? '不' :''}}包含以下选项</div>
          <div
            style="margin-bottom:2px;color:#777;"
            v-for="(value,index) in filter.range"
            :key="index"
          >{{ value }}</div>
        </a-collapse-panel>
      </a-collapse>
    </draggable>
    <draggable
      class="inner-filter-container"
      v-model="innerFilters"
      group="field"
      ghostClass="new-field"
      :sort="false"
    >
      图内筛选器
      <div style="margin-top: 10px;" />
      <div
        class="filter-item"
        v-for="innerFilter in innerFilters"
        :key="innerFilter.id"
      >{{innerFilter.title}}</div>
    </draggable>
    <a-modal
      title="编辑筛选项"
      :destroyOnClose="true"
      :visible="filterAddModalVisible"
      @ok="handleAddFilter"
      @cancel="addField = null; filterAddModalVisible = false;"
    >
      <add-filter v-if="filterAddModalVisible" />
    </a-modal>
  </div>
</template>

<script>
import addFilter from './addFilter'
import draggable from 'vuedraggable'
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      activeFilter: '',
      filterAddModalVisible: false,
    }
  },
  computed: {
        ...mapGetters('chart',[
      'fields'
    ]),
    filters: {
      get() {
        return this.$store.getters['chart/filters']
      },
      set(value) {
        const filter = this.checkFilter(value, this.filters)
        if(filter) {
          this.$store.dispatch('chart/newFilter',filter)
          this.filterAddModalVisible = true
        }
      }
    },
    innerFilters: {
      get() {
        return this.$store.getters['chart/innerFilters']
      },
      set(value) {
        const filter = this.checkFilter(value, this.innerFilters)
        if(filter) {
          if(filter.type !== 'text') {
            this.$message.error('类型不允许')
            return
          }
          this.$store.dispatch('chart/newInnerFilter', filter)
        }
      }
    }
  },
  components: {
    draggable,
    addFilter
  },
  methods: {
    handleAddFilter() {
      this.$store.dispatch('chart/saveFilter').then(() => {
        this.filterAddModalVisible = false;
      })
    },
    editFilter(filter) {
      this.$store.dispatch('chart/editFilter',filter)
      this.filterAddModalVisible = true
    },
    checkFilter(value, filters){ 
        const arr = value.filter(i=> !filters.map(i=>i.id).includes(i.id))
        if(arr.length == 0) {
          this.$message.error('已经添加过该字段的过滤条件')
          return;
        }
        return arr[0]
    }
  }
}
</script>

<style lang="less">
.all-filter-container {
  display: flex;
  flex-direction: column;
  margin-top: 10px;
  .filter-container {
    margin-bottom: 10px;
    padding-right: 15px;
    height: calc(70% - 12px);
    color: #666;
    font-size: 15px;
    .ant-collapse {
      margin-top: 10px;
      font-size: 11px;
      .ant-collapse-item > .ant-collapse-header {
        padding: 5px 10px;
        padding-left: 20px;
        .ant-collapse-arrow {
          left: 6px;
          font-size: 10px;
        }
      }
    }
    .icon {
      &:hover {
        color: #666;
      }
    }
  }
  .inner-filter-container {
    color: #666;
    font-size: 15px;
    height: calc(30% - 7.5px);
    .filter-item {
      margin-bottom: 6px;
      font-size: 12px;
      color: #777;
    }
  }
  .new-field {
    opacity: 0;
  }
}
</style>