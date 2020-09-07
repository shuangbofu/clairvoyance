package cn.shuangbofu.clairvoyance.core.domain.chart;

import org.junit.Test;

/**
 * Created by shuangbofu on 2020/9/7 15:57
 */
public class ChartSqlTest {

    @Test
    public void datacorrection() {

        String oldJson = "{\"innerFilters\":[{\"filterType\":\"inner\",\"id\":364,\"filterType\":\"inner\",\"range\":[]}],\"filters\":[],\"layers\":[{\"x\":[{\"id\":3}],\"y\":[{\"id\":2},{\"id\":4}],\"sort\":null,\"chartType\":\"C5\"},{\"x\":[{\"id\":4}],\"y\":[{\"id\":2},{\"id\":4}],\"sort\":null,\"chartType\":\"C5\"}],\"drillFields\":[{\"id\":3},{\"id\":4}]}";
//        System.out.println(convertJson(oldJson));
    }

//    private String convertJson(String oldJson) {
//        JsonNode jsonNode = JSON.parseObject(oldJson, JsonNode.class);
//        JsonNode innerFilters = jsonNode.get("innerFilters");
//        if (innerFilters.isArray()) {
//            Map<Integer, List<ChartInnerFilter>> map = new HashMap<>();
//            List<ChartInnerFilter> filters = new ArrayList<>();
//            map.put(0, filters);
//            Iterator<JsonNode> elements = innerFilters.elements();
//            while (elements.hasNext()) {
//                JsonNode next = elements.next();
//                ChartInnerFilter filter = JSON.parseObject(next, ChartInnerFilter.class);
//                filters.add(filter);
//            }
//            ((ObjectNode) jsonNode).set("innerFilters", JSON.parseObject(map, JsonNode.class));
//        }
//        return JSON.toJSONString(jsonNode);
//    }
}
