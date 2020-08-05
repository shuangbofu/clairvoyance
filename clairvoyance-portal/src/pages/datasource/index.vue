<template>
  <div class="datasource-container">
    <a-button @click="addDs">新增数据源</a-button>
    <div class="ds-list">
      <div v-for="ds in datasources" :key="ds.id" class="ds-item">
        <div class="title">{{ds.name}}</div>
        <div class="description">{{ds.description}}</div>
        <a-button size="small" @click="ping(ds.id)">连接测试</a-button>
        <a-button size="small" style="margin-left: 10px;" @click="showTables(ds.id,ds.type)">所有表</a-button>
        <a-button size="small" style="margin-left: 10px;" @click="importTable(ds.id)">导入</a-button>
        <div class="table-list">
          <div :style="{ borderBottom: '1px solid #E9E9E9' }">
            <a-checkbox
              :indeterminate="indeterminate"
              :checked="checkAll"
              @change="onCheckAllChange"
            >全选</a-checkbox>
          </div>
          <a-checkbox-group v-model="checkTables" :options="tables" @change="onChange" />
        </div>
      </div>
    </div>

    <a-modal
      :destroyOnClose="true"
      :visible="visible"
      :width="400"
      @cancel="() => {visible = false; form ={};}"
      @ok="handleOk"
      title="添加数据源"
    >
      <a-form-model>
        <a-form-model-item label="名称">
          <a-input v-model="form.name"></a-input>
        </a-form-model-item>
        <a-form-model-item label="数据源类型">
          <a-select v-model="form.type">
            <a-select-option
              v-for="dsType in dsTypeList"
              :key="dsType.name"
              :value="dsType.name"
            >{{dsType.title}}</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="连接url">
          <a-input-search
            enter-button="连接测试"
            v-model="form.jdbcUrl"
            :loading="pingLoading"
            @search="ping('')"
          ></a-input-search>
        </a-form-model-item>
        <a-form-model-item label="用户名">
          <a-input v-model="form.username"></a-input>
        </a-form-model-item>
        <a-form-model-item label="密码">
          <a-input type="password" v-model="form.password"></a-input>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>

<script>
const dsTypeList = [
  {
    name: "mysql",
    title: "MySql"
  },
  {
    name: "presto",
    title: "Presto"
  }
];
export default {
  data() {
    return {
      datasources: [],
      visible: false,
      datasourceId: "",
      form: {
        jdbcUrl: "",
        username: "",
        password: "",
        type: "",
        name: ""
      },
      dsTypeList,
      pingLoading: false,
      checkTables: [],
      tables: [],
      checkAll: false,
      indeterminate: false
    };
  },
  created() {
    this.initList();
  },
  methods: {
    initList() {
      this.$axios.get("/datasource/list").then(data => {
        this.datasources = data;
      });
    },
    addDs() {
      this.visible = true;
    },
    handleOk() {
      this.$axios
        .post("/datasource", this.form)
        .then(data => {
          if (data) {
            this.$message.success("添加成功！");
            this.visible = false;
            this.initList();
          }
        })
        .catch(() => {
          this.visible = false;
        });
    },
    ping(dsId) {
      this.pingLoading = true;
      this.$axios
        .post(`/datasource/ping?datasourceId=${dsId}`, this.form)
        .then(data => {
          if (data) {
            this.$message.success("测试成功！");
          } else {
            this.$message.error("测试失败！");
          }
          this.pingLoading = false;
        })
        .catch(() => {
          this.pingLoading = false;
        });
    },
    showTables(dsId) {
      this.$axios.get(`/datasource/tables?datasourceId=${dsId}`).then(data => {
        this.tables = data;
      });
    },
    onChange(checkedList) {
      this.indeterminate =
        !!checkedList.length && checkedList.length < this.tables.length;
      this.checkAll = checkedList.length === this.tables.length;
    },
    onCheckAllChange(e) {
      Object.assign(this, {
        checkTables: e.target.checked ? this.tables : [],
        indeterminate: false,
        checkAll: e.target.checked
      });
    },
    importTable(dsId, dsType) {
      this.$axios
        .post("/workSheet/import", {
          datasourceId: dsId,
          folderId: 0,
          tables: this.checkTables,
          allTables: this.checkAll,
          sheetType: dsType,
          newFolder: true
        })
        .then(data => {
          if (data) {
            this.$message.success("导入成功！");
          }
        });
    }
  }
};
</script>

<style lang="less">
.datasource-container {
  .ds-list {
    margin-top: 20px;
    display: flex;
    .ds-item {
      .title {
        font-size: 15px;
        padding: 10px;
      }
      .description {
        padding: 0 10px;
        margin-bottom: 10px;
        font-size: 14px;
        color: #888;
      }
      border: 1px solid #e6e6e6;
      background: #fff;
      padding: 10px;
      margin-right: 10px;
    }
    .table-list {
      margin-top: 10px;
      padding: 10px;
      border: 1px solid #e6e6e6;
    }
  }
}
</style>