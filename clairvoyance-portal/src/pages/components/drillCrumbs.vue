<template>
  <div>
    <span style="cursor:pointer; color: #4876FF;" @click="onClick(0)">{{first}}</span>
    <span style="margin: 0 8px;">></span>
    <template v-for="(value, index) in arr">
      <span
        style="cursor:pointer; color: #4876FF;"
        :key="index"
        href="#"
        @click="onClick(index+1)"
      >{{value}}</span>
      <span :key="index+'__'" style="margin: 0 4px;" v-if="index < arr.length -1">></span>
    </template>
  </div>
</template>

<script>
export default {
  props: ['first','arr'],
  methods: {
    onClick(index) {
      this.$emit('click',index)
    }
  }
}
</script>

<style>
</style>