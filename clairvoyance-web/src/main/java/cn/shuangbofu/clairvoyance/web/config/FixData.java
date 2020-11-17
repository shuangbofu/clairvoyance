package cn.shuangbofu.clairvoyance.web.config;

import cn.shuangbofu.clairvoyance.core.chart.ChartLayer;
import cn.shuangbofu.clairvoyance.core.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.chart.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.chart.filter.InnerChartFilter;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.web.dao.Daos;
import cn.shuangbofu.clairvoyance.web.entity.Chart;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;

import java.util.*;

/**
 * Created by shuangbofu on 2020/9/7 17:20
 * <p>
 * 数据订正
 */
public class FixData {
    public static void main(String[] args) {
        String json = "{\"filters\":[{\"filterType\":\"exact\",\"id\":2961,\"range\":[\"McoO8jXh\",\"i9TxRD2l\",\"HaoiuWzy\",\"1wzznw9a\",\"vhrcfp2v\",\"b3ov3nor\"],\"included\":true}],\"layers\":[{\"innerFilters\":null,\"x\":[{\"id\":2960}],\"y\":[{\"id\":2961,\"aggregator\":\"AVG\"},{\"id\":2967,\"aggregator\":\"COUNT\"}],\"sort\":null}],\"drillFields\":[]}";

//        String s = convertInnerFiltersStructure2(json);
//        System.out.println(s);

        ChartSql chartSql = JSON.parseObject(json, ChartSql.class);

//        chartSql.getLayers().forEach(chartLayer -> chartLayer.setYOptional(Lists.newArrayList()));
        System.out.println(chartSql);
    }

    private static String convertInnerFiltersStructure2(String oldJson) {
        JsonNode jsonNode = JSON.jsonString2JsonNode(oldJson);
        JsonNode innerFiltersNode = jsonNode.get("innerFilters");
        JsonNode layersNode = jsonNode.get("layers");
        List<ChartLayer> chartLayers = JSON.parseArray(layersNode, ChartLayer.class);
        ObjectNode objectNode = (ObjectNode) jsonNode;
        if (innerFiltersNode != null) {
            for (int i = 0; i < layersNode.size(); i++) {
                JsonNode filtersNode = innerFiltersNode.isArray() ? innerFiltersNode : innerFiltersNode.get(i + "");
                if (filtersNode != null) {
                    List<InnerChartFilter> chartInnerFilters = JSON.parseArray(filtersNode, InnerChartFilter.class);

                    ChartLayer chartLayer = chartLayers.get(i);
                    if (chartLayer != null) {
                        chartLayer.setInnerFilters(chartInnerFilters);
                    }
                }
            }
            objectNode.set("layers", JSON.object2JsonNode(chartLayers));
            objectNode.remove("innerFilters");
        }
        return JSON.toJSONString(jsonNode);
    }

    public static void fix() {
        for (Chart chart : Daos.chart().findAll()) {
            fixChart(chart);
        }
    }


    private static void fixChart(Chart chart) {
//        JsonNode node = JSON.jsonString2JsonNode(chart.getSqlConfig());
//        JsonNode layers = node.get("layers");
//        Iterator<JsonNode> elements = layers.elements();
//        while (elements.hasNext()) {
//            JsonNode next = elements.next();
//            next
//        }
        ChartSql chartSql = JSON.parseObject(chart.getSqlConfig(), ChartSql.class);
        chartSql.getLayers().forEach(chartLayer -> chartLayer.setYOptional(Lists.newArrayList()).setXOptional(Lists.newArrayList()));
        Daos.chart().updateSqlConfig(chart.getId(), JSON.toJSONString(chartSql));
//        new Chart().setSqlConfig(JSON.toJSONString(chartSql)).setId(chart.getId()).update();

//        chart.setSqlConfig(chart.getSqlConfig().replace("\"inner\"", "\"" + ChartFilter.EXACT + "\""));
//        chart.setSqlConfig(convertInnerFiltersStructure2(chart.getSqlConfig()));
//        List<ChartLayoutConfig> configs = JSON.parseArray(chart.getLayoutConfig(), ChartLayoutConfig.class);
//        List<ChartType> chartTypes = new ArrayList<>();
//        JsonNode node = JSON.jsonString2JsonNode(chart.getSqlConfig());
//        JsonNode layers = node.get("layers");
//        Iterator<JsonNode> elements = layers.elements();
//
//        while (elements.hasNext()) {
//            JsonNode next = elements.next();
//            JsonNode chartTypeNode = next.get("chartType");
//            if (chartTypeNode != null) {
//                String type = chartTypeNode.asText();
//                ObjectNode objectNode = (ObjectNode) next;
//                objectNode.remove("chartType");
//                chartTypes.add(StringUtils.isNotEmpty(type) && !"null".equals(type) ? ChartType.valueOf(type) : null);
//            }
//        }
//        if (configs == null || configs.size() == 0) {
//            configs = chartTypes.stream()
//                    .map(i -> new ChartLayoutConfig().setChartType(i))
//                    .collect(Collectors.toList());
//            chart.setLayoutConfig(JSON.toJSONString(configs));
//        }
//        chart.setSqlConfig(JSON.toJSONString(node));
//        System.out.println(chart.getSqlConfig());
//        chart.update();
    }

    private static String convertInnerFiltersStructure(String oldJson) {
        JsonNode jsonNode = JSON.jsonString2JsonNode(oldJson);
        JsonNode innerFilters = jsonNode.get("innerFilters");
        if (innerFilters.isArray()) {
            Map<Integer, List<ChartFilter>> map = new HashMap<>();
            List<ChartFilter> filters = new ArrayList<>();
            map.put(0, filters);
            Iterator<JsonNode> elements = innerFilters.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                ChartFilter filter = JSON.parseObject(next, ChartFilter.class);
                filters.add(filter);
            }
            ((ObjectNode) jsonNode).set("innerFilters", JSON.object2JsonNode(map));
        }
        return JSON.toJSONString(jsonNode);
    }
}
