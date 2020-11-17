package cn.shuangbofu.clairvoyance.web.pojo.VO.Dashboard;

import cn.shuangbofu.clairvoyance.web.entity.DashboardLink;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Date: 2020/10/25 12:27 下午
 */
@Data
public class ChartLinkVO {

    private Long dashboardId;
    private Long chartId;
    private List<LinkVO> links;

    public static List<ChartLinkVO> toTreeList(Long dashboardId, List<DashboardLink> dashboardLinkList) {
        List<ChartLinkVO> chartLinkVOList = new ArrayList<>();
        Map<Long, List<DashboardLink>> chartLinkListMap = dashboardLinkList.stream().collect(Collectors.groupingBy(DashboardLink::getChartId));
        for (Long chartId : chartLinkListMap.keySet()) {
            List<LinkVO> linkVOList = new ArrayList<>();
            List<DashboardLink> linkList = chartLinkListMap.get(chartId);
            Map<Long, List<DashboardLink>> linkChartListMap = linkList.stream().collect(Collectors.groupingBy(DashboardLink::getLinkedChartId));
            for (Long linkChartId : linkChartListMap.keySet()) {
                List<LinkFieldVO> linkFieldVOList = new ArrayList<>();
                List<DashboardLink> linkFieldList = linkChartListMap.get(linkChartId);
                for (DashboardLink linkField : linkFieldList) {
                    LinkFieldVO linkFieldVO = new LinkFieldVO();
                    linkFieldVO.setFieldId(linkField.getFieldId());
                    linkFieldVO.setLinkedFieldId(linkField.getLinkedFieldId());
                    linkFieldVOList.add(linkFieldVO);
                }
                LinkVO linkVO = new LinkVO();
                linkVO.setLinkedChartId(linkChartId);
                linkVO.setFieldMappings(linkFieldVOList);
                linkVOList.add(linkVO);
            }

            ChartLinkVO chartLinkVO = new ChartLinkVO();
            chartLinkVO.setDashboardId(dashboardId);
            chartLinkVO.setChartId(chartId);
            chartLinkVO.setLinks(linkVOList);
            chartLinkVOList.add(chartLinkVO);
        }
        return chartLinkVOList;
    }
}
