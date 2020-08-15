<template>
  <div>
    <draggable
      class="field-line"
      ghostClass="new-field"
      group="field"
      v-model="arrData"
      @change="onChange"
      :sort="this.mode !== 'drill'"
      draggable=".field-block"
    >
      <div slot="header">
        <div class="title">{{title}}</div>
      </div>
      <div :key="index" class="field-block" v-for="(f,index) in arrData">
        <div
          @click="rollUp(index)"
          v-if="mode ==='drill'"
          :class="['drill-field', drillLevel === index ? 'in-level':'',drillLevel < index ? 'disabled' : '']"
        >
          <div class="field-block-main">
            {{f.realAliasName}}
            <a-icon
              @click="removeDrillField(index)"
              class="close-icon"
              style="margin-left: 6px;"
              theme="filled"
              type="close-circle"
            />
          </div>
          <a-icon class="arrow-right" type="right" v-if="index < arrData.length -1" />
        </div>
        <a-dropdown v-else>
          <a-menu @click="e => handleMenuClick(e, f)" slot="overlay">
            <a-menu-item
              v-if="mode === 'x' && index === 0 && drillFields.length === 0"
              @click="openDrill(f.id)"
              key="drill"
            >下钻</a-menu-item>
            <a-menu-item
              disabled
              v-if="f.aliasName && f.aliasName !== ''"
            >{{mode === 'y' ? f.aggregatorAlias : f.name}}</a-menu-item>
            <a-menu-item @click="setField(index)" key="3">设置字段</a-menu-item>
            <a-menu-item @click="setFilter" key="4">结果筛选器</a-menu-item>
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
          <div
            class="field-block-main"
            :class="[drillField && index === 0 && mode === 'x' ? 'drill': '']"
          >
            <a-icon type="down" />
            <a-icon
              :type="`sort-${chartLayer.sort.orderType}ending`"
              style="margin-right: 5px;"
              v-if="chartLayer.sort && chartLayer.sort.id === f.id && chartLayer.sort.axis === mode"
            />
            {{f.realAliasName}}
            <a-icon
              v-if="!drillField || drillField.id !== f.id"
              @click="removeField(index)"
              class="close-icon"
              style="margin-left: 6px;"
              theme="filled"
              type="close-circle"
            />
          </div>
        </a-dropdown>
      </div>
    </draggable>
    <a-modal
      :destroyOnClose="true"
      :title="`修改字段 ${settingField.name}`"
      :visible="settingFieldVisible"
      :width="400"
      @cancel="() => {settingFieldVisible = false; form ={};}"
      @ok="finishSetField"
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
    import {mapGetters} from 'vuex'

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
            ...mapGetters('chart', [
                'fields',
                'chartLayer',
                'drillFields',
                'drillField',
                'drillLevel'
            ]),
            arrData: {
                get() {
                    if(this.mode ==='drill') {
                        return this.drillFields
                    }
                    return this.chartLayer[this.mode]
                },set() {}
            },
            title() {
                switch (this.mode) {
                    case "x":
                        return "维度";
                    case "y":
                        return "数值";
                    case "drill":
                        return "图层";
                    default:
                        return "";
                }
            }
        },
        methods: {
            removeField(index) {
                this.arrData.splice(index, 1);
                this.saveChart();
            },
            handleMenuClick(e, field) {
                const order = e.keyPath[1];
                if (order === "sort" || !order) {
                    return;
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
                const aliasName = this.settingField.aliasName
                const tar = this.arrData.filter(i => i.id !== this.settingField.id).map(i => i.aliasName).find(i => i === aliasName)
                if (tar) {
                    this.$message.error('别名重复')
                    return
                }
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
                this.chartLayer.sort = sort;
                this.saveChart();
            },
            saveChart() {
                this.$store.dispatch('chart/saveChart')
            },
            removeDrillField(index) {
                this.$store.dispatch('chart/removeDrillField', index)
            },
            onChange(e) {
                if(e.added) {
                    const element = e.added.element
                    let index = e.added.newIndex -1
                    if(index === -1) {
                        index = 0
                    }
                    // x、y、下钻（图层）
                    this.$store.dispatch('chart/addField', {
                        data:element,
                        name: this.mode,
                        index
                    })
                } else if(e.moved) {
                  // console.log(e.moved)
                  const newIndex = e.moved.newIndex
                  const oldIndex = e.moved.oldIndex
                  this.$store.dispatch('chart/moveField',{
                    newIndex,oldIndex,
                    name: this.mode
                  })
                }
            },
            rollUp(index) {
              this.$store.dispatch('chart/rollUp',index)
            },
            openDrill(id) {
              this.$store.dispatch('chart/openDrill',id)
            },
            setFilter(index) {
                // 结果选择器
                console.log(index)
            }
        }
    };
</script>

<style lang="less">
.field-line {
  height: 40px;
  display: flex;
  flex-direction: row;
  box-sizing: border-box;
  width: 100%;
  border-bottom: 1px solid #e6e6e6;

  .title {
    color: #666;
    font-size: 14px;
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
    .drill-field {
      margin-bottom: 5px;
      display: flex;
      align-items: center;
      .arrow-right {
        font-size: 13px;
        margin-left: 6px;
        color: #888;
      }
      &.in-level {
        .field-block-main {
          border-color: #4876ff !important;
        }
      }
      &.disabled {
        .field-block-main {
          cursor: default;
        }
      }
      .field-block-main {
        background: transparent !important;
        border: 2px solid #e6e6e6 !important;
        line-height: 20px;
        &:hover {
          border-color: yellow;
        }
      }
    }

    .field-block-main {
      background: #fff;
      color: #666;
      padding: 4px 10px;
      height: 32px;
      font-size: 13px;
      line-height: 24px;
      border: 1px solid #e6e6e6;
      box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
      box-sizing: border-box;
      cursor: pointer;
      &.drill {
        border: 2px solid red !important;
        background: transparent;
        line-height: 20px;
      }

      .close-icon {
        opacity: 0;
      }

      &:hover {
        .close-icon {
          opacity: 1;
          color: #666;

          &:hover {
            color: #4876ff;
          }
        }
      }
    }
  }

  .add-button {
    margin-left: 10px;
  }
}
</style>
