package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.DashboardFilter;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/14 17:14
 */
@Data
@Accessors(chain = true)
public class DashboardFilterVO {

    private Long dashboardId;
    private List<Long> selectedCharts;
    private Map<Long, Long> sheetFieldMap;
    private List<String> template;

    public static DashboardFilterVO toVO(DashboardFilter filter) {
        String sheetFieldMapStr = filter.getSheetFieldMap();
        String[] split = sheetFieldMapStr.split(",");
        Map<Long, Long> map = new HashMap<>();
        for (String s : split) {
            if (StringUtils.isNotEmpty(s)) {
                String[] kv = s.split(":");
                if (kv.length > 1) {
                    map.put(Long.parseLong(kv[0]), Long.parseLong(kv[1]));
                }
            }
        }

        return new DashboardFilterVO()
                .setDashboardId(filter.getDashboardId())
                .setSelectedCharts(str2List(filter.getSelectedCharts(), Long::parseLong))
                .setSheetFieldMap(map)
                .setTemplate(str2List(filter.getTemplate(), i -> i))
                ;
    }

    private static <T> List<T> str2List(String str, Function<String, T> function) {
        return Arrays.stream(str.split(",")).map(function).collect(Collectors.toList());
    }

    public DashboardFilter toModel() {
        return new DashboardFilter().setDashboardId(dashboardId)
                .setTemplate(String.join(",", template))
                .setSelectedCharts(String.join(",", selectedCharts.stream().map(Object::toString).collect(Collectors.toList())))
                .setSheetFieldMap(sheetFieldMap.keySet().stream().map(i -> i + ":" + sheetFieldMap.get(i)).collect(Collectors.joining(",")));
    }
}
