<template>
  <div class="preview-container">
    <div class="data-container">
      <a-table
        table-layout="auto"
        :style="{ 'word-break': 'break-all' }"
        size="small"
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
      sheetData: []
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
      return this.workSheet.fields.map(i => {
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
        .post("/workSheet/preview", {
          workSheetId: this.workSheet.id,
          ...this.previewCondition
        },{
          timeout: 100000
        })
        .then(data => {
          this.sheetData = data.data;
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
  .data-continer {
    .ant-table td {
      white-space: nowrap;
    }
    // width: calc(100% - 500px);
    // overflow: auto;
  }
  td,
  th {
    white-space: nowrap;
  }
}
</style>