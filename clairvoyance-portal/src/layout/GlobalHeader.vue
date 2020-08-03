<template>
  <div style="width: 100%;line-height: 55px; display: flex;">
    <!-- <a-icon
      class="trigger"
      :type="collapsed ? 'menu-unfold' : 'menu-fold'"
      @click="toggleCollpased"
    />-->
    <router-link to="/">
      <img style="height: 54px; padding-top: 2px; margin: 0 30px;" src="@/assets/logo.png" />
    </router-link>
    <i-menu
      style="height: 64px; line-height: 64px;"
      theme="light"
      :collapsed="collapsed"
      :menu-data="menuData"
      @select="onSelect"
    />
  </div>
</template>

<script>
import IMenu from "./menu";
let menuData = [];
export default {
  props: ["collapsed"],
  data() {
    return {
      menuData
    };
  },
  components: {
    IMenu
  },
  computed: {},
  beforeCreate() {
    menuData = this.$router.options.routes.find(item => item.path === "/")
      .children;
  },
  methods: {
    toggleCollpased() {
      this.$emit("toggleCollpased");
    },
    onSelect() {}
  }
};
</script>

<style lang="less">
.trigger {
  font-size: 20px;
  line-height: 55px;
  padding: 0 16px;
  cursor: pointer;
  transition: color 0.3s;
  &:hover {
    color: #ff6347;
  }
}
</style>