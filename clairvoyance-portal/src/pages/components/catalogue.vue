<template>
  <div class="dashboard-container">
    <a-row type="flex" justify="center" align="top" :gutter="16">
      <a-col :xs="{span: 7}" :xl="{span: 5}" :xxl="{span: 5}">
        <div class="catalogue-container">
          <div style="display: flex; justify-content: space-between">
            <a-input placeholder="过滤" v-model="filterValue" icon="FilterOutlined">
              <a-icon type="filter" slot="suffix" />
            </a-input>
            <a-dropdown :trigger="['click']">
              <a-menu slot="overlay" @click="handleMenuClick">
                <a-menu-item key="文件夹">
                  <a-icon type="folder" />创建文件夹
                </a-menu-item>
                <a-menu-item key="仪表盘">
                  <a-icon type="dashboard" />创建仪表盘
                </a-menu-item>
              </a-menu>
              <div>
                <a-button icon="plus" style="margin-left: 5px;"></a-button>
              </div>
            </a-dropdown>
          </div>
          <div class="catalogue-tree">
            <a-tree
              v-if="treeData.length > 0"
              show-icon
              :tree-data="filteredTreeData"
              @expand="expandNode"
              @select="selectNode"
              :expandedKeys.sync="defaultExpandedKeys"
              :selectedKeys.sync="defaultSelectedKeys"
            >
              <template slot-scope="{expanded}" slot="folder">
                <a-icon
                  class="tree-icon"
                  theme="outlined"
                  :type="expanded ? 'folder-open': 'folder'"
                ></a-icon>
              </template>
              <a-icon class="tree-icon" theme="outlined" :type="treeIcon" :slot="item" />
              <template slot="title" slot-scope="data">
                <a-tooltip
                  v-if="!data.folder"
                  overlayClassName="tooltip"
                  placement="right"
                  :overlayStyle="{
                'background': '#fff'
              }"
                >
                  <template slot="title">
                    <template v-if="item === 'dashboard'">
                      <div>名称：{{data.title}}</div>
                      <div>备注：{{data.remarks}}</div>
                    </template>
                    <template v-if="item === 'workSheet'">
                      <div>名称：{{data.title}}</div>
                      <div>原名：{{data.tableName}}</div>
                      <div>描述：{{data.description}}</div>
                    </template>
                  </template>
                  {{data.title}}
                </a-tooltip>
                <template v-else>{{data.title}}</template>
              </template>
            </a-tree>
          </div>
        </div>
      </a-col>
      <a-col :xs="{span: 17}" :xl="{span: 19}" :xxl="{span: 19}">
        <div class="right-container">
          <a-spin class="spin" v-if="dashboardLoading" tip="仪表盘加载中……" />
          <!-- <chart-list v-else :dashboard="dashboard" /> -->
          <template v-else>
            <slot name="right-container"></slot>
          </template>
        </div>
      </a-col>
    </a-row>
    <a-modal
      :title="`新建${menuKey}`"
      :visible="visible"
      :confirm-loading="confirmLoading"
      :destroyOnClose="true"
      @ok="handleOk"
      @cancel="() => {visible = false; form = {parentId: ''};}"
    >
      <a-form-model
        v-model="form"
        v-bind="{
        labelCol: { span: 6 },
        wrapperCol: { span: 16 },
      }"
      >
        <a-form-model-item label="所属文件夹">
          <a-tree-select
            v-model="form.parentId"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="selectedTreeData"
            tree-default-expand-all
          ></a-tree-select>
        </a-form-model-item>
        <a-form-model-item :label="`${menuKey}名称`">
          <a-input v-model="form.name" />
        </a-form-model-item>
        <a-form-model-item v-if="menuKey == '仪表盘'" :label="`${menuKey}备注`">
          <a-input type="textarea" v-model="form.remarks" />
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
function setLocal(key, value) {
  localStorage.setItem(key, JSON.stringify(value));
}

function getLocal(key) {
  return JSON.parse(localStorage.getItem(key));
}

// function convert2Node(data) {
//   const res = {
//     title: data.name,
//     ...data,
//     key: "node_" + data.id,
//     value: data.id,
//     isLeaf: !data.folder || data.children.length === 0,
//     children: data.children.map(c => convert2Node(c)),
//     selectable: !data.folder
//   };
//   if (!data.folder) {
//     res.scopedSlots = { icon: this.item, title: "title" };
//   } else {
//     res.scopedSlots = { icon: "folder", title: "title" };
//   }
//   return res;
// }

function selectTreeNode(nodes) {
  return nodes
    .filter(i => i.folder)
    .map(i => {
      return {
        ...i,
        selectable: true,
        children: selectTreeNode(i.children)
      };
    });
}

function nodesAfterFilter(nodes, value) {
  let res = [];
  nodes.forEach(i => {
    if (i.isLeaf) {
      if (i.title.includes(value)) {
        res.push(i);
      }
    } else {
      const line = { ...i, children: nodesAfterFilter(i.children, value) };
      if (line.children.length > 0) {
        res.push(line);
      }
    }
  });
  return res;
}

