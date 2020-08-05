<template>
  <div class="field-line">
    <div class="title">{{title}}</div>
    <div :key="index" class="field-block" v-for="(f,index) in arrData">
      <a-dropdown>
        <a-menu @click="e => handleMenuClick(e, f)" slot="overlay">
          <a-sub-menu key="field" title="选择字段">
            <a-menu-item :key="field.name" v-for="field in fields">{{field.title}}</a-menu-item>
          </a-sub-menu>
          <a-menu-item @click="setField(index)" key="3">设置字段</a-menu-item>
          <a-sub-menu key="aggregator" title="聚合函数" v-if="mode === 'y'">
            <a-menu-item :key="value" v-for="(label, value) in aggregatorFunc">{{label}}</a-menu-item>
          </a-sub-menu>
          <a-sub-menu key="sort" title="排序">
            <a-menu-item
              :key="sortMenu.name"
              @click="setSort(f, sortMenu.name)"
              v-for="sortMenu in sortMenus"
            >{{sortMenu.title}}</a-menu-item>
          </a-sub-menu>
        </a-menu>
        <a-button style="margin-left: 8px">
          <a-icon
            style="margin-right: 5px;"
            v-if="sqlConfig.sort && sqlConfig.sort.name === f.name"
            :type="`sort-${sqlConfig.sort.orderType}ending`"
          />
          {{f.finalAliasName}}
          <a-icon type="down" />
        </a-button>
      </a-dropdown>
      <a-button @click="removeField(index)" icon="close" style="margin-left: -1px; font-size:14px;"></a-button>
    </div>
    <a-modal
      :destroyOnClose="true"
      :visible="settingFieldVisible"
      :width="400"
      @cancel="() => {settingFieldVisible = false; form ={};}"
      @ok="finishSetField"
      title="修改字段"
    >
      <a-form-model-item label="字段别名">
        <a-input v-model="settingField.aliasName"></a-input>
      </a-form-model-item>
      <a-form-model-item label="字段描述">
        <a-input type="textarea" v-model="settingField.description"></a-input>
      </a-form-model-item>
    </a-modal>
    <a-button @click="addField" class="add-button" icon="plus"></a-button>
  </div>
</template>

<script>
const sortMenus = [
  {
    name: "default",
    title: "默认"
  },
  {
    name: "asc",
    title: "升序"
  },
  {
    name: "desc",
    title: "降序"
  }
];
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
  props: ["mode", "fields", "arrData", "sqlConfig"],
  data() {
    return {
      sortMenus,
      aggregatorFunc,
      settingField: {},
      settingFieldVisible: false,
      settingFieldIndex: ""
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
      this.saveChart();
    },
    handleMenuClick(e, field) {
      const order = e.keyPath[1];
      if (order === "sort") {
        return;
      }
      if (order === "field") {
        Object.assign(field, { ...this.fields.find(f => f.name === e.key) });
        if (this.mode === "y") {
          field.aggregator = field.type === "value" ? "SUM" : "COUNT";
        }
      }
      if (order === "aggregator") {
        field.aggregator = e.key;
      }
      this.saveChart();
    },
    setField(index) {
      this.settingFieldVisible = true;
      this.settingFieldIndex = index;
      this.settingField = this.arrData[index];
    },
    finishSetField() {
      Object.assign(this.arrData[this.settingFieldIndex], this.settingField);
      this.settingFieldVisible = false;
      this.saveChart();
    },
    setSort(field, orderType) {
      const sort =
        orderType === "default"
          ? null
          : {
              name: field.name,
              orderType
            };
      this.sqlConfig.sort = sort;
      this.saveChart();
    },
    saveChart() {
      this.$emit("save");
    }
  }
};
</script>

<style lang="less">
.field-line {
  height: 40px;
  display: flex;
  width: 100%;

  .title {
    font-size: 16px;
    line-height: 30px;
    margin-right: 10px;
  }

  .field-block {
    display: flex;
  }

  .add-button {
    margin-left: 10px;
    // flex-flow: ;
  }
}
</style>
