<template>
  <div>
    <a-table :pagination="false" size="small" :data-source="fieldsData" :columns="columns"></a-table>
  </div>
</template>

<script>
const columns = [
  {
    title: "行数",
    dataIndex: "seqNo"
  },
  {
    title: "原字段名",
    dataIndex: "name"
  },
  {
    title: "新字段名",
    dataIndex: "title"
  },
  {
    title: "字段类型",
    dataIndex: "type",
    scopedSlots: { customRender: "type" }
  },
  {
    title: "字段描述",
    dataIndex: "remarks"
  }
];
export default {
  props: ["data"],
  data() {
    return {
      columns
    };
  },
  computed: {
    fieldsData() {
      return this.data.filter(i=>i.fieldType === 'origin')
    }
  }
};
</script>

<style>
</style>