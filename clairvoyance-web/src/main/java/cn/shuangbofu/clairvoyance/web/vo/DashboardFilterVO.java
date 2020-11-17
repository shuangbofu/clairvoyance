package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.entity.DashboardFilter;
import cn.shuangbofu.clairvoyance.web.entity.DashboardFilterSelected;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/14 17:14
 */
@Data
@Accessors(chain = true)
public class DashboardFilterVO {

    private Long id;
    private Long dashboardId;
    private List<ChartConf> selectedCharts;
    private List<DashboardFilterVO> children;
    private Long parentId;
    private String name;
    private Boolean visible;

    public static DashboardFilterVO toVO(DashboardFilter filter, List<DashboardFilterSelected> dashboardFilterSelectedList) {
        List<ChartConf> chartConfList = toVOs(dashboardFilterSelectedList);
        return new DashboardFilterVO()
                .setName(filter.getName())
                .setVisible(filter.getVisible())
                .setId(filter.getId())
                .setParentId(filter.getParentId())
                .setDashboardId(filter.getDashboardId())
                .setSelectedCharts(chartConfList);
    }

    private static <T> List<T> str2List(String str, Function<String, T> function) {
        if (StringUtils.isEmpty(str)) {
            return Lists.newArrayList();
        }
        return Arrays.stream(str.split(",")).map(function).collect(Collectors.toList());
    }

    public static List<DashboardFilterVO> toTreeList(List<DashboardFilterVO> dashboardFilterVOList) {
        return getTreeList(0L, dashboardFilterVOList);
    }

    private static List<DashboardFilterVO> getTreeList(Long parentId, List<DashboardFilterVO> allFilters) {
        return allFilters.stream().filter(i -> i.getParentId().equals(parentId)).peek(i -> {
            Long pId = i.getId();
            List<DashboardFilterVO> children = getTreeList(pId, allFilters);
            i.setChildren(children);
        }).collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public static List<ChartConf> toVOs(List<DashboardFilterSelected> dashboardFilterSelectedList) {
        List<ChartConf> chartConfList = new ArrayList<>();
        if (dashboardFilterSelectedList != null) {
            for (DashboardFilterSelected dashboardFilterSelected : dashboardFilterSelectedList) {
                ChartConf chartConf = new ChartConf();
                chartConf.setChartId(dashboardFilterSelected.getChartId());
                chartConf.setFieldId(dashboardFilterSelected.getFieldId());
                chartConf.setWorkSheetId(dashboardFilterSelected.getWorkSheetId());
                chartConfList.add(chartConf);
            }
        }
        return chartConfList;
    }

    public DashboardFilter toModel() {
        return new DashboardFilter()
                .setVisible(visible)
                .setName(name)
                .setDashboardId(dashboardId);
    }

    public List<ChartConf> getDistinctWorksheetAndFields() {
        return getSelectedCharts().stream().filter(distinctByKey(ChartConf::getWorkSheetId)).collect(Collectors.toList());
    }

    @Data
    public static class ChartConf {
        private Long chartId;
        private Long workSheetId;
        private Long fieldId;
    }
}
