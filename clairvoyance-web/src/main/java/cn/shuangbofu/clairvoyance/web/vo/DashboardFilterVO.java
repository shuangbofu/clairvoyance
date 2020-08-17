package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.DashboardFilter;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.google.common.collect.Lists;
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

    private Long id;
    private Long dashboardId;
    private List<Long> selectedCharts;
    private Map<Long, Long> sheetFieldMap;
    private List<String> template;
    private List<DashboardFilterVO> children;
    private Long parentId;
    private Boolean included;
    private String name;
    private Boolean visible;

    public static DashboardFilterVO toVO(DashboardFilter filter) {
        String sheetFieldMapStr = filter.getSheetFieldMap();
        Map<Long, Long> map = new HashMap<>();
        if (StringUtils.isNotEmpty(sheetFieldMapStr)) {
            String[] split = sheetFieldMapStr.split(",");
            for (String s : split) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }
                String[] kv = s.split(":");
                if (kv.length > 1) {
                    map.put(Long.parseLong(kv[0]), Long.parseLong(kv[1]));
                }
            }
        }
        return new DashboardFilterVO()
                .setName(filter.getName())
                .setVisible(filter.getVisible())
                .setIncluded(filter.getIncluded())
                .setId(filter.getId())
                .setParentId(filter.getParentId())
                .setDashboardId(filter.getDashboardId())
                .setSelectedCharts(str2List(filter.getSelectedCharts(), Long::parseLong))
                .setSheetFieldMap(map)
                .setTemplate(str2List(filter.getTemplate(), i -> i));
    }

    private static <T> List<T> str2List(String str, Function<String, T> function) {
        if (StringUtils.isEmpty(str)) {
            return Lists.newArrayList();
        }
        return Arrays.stream(str.split(",")).map(function).collect(Collectors.toList());
    }

    public static List<DashboardFilterVO> toTreeList(List<DashboardFilter> dashboardFilters) {
        return getTreeList(0L, toVos(dashboardFilters));
    }

    public static List<DashboardFilterVO> toVos(List<DashboardFilter> dashboardFilters) {
        return dashboardFilters.stream().map(DashboardFilterVO::toVO).collect(Collectors.toList());
    }

    private static List<DashboardFilterVO> getTreeList(Long parentId, List<DashboardFilterVO> allFilters) {
        return allFilters.stream().filter(i -> i.getParentId().equals(parentId)).peek(i -> {
            Long pId = i.getId();
            List<DashboardFilterVO> children = getTreeList(pId, allFilters);
            i.setChildren(children);
        }).collect(Collectors.toList());
    }

    public DashboardFilter toModel() {
        return new DashboardFilter()
                .setVisible(visible)
                .setName(name)
                .setIncluded(included)
                .setDashboardId(dashboardId)
                .setTemplate(String.join(",", template))
                .setSelectedCharts(selectedCharts.stream().map(Object::toString).collect(Collectors.joining(",")))
                .setSheetFieldMap(sheetFieldMap.keySet().stream().map(i -> i + ":" + sheetFieldMap.get(i)).collect(Collectors.joining(",")));
    }
}
