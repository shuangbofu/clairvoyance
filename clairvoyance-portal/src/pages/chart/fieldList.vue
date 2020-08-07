<template>
  <div class="field-list">
    <a-tabs size="small" style="margin-top: 10px;" default-active-key="1">
      <a-tab-pane key="1" tab="字段">
        <div class="field-origin-list">
          <div class="field-item" v-for="field in fields" :key="field.name">{{field.title}}</div>
        </div>
      </a-tab-pane>
      <a-tab-pane key="2" tab="参数"></a-tab-pane>
      <a-dropdown slot="tabBarExtraContent" :trigger="['click']">
        <a-button icon="plus" type="link" size="small"></a-button>
        <a-menu slot="overlay">
          <a-menu-item>
            <a href="javascript:;">添加计算字段</a>
          </a-menu-item>
          <a-menu-item>
            <a href="javascript:;">添加分组字段</a>
          </a-menu-item>
          <a-menu-item>
            <a href="javascript:;" disabled>添加参数</a>
          </a-menu-item>
        </a-menu>
      </a-dropdown>
    </a-tabs>
  </div>
</template>

<script>
export default {
  props: ['fields']
}
</script>

<style>
</style>