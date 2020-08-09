package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Dashboard;
import cn.shuangbofu.clairvoyance.core.domain.dashboard.GlobalFilter;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
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
    private String dashboardLayout;

    /**
     * 全局过滤器配置
     */
    @ApiModelProperty("全局过滤器配置")
    private GlobalFilter globalFilter;

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

        vo.setDashboardLayout(dashboard.getLayoutConfig());
        vo.setGlobalFilter(JSON.parseObject(dashboard.getFilterConfig(), GlobalFilter.class));
        vo.setCharts(charts);

        return vo;
    }

    /**
     * TODO 目前只保存这些，后续需要再添加
     *
     * @return
     */
    public Dashboard toModel() {
        return new Dashboard().setId(getRefId())
                .setRemarks(getRemarks())
                .setName(getName())
                .setLayoutConfig(dashboardLayout.toString())
                ;
    }
}

