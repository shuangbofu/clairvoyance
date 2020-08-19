<template>
  <div v-if="sheetData" class="preview-container">
    <div class="top">
      显示最新
      <span class="value">{{sheetData.length}}</span>条数据，总共
      <span class="value">{{total}}</span>条数据
    </div>
    <div class="data-container">
      <a-table
        table-layout="auto"
        :style="{ 'word-break': 'break-all' }"
        size="small"
        rowKey="index"
        :pagination="false"
        :data-source="sheetData"
        :columns="columns"
        :scroll="{x: 1600, y: 500}"
      ></a-table>
    </div>
  </div>
</template>

<script>
export default {
  props: ["workSheet"],
  data() {
    return {
      previewCondition: {
        fields: [],
        sql: "",
        wheres: [],
        whereType: "condition",
        linker: "and",
        order: {
          name: "",
          desc: true
        }
      },
      sheetData: null,
      total: 0
      // tableWidth: 0
    };
  },
  computed: {
    columns() {
      const data = this.sheetData;
      let map = {};
      data.forEach(line => {
        for (let key in line) {
          const length = line[key].toString().length || 0;
          const now = map[key];
          if (now === undefined || now < length) {
            map[key] = length;
          }
        }
      });
      this.workSheet.fields.forEach(f => {
        f.width = map[f.name] * 10 + 20;
      });
      return this.workSheet.fields
      .filter(i=>i.fieldType === 'origin')
      .map(i => {
        let c = {
          dataIndex: i.name,
          title: i.title,
          width: i.width
        };
        // if (index !== this.workSheet.fields.length - 1) {
        //   c.width = i.width;
        // }
        return c;
      });
    }
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.$axios
        .post(`/workSheet/preview/${this.workSheet.workSheetId}`, this.previewCondition,{
          timeout: 10000000
        })
        .then(data => {
          this.sheetData = data.data;
          this.total = data.total
          // let tableWidth = 0;
          // let map = {};
          // data.data.forEach(line => {
          //   for (let key in line) {
          //     const length = line[key].toString().length || 0;
          //     const now = map[key];
          //     if (now === undefined || now < length) {
          //       map[key] = length;
          //       tableWidth = tableWidth + (length - (now || 0)) * 10 + 20;
          //     }
          //   }
          // });
          // this.tableWidth = tableWidth;
        });
    }
  }
};
</script>

<style lang="less">
.preview-container {
  .top {
    font-size: 13px;
    color: #666;
    margin-bottom: 10px;
    .value {
      color: #4876FF;
      margin: 0 4px;
    }
  }
  .data-continer {
    .ant-table td {
      white-space: nowrap;
    }
  }
  td,
  th {
    white-space: nowrap;
  }
}
</style>