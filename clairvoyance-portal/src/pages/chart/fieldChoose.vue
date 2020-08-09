<template>
  <div>
    <draggable
      class="field-line"
      v-model="arrData"
      group="field"
      ghostClass="new-field"
      :sort="false"
    >
      <div class="title">{{title}}</div>
      <div :key="index" class="field-block" v-for="(f,index) in arrData">
        <a-dropdown>
          <a-menu @click="e => handleMenuClick(e, f)" slot="overlay">
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
          <div class="field-block-main">
            <a-icon type="down" />
            <a-icon
              style="margin-right: 5px;"
              v-if="sqlConfig.sort && sqlConfig.sort.id === f.id && sqlConfig.sort.axis === mode"
              :type="`sort-${sqlConfig.sort.orderType}ending`"
            />
            {{f.finalAliasName}}
            <a-icon
              @click="removeField(index)"
              class="close-icon"
              theme="filled"
              style="margin-left: 6px;"
              type="close-circle"
            />
          </div>
        </a-dropdown>
      </div>
    </draggable>
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
      <a-form-model-item label="单位" v-if="mode==='y'">
        <a-input v-model="settingField.unit"></a-input>
      </a-form-model-item>
      <a-form-model-item label="字段描述">
        <a-input type="textarea" v-model="settingField.description"></a-input>
      </a-form-model-item>
    </a-modal>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import { mapGetters } from 'vuex'
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
  props: ["mode"],
  data() {
    return {
      sortMenus,
      aggregatorFunc,
      settingField: {},
      settingFieldVisible: false,
      settingFieldIndex: ""
    };
  },
  components: {
    draggable
  },
  computed: {
    ...mapGetters('chart',[
      'fields',
      'sqlConfig',
    ]),
    arrData: {
      get() {
        return this.sqlConfig[this.mode]
      },
      set(value) {
        this.$store.dispatch('chart/chooseField', {
          mode: this.mode,
          data: value
        })
      }
    },
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
      // const field = this.arrData[index]
      // if(field.name === this.sqlConfig.sort.name) {
      //   this.setField(field, 'default')
      // }
      this.arrData.splice(index, 1);
      this.saveChart();
    },
    handleMenuClick(e, field) {
      const order = e.keyPath[1];
      if (order === "sort" || !order) {
        return;
      }
      // if (order === "field") {
      //   Object.assign(field, { ...this.fields.find(f => f.name === e.key) });
      //   field.aliasName = ''
      //   if (this.mode === "y") {
      //     field.aggregator = field.type === "value" ? "SUM" : "COUNT";
      //   }
      // }
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
              id: field.id,
              axis: this.mode,
              orderType
            };
      this.sqlConfig.sort = sort;
      this.saveChart();
    },
    saveChart() {
      this.$store.dispatch('chart/saveChart')
    }
  }
};
</script>

<style lang="less">
.field-line {
  height: 44px;
  display: flex;
  flex-direction: row;
  box-sizing: border-box;
  width: 100%;
  border-bottom: 1px solid #e6e6e6;
  .title {
    color: #666;
    font-size: 15px;
    line-height: 32px;
    margin-right: 26px;
  }
  .new-field {
    opacity: 0;
    margin-right: 10px;
    // background: #fff;
    // color: #666;
    padding: 4px 10px;
    height: 36px;
    font-size: 14px;
    line-height: 28px;
    border: 1px solid #e6e6e6;
  }

  .field-block {
    display: flex;
    margin-right: 10px;
    &:first-child {
      margin-left: 10px;
      background: red;
    }
    .field-block-main {
      background: #fff;
      color: #666;
      padding: 4px 10px;
      height: 36px;
      font-size: 14px;
      line-height: 28px;
      border: 1px solid #e6e6e6;
      cursor: pointer;
      .close-icon {
        opacity: 0;
      }
      &:hover {
        .close-icon {
          opacity: 1;
          color: #666;
          font-size: 14px;
          &:hover {
            color: #4876ff;
          }
        }
      }
    }
  }

  .add-button {
    margin-left: 10px;
    // flex-flow: ;
  }
}
</style>
