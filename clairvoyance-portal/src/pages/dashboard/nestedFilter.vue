<template>
  <draggable
    class="item-container"
    tag="div"
    :group="{name: 'filter', clone:false}"
    :list="list"
    :value="value"
    @input="emitter"
  >
    <div class="item-group" v-for="filter in realValue" :key="filter.name">
      <div
        :class="['item',activeFilterId===filter.id ? 'active' : '']"
        @click="onClick(filter.id)"
      >{{filter.name}}</div>
      <nested-filter class="item-sub" @click="onClick" v-model="filter.children" />
    </div>
  </draggable>
</template>

<script>
import draggable from 'vuedraggable'
import { mapGetters } from 'vuex'
export default {
  props: {
    value: {
      required: false,
      type: Array,
      default: null
    },
    list: {
      required: false,
      type: Array,
      default: null
    }
  },
  components: {
    draggable
  },
  name: 'nested-filter',
  computed: {
    ...mapGetters('dashboard',['activeFilterId']),
    realValue() {
      return this.value ? this.value : this.list;
    }
  },
  methods: {
    emitter(value) {
      this.$emit('input',value)
    },
    onClick(v) {
      this.$store.dispatch('dashboard/changeActiveFilter',v)
    }
  }
}
</script>

<style lang="less">
.item-container {
  max-width: 20rem;
  margin: 0;
}

.item {
  padding: 0.3rem;
  font-size: 13px;
  color: #888;
  border: 1px solid #e6e6e6;
  cursor: pointer;
  &.active {
    color: #4876ff;
    border-color: #4876ff;
    background-color: #fff;
  }
}
.item-sub {
  margin: 0 0 0 1rem;
}
</style>