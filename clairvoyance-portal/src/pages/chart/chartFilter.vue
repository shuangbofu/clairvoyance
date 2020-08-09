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
    <div class="inner-filter-container">图内筛选器</div>
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
        const arr = value.filter(i=> !this.filters.map(i=>i.id).includes(i.id))
        if(arr.length == 0) {
          this.$message.error('已经添加过该字段的过滤条件')
          return;
        }
        this.$store.dispatch('chart/newFilter',arr[0])
        this.filterAddModalVisible = true
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
    height: 50%;
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
  }
  .new-field {
    opacity: 0;
  }
}
</style>