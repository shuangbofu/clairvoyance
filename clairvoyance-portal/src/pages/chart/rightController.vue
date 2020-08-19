<template>
  <div>
    <div class="setting-button" @click="visible = true">
      <a-icon class="icon" type="setting" />
    </div>
    <a-drawer
      title="设置"
      placement="right"
      :visible="visible"
      @close="visible = false"
      width="500px"
    >
      <a-form-model>
        <a-form-model-item label="图表标题">
          <a-input v-model="chart.name"></a-input>
        </a-form-model-item>
        <a-form-model-item label="图表类型">
          <a-radio-group v-model="chartLayoutConfig.chartType" @change="changeChartType">
            <a-radio-button v-for="ct in chartTypes" :key="ct.name" :value="ct.name">{{ct.title}}</a-radio-button>
          </a-radio-group>
        </a-form-model-item>
      </a-form-model>
    </a-drawer>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { chartTypes } from "../components/chartPaint.js";
export default {
  data() {
    return {
      visible: false,
      chartTypes
    };
  },
  computed: {
    ...mapGetters('chart',[
      'chart',
      'chartLayer',
      'chartLayoutConfig'])
  },
  methods: {
    changeChartType() {
      this.$emit("refresh");
    }
  }
};
</script>

<style lang="less">
.setting-button {
  top: 100px;
  right: 20px;
  position: absolute;
  width: 40px;
  height: 40px;
  background: #fff;
  box-sizing: border-box;
  background: #4876ff;
  box-shadow: 1px 1px 6px rgba(0, 0, 0, 0.4);
  cursor: pointer;
  z-index: 100;
  border-radius: 4px;
  text-align: center;
  user-select: none;
  .icon {
    font-size: 22px;
    color: #fff;
    line-height: 48px;
  }
  .title {
    margin-left: 5px;
    color: #fff;
    font-size: 16px;
    line-height: 40px;
  }
  &:hover {
    background: #4876ff;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.5);
  }
}
</style>