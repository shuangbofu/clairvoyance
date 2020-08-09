<template>
  <div class="field-list">
    <a-tabs size="small" style="margin-top: 10px;" default-active-key="1">
      <a-tab-pane key="1" tab="字段">
        <draggable
          class="list"
          v-model="fields"
          :group="{ name: 'field', pull: 'clone', put: false }"
          :sort="false"
        >
          <div class="field-item" v-for="field in fields" :key="field.id">
            <a-icon style="color: #4876ff" v-if="field.type === 'value'" type="number" />
            <span
              style="margin: 0 3px; color: #4876ff; font-size: 15x;font-weight: 500;"
              v-else-if="field.type === 'text'"
              type="text"
            >T</span>
            <span style="margin-left: 6px;">{{field.title}}</span>
          </div>
        </draggable>
      </a-tab-pane>
      <a-tab-pane key="2" tab="参数"></a-tab-pane>
      <a-dropdown slot="tabBarExtraContent" :trigger="['click']">
        <a-button icon="plus" type="link" size="small"></a-button>
        <a-menu slot="overlay">
          <a-menu-item>
            <a href="javascript:;">添加计算字段</a>
          </a-menu-item>
          <a-menu-item>
            <a href="javascript:;">添加分组字段</a>
          </a-menu-item>
          <a-menu-item>
            <a href="javascript:;" disabled>添加参数</a>
          </a-menu-item>
        </a-menu>
      </a-dropdown>
    </a-tabs>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
export default {
  computed: {
    fields() {
      return this.$store.getters['chart/fields']
    }
  },
  components: {
    draggable
  }
}
</script>

<style lang="less">
.field-list {
  padding: 0 20px;
  height: 100%;
  overflow: auto;
  .field-item {
    padding: 10px;
    cursor: pointer;
    font-size: 14px;
  }
  .ant-tabs-nav .ant-tabs-tab {
    margin-right: 10px;
  }
}
</style>