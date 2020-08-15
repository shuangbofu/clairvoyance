package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Dashboard;
import cn.shuangbofu.clairvoyance.core.db.DashboardFilter;
import cn.shuangbofu.clairvoyance.core.loader.DashboardFilterLoader;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/31 15:04
 */
@Data
@ApiModel("仪表盘")
public class DashboardVO extends DashboardSimpleVO {

    @ApiModelProperty("布局配置")
    private LayoutConfig layoutConfig;

    /**
     * 全局过滤器配置
     */
    @ApiModelProperty("全局过滤器配置")
    private List<DashboardFilterVO> globalFilters;

    /**
     * 图表
     */
    private List<ChartVO> charts;

    public static DashboardVO toVO(Dashboard dashboard, List<ChartVO> charts) {
        DashboardSimpleVO simpleVO = DashboardSimpleVO.toSimpleVO(dashboard);
        DashboardVO vo = new DashboardVO();
        vo
                .setDashboardId(simpleVO.getRefId())
                .setName(simpleVO.getName())
                .setRemarks(simpleVO.getRemarks())
//                .setTags(simpleVO.getTags())
        ;
        vo.setLayoutConfig(JSON.parseObject(dashboard.getLayoutConfig(), LayoutConfig.class));

        List<DashboardFilter> dashboardFilters = DashboardFilterLoader.getListByDashboardId(dashboard.getId());

        List<DashboardFilterVO> filters = DashboardFilterVO.toTreeList(dashboardFilters);
        vo.setGlobalFilters(filters);
        vo.setCharts(charts);

        return vo;
    }

    /**
     * TODO 目前只保存这些，后续需要再添加
     *
     * @return
     */
    public Dashboard toModel() {
        Dashboard dashboard = new Dashboard()
                .setId(getDashboardId())
                .setLayoutConfig(JSON.toJSONString(layoutConfig));
        if (StringUtils.isNotEmpty(getRemarks())) {
            dashboard.setRemarks(getRemarks());
        }
        if (StringUtils.isNotEmpty(getName())) {
            dashboard.setName(getName());
        }
        return dashboard;
    }
}

