<template>
    <div>
        <grid-layout
                :col-num="12"
                :is-draggable="true"
                :is-mirrored="false"
                :is-resizable="true"
                :layout.sync="layouts"
                :margin="[5, 5]"
                :row-height="30"
                :use-css-transforms="true"
                :vertical-compact="true"
                @layout-updated="layoutUpdatedEvent"
        >
            <grid-item
                    :h="item.h"
                    :i="item.i"
                    :key="item.i.id"
                    :w="item.w"
                    :x="item.x"
                    :y="item.y"
                    v-for="item in layouts"
            >
                <chart-item
                        :chart="charts.find(i=>i.chartId=== item.i)"
                        :globalFilterParams="globalFilterParams"
                        :ref="`chartItem_${item.i}`"
                />
            </grid-item>
        </grid-layout>
    </div>
</template>

<script>
    import ChartItem from "./chartItem";
    import VueGridLayout from 'vue-grid-layout';

    export default {
        props: ['layouts', 'charts', 'globalFilterParams'],
        data() {
            return {}
        },
        computed: {},
        components: {
            GridLayout: VueGridLayout.GridLayout,
            GridItem: VueGridLayout.GridItem,
            ChartItem,
        },
        methods: {
            layoutUpdatedEvent(newLayout) {
                console.log(newLayout)
                this.$emit('update', newLayout)
                Object.functionValues(this.$refs).forEach(refs => {
                    const ref = refs[0]
                    if (ref) {
                        ref.resize()
                    }
                })
            },
            refresh(chartIds) {
                chartIds.forEach(i => {
                    const ref = this.$refs[`chartItem_${i}`][0]
                    if (ref) {
                        ref.fetchData()
                    }
                })
            }
        }
    }
</script>

<style>
</style>