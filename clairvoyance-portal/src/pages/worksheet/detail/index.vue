<template>
  <div class="work-sheet-detail-container">
    <div class="work-sheet-header">
      <div class="title">
        {{data.title}}
        <div class="description">{{data.description || '该表没有描述，赶紧加上'}}</div>
      </div>
      <div class="button-list">
        <a-button icon="bar-chart">新建图表</a-button>
        <a-button icon="file-excel">上传数据</a-button>
        <a-button icon="share-alt">创建合表</a-button>
      </div>
    </div>
    <a-tabs class="tabs" default-active-key="1" @change="chooseTab">
      <a-tab-pane key="1" tab="数据预览">
        <preview :work-sheet="data" />
      </a-tab-pane>
      <a-tab-pane key="2" tab="字段设置" force-render>
        <field-list :data="data.fields" />
      </a-tab-pane>
    </a-tabs>
  </div>
</template>

<script>
import Preview from "../../components/preview";
import FieldList from "./fieldList";
export default {
  props: ["data"],
  data() {
    return {
      tabKey: 1
    };
  },
  components: {
    FieldList,
    Preview
  },
  methods: {
    chooseTab() {}
  }
};
</script>

<style lang="less">
.work-sheet-detail-container {
  padding: 20px;
  background: #fff;
  border: 1px solid #e6e6e6;
  .work-sheet-header {
    display: flex;
    justify-content: space-between;
    padding: 0 10px;
    .title {
      font-size: 28px;
      color: #4876FF;
      .description {
        font-size: 14px;
        color: #777;
      }
    }
    button {
      margin-left: -1px;
    }
  }
  .tabs {
    margin-top: 10px;
    padding: 0 0px;
  }
}
</style>