package cn.shuangbofu.clairvoyance.web.config;

import cn.shuangbofu.clairvoyance.core.db.Chart;
import cn.shuangbofu.clairvoyance.core.domain.chart.ChartLayer;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartInnerFilter;
import cn.shuangbofu.clairvoyance.core.enums.ChartType;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.vo.ChartLayoutConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/9/7 17:20
 */
public class FixData {
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
                    List<ChartInnerFilter> chartInnerFilters = JSON.parseArray(filtersNode, ChartInnerFilter.class);

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
        List<Chart> charts = Chart.from().all();
        for (Chart chart : charts) {
            fixChart(chart);
        }
    }

    private static void fixChart(Chart chart) {
        chart.setSqlConfig(convertInnerFiltersStructure2(chart.getSqlConfig()));
        List<ChartLayoutConfig> configs = JSON.parseArray(chart.getLayoutConfig(), ChartLayoutConfig.class);
        List<ChartType> chartTypes = new ArrayList<>();
        JsonNode node = JSON.jsonString2JsonNode(chart.getSqlConfig());
        JsonNode layers = node.get("layers");
        Iterator<JsonNode> elements = layers.elements();

        while (elements.hasNext()) {
            JsonNode next = elements.next();
            JsonNode chartTypeNode = next.get("chartType");
            if (chartTypeNode != null) {
                String type = chartTypeNode.asText();
                ObjectNode objectNode = (ObjectNode) next;
                objectNode.remove("chartType");
                chartTypes.add(StringUtils.isNotEmpty(type) && !"null".equals(type) ? ChartType.valueOf(type) : null);
            }
        }
        if (configs == null || configs.size() == 0) {
            configs = chartTypes.stream()
                    .map(i -> new ChartLayoutConfig().setChartType(i))
                    .collect(Collectors.toList());
            chart.setLayoutConfig(JSON.toJSONString(configs));
        }
        chart.setSqlConfig(JSON.toJSONString(node));
        System.out.println(chart.getSqlConfig());
        chart.update();
    }

    private static String convertInnerFiltersStructure(String oldJson) {
        JsonNode jsonNode = JSON.jsonString2JsonNode(oldJson);
        JsonNode innerFilters = jsonNode.get("innerFilters");
        if (innerFilters.isArray()) {
            Map<Integer, List<ChartInnerFilter>> map = new HashMap<>();
            List<ChartInnerFilter> filters = new ArrayList<>();
            map.put(0, filters);
            Iterator<JsonNode> elements = innerFilters.elements();
            while (elements.hasNext()) {
                JsonNode next = elements.next();
                ChartInnerFilter filter = JSON.parseObject(next, ChartInnerFilter.class);
                filters.add(filter);
            }
            ((ObjectNode) jsonNode).set("innerFilters", JSON.object2JsonNode(map));
        }
        return JSON.toJSONString(jsonNode);
    }
}
