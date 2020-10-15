package cn.shuangbofu.clairvoyance.core.chart.result;

import cn.shuangbofu.clairvoyance.core.chart.ChartSql;
import cn.shuangbofu.clairvoyance.core.chart.sql.base.FieldAlias;
import cn.shuangbofu.clairvoyance.core.field.AbstractChartField;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/9/30 10:19
 */
public class ReplaceKeyHandler extends AbstractResultHandler {
    public ReplaceKeyHandler(ChartSql chartSql) {
        super(chartSql);
    }

    @Override
    public List<Map<String, Object>> handle(List<Map<String, Object>> origin) {
        List<FieldAlias> xy = getLayer().getXY();
        Map<String, Long> mapping = xy.stream().collect(Collectors.toMap(FieldAlias::getFinalAliasName, AbstractChartField::getUniqId));
        List<Map<String, Object>> res = new ArrayList<>();
        origin.forEach(map -> {
            Map<String, Object> newMap = new LinkedHashMap<>();
            for (String key : map.keySet()) {
                Long v = mapping.get(key);
                if (v != null) {
                    newMap.put(v.toString(), map.get(key));
                }
            }
            res.add(newMap);
        });
        origin.clear();
        return res;
    }
}