export default {
  props: ["item"],
  data() {
    return {
      treeData: [],
      defaultExpandedKeys: [],
      defaultSelectedKeys: [],
      filterValue: "",
      visible: false,
      confirmLoading: false,
      menuKey: "",
      form: {
        parentId: ""
      },
      dashboard: {},
      dashboardLoading: true
    };
  },
  created() {
    this.init();
  },
  components: {},
  computed: {
    selectedKeysLocalKey() {
      return `${this.item}_selectedKeys`;
    },
    expandedKeysLocalKey() {
      return `${this.item}_expandedKeys`;
    },
    selectedRefIdKey() {
      return `${this.item}_ref_id`;
    },
    filteredTreeData() {
      return nodesAfterFilter(this.treeData, this.filterValue);
    },
    selectedTreeData() {
      return selectTreeNode(this.treeData);
    },
    treeIcon() {
      if (this.item === "workSheet") {
        return "table";
      }
      return this.item;
    }
  },
  methods: {
    init() {
      this.defaultSelectedKeys = getLocal(this.selectedKeysLocalKey);
      this.defaultExpandedKeys = getLocal(this.expandedKeysLocalKey);
      const selectedRefId = getLocal(this.selectedRefIdKey);
      this.$axios.get(`/${this.item}/catalogue`).then(data => {
        const newData = [
          {
            name: "根目录",
            id: 0,
            refId: 0,
            folder: true,
            children: data
          }
        ];
        const treeData = newData.map(d => this.convert2Node(d));
        this.treeData = treeData;
        if (selectedRefId != null) {
          this.chooseDashboard(selectedRefId);
        } else {
          this.dashboardLoading = false;
        }
      });
    },
    selectNode(selectedKeys, { node }) {
      // TODO 取消反选
      setLocal(this.selectedKeysLocalKey, selectedKeys);
      this.selectedKeys = selectedKeys;
      if (!node.dataRef.folder) {
        setLocal(this.selectedRefIdKey, node.dataRef.refId);
        if (!node.selected) {
          this.chooseDashboard(node.dataRef.refId);
        }
      }
    },
    expandNode(expandedKeys) {
      setLocal(this.expandedKeysLocalKey, expandedKeys);
    },
    handleMenuClick(e) {
      this.menuKey = e.key;
      this.visible = true;
    },
    chooseDashboard(id) {
      this.dashboardLoading = true;
      this.$emit("choose", id);
    },
    loaded() {
      this.dashboardLoading = false;
    },
    handleOk() {
      if (this.form.parentId === "") {
        this.$message.error("所属文件夹不能为空！");
        return;
      }
      let url = `/${this.item}`;
      if (this.menuKey === "文件夹") {
        url += "/folder";
      }
      this.confirmLoading = true;
      this.$axios
        .post(url, this.form)
        .then(value => {
          if (value) {
            this.confirmLoading = false;
            this.$message.success(`创建${this.menuKey}成功！`);
            this.visible = false;

            if (this.menuKey !== "文件夹") {
              setLocal(this.selectedKeysLocalKey, [`node_${value.nodeId}`]);
              setLocal(this.selectedRefIdKey, value.dashboardId);
            }
            this.defaultExpandedKeys.push(`node_${this.form.parentId}`);
            setLocal(this.expandedKeysLocalKey, this.defaultExpandedKeys);

            // this.addExandedkey(`node_${this.form.parentId}`);
            this.init();
          }
        })
        .catch(msg => {
          console.log(msg);
          this.confirmLoading = false;
        });
    },
    convert2Node(data) {
      const res = {
        title: data.name,
        ...data,
        key: "node_" + data.id,
        value: data.id,
        isLeaf: !data.folder || data.children.length === 0,
        children: data.children.map(c => this.convert2Node(c)),
        selectable: !data.folder
      };
      if (!data.folder) {
        res.scopedSlots = { icon: this.item, title: "title" };
      } else {
        res.scopedSlots = { icon: "folder", title: "title" };
      }
      return res;
    }
  }
};
</script>

<style lang="less">
.dashboard-container {
  background: #f6f6f6;
  .catalogue-container {
    width: 100%;
    padding: 10px;
    overflow: auto;
    height: calc(100vh - 94px);
    border: 1px solid #e6e6e6;
    background: #fff;
    box-shadow: 0 2px 4px rgba(0, 21, 41, 0.08);
    // box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.1), 0 16px 24px 0 rgba(0, 0, 0, 0.1);
    .tree-icon {
      color: #4876ff;
      margin-right: 5px;
    }
    .tooltip {
      // background: #fff;
      color: red;
    }
  }
  .right-container {
    // padding: 20px;
    // border: 1px solid #e6e6e6;
    width: 100%;
    height: calc(100vh - 95px);
    // background: #fff;
    overflow: auto;
    .spin {
      text-align: center;
      margin: 100px 0;
      width: 100%;
    }
  }
}
</style>
