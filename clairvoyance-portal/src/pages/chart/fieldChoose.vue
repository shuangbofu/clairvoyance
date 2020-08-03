<template>
  <div class="field-line">
    <div class="title">{{title}}</div>
    <div v-for="(f,index) in arrData" :key="index" class="field-block">
      <a-dropdown>
        <a-menu slot="overlay" @click="e => handleMenuClick(e, f)">
          <a-sub-menu key="field" title="选择字段">
            <a-menu-item v-for="field in fields" :key="field.name">{{field.title}}</a-menu-item>
          </a-sub-menu>
          <a-menu-item key="3">设置字段</a-menu-item>
          <a-sub-menu key="aggregator" title="聚合函数" v-if="mode === 'y'">
            <a-menu-item v-for="(label, value) in aggregatorFunc" :key="value">{{label}}</a-menu-item>
          </a-sub-menu>
        </a-menu>
        <a-button style="margin-left: 8px">
          {{f.aggregator ? (f.title + ' ('+aggregatorFunc[f.aggregator]+')') : f.title}}
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
      <a-button icon="close" style="margin-left: -1px; font-size:14px;" @click="removeField(index)"></a-button>
    </div>
    <a-button class="add-button" icon="plus" @click="addField"></a-button>
  </div>
</template>

<script>
const aggregatorFunc = {
  SUM: "求和",
  AVG: "平均值",
  COUNT: "计数",
  DISTINCT_COUNT: "去重计数",
  MIN: "最小值",
  MAX: "最大值"
};
let idx = 1;
export default {
  props: ["mode", "fields", "arrData"],
  data() {
    return {
      aggregatorFunc
    };
  },
  computed: {
    title() {
      switch (this.mode) {
        case "x":
          return "维度";
        case "y":
          return "数值";
        default:
          return "";
      }
    }
  },
  methods: {
    addField() {
      this.arrData.push({
        name: "",
        title: "未选择",
        idx: idx++
      });
    },
    removeField(index) {
      this.arrData.splice(index, 1);
      this.$emit("save");
    },
    handleMenuClick(e, field) {
      const order = e.keyPath[1];
      if (order === "field") {
        Object.assign(field, { ...this.fields.find(f => f.name === e.key) });
        if (this.mode === "y") {
          field.aggregator = field.type === "value" ? "SUM" : "COUNT";
        }
      }
      if (order === "aggregator") {
        field.aggregator = e.key;
      }
      this.$emit("save");
    }
  }
};
</script>

<style lang="less">
.field-line {
  height: 40px;
  display: flex;
  border-bottom: 1px solid #e6e6e6;
  width: 100%;
  .title {
    font-size: 16px;
    line-height: 30px;
    margin-right: 10px;
  }
  .field-block {
    display: flex;
    // background: red;
    // margin-bottom: 10px;
    // margin-right: 10px;
    // color: #fff;
    // font-size: 14px;
    // padding: 10px;
  }
  .add-button {
    margin-left: 10px;
    // flex-flow: ;
  }
}
</style>