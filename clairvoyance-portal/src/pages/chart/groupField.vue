<template>
  <div>
    <a-form-model>
      <a-form-model-item label="字段名称">
        <a-input v-model="form.name"></a-input>
      </a-form-model-item>
      <a-form-model-item label="分组字段">
        <a-select v-model="form.refId">
          <a-select-option v-for="field in fields" :key="field.id" :value="field.id">{{field.title}}</a-select-option>
        </a-select>分组方式：
        <a-select v-model="form.mode">
          <a-select v-for="mode in modes" :key="mode.name" :value="mode.name">{{mode.label}}</a-select>
        </a-select>
      </a-form-model-item>
      <a-form-model-item></a-form-model-item>
    </a-form-model>
  </div>
</template>

<script>
const modes = [{
  name: 'expression',
  label: '表达式'
}]
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      modes,
      form: {}
    }
  },
  computed: {
    ...mapGetters('chart',[
      'fields'])
  }
      
}
</script>

<style>
</style>