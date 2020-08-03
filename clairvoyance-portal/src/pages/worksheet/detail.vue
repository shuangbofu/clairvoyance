<template>
  <div class="work-sheet-detail-container">
    <div class="work-sheet-header">
      <div class="title">
        {{data.title}}
        <div class="description">{{data.description}}</div>
      </div>
      <div class="button-list">
        <a-button icon="bar-chart">新建图表</a-button>
        <a-button icon="file-excel">上传数据</a-button>
        <a-button icon="share-alt">创建合表</a-button>
      </div>
    </div>
    <div class="tabs">
      <a-tabs default-active-key="2" @change="chooseTab">
        <a-tab-pane key="1" tab="数据预览">数据预览</a-tab-pane>
        <a-tab-pane key="2" tab="字段设置" force-render>
          <field-list :data="data.fields" />
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>
import FieldList from "./fieldList";
export default {
  props: ["data"],
  data() {
    return {
      tabKey: 2
    };
  },
  components: {
    FieldList
  },
  methods: {
    chooseTab() {}
  }
};
</script>

<style lang="less">
.work-sheet-detail-container {
  padding: 20px 30px;
  background: #fff;
  border: 1px solid #e6e6e6;
  .work-sheet-header {
    display: flex;
    justify-content: space-between;
    .title {
      font-size: 28px;
      color: #4876ff;
      .description {
        font-size: 14px;
        color: #777;
      }
    }
    button {
      margin-left: -1px;
    }
    .tabs {
      margin-top: 20px;
    }
  }
}
</style>