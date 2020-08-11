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
          <div
            :class="[field.fieldType !== 'origin' ? 'custom' : '']"
            class="field-item"
            v-for="field in fields"
            :key="field.id"
          >
            <a-icon class="icon" v-if="field.type === 'value'" type="number" />
            <span
              class="icon"
              style="margin: 0 3px; font-size: 15x;font-weight: 500;"
              v-else-if="field.type === 'text'"
              type="text"
            >T</span>
            <span style="margin-left: 6px;">{{field.title}}</span>
            <a-icon
              style="float:right;line-height: 24px;"
              type="setting"
              v-if="field.fieldType !== 'origin'"
            />
          </div>
        </draggable>
      </a-tab-pane>
      <a-tab-pane key="2" tab="参数"></a-tab-pane>
      <a-dropdown slot="tabBarExtraContent" :trigger="['click']">
        <a-button icon="plus" type="link" size="small"></a-button>
        <a-menu slot="overlay">
          <a-menu-item>
            <a href="javascript:;" @click="addField('computed')">添加计算字段</a>
          </a-menu-item>
          <a-menu-item>
            <a href="javascript:;" @click="addField('group')">添加分组字段</a>
          </a-menu-item>
          <a-menu-item>
            <a href="javascript:;" disabled>添加参数</a>
          </a-menu-item>
        </a-menu>
      </a-dropdown>
    </a-tabs>
    <a-modal
      :title="`添加${fieldType === 'group' ? '分组': '计算'}字段`"
      :destroyOnClose="true"
      :visible="fieldAddVisible"
      @ok="handleAddField"
      @cancel="fieldAddVisible = false;"
    >
      <group-field v-if="fieldType === 'group'" />
    </a-modal>
  </div>
</template>

<script>
import GroupField from './groupField'
import draggable from 'vuedraggable'
export default {
  data() {
    return {
      fieldAddVisible:false,
      fieldType: ''
    }
  },
  computed: {
    fields() {
      return this.$store.getters['chart/fields']
    }
  },
  components: {
    draggable,
    GroupField
  },
  methods: {
    handleAddField() {

    },
    addField(fieldType) {
      this.fieldAddVisible = true
      this.fieldType = fieldType
    }
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
    .icon {
      color: #4876ff;
    }
    &.custom {
      .icon {
        color: red;
      }
    }
  }
  .ant-tabs-nav .ant-tabs-tab {
    margin-right: 10px;
  }
}
</style>