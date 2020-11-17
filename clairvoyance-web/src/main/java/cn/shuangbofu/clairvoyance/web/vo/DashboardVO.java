package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.entity.Dashboard;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Chart.ChartVO;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Dashboard.ChartLinkVO;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/31 15:04
 */
@Data
public class DashboardVO extends DashboardSimpleVO {

    private LayoutConfig layoutConfig;

    /**
     * 全局过滤器配置
     */
    private List<DashboardFilterVO> globalFilters;


    /**
     * 图标联动配置
     */
    private List<ChartLinkVO> chartLinkList;

    /**
     * 图表
     */
    private List<ChartVO> charts;

    /**
     * 上级目录ID
     */
    private Long parentId;

    /**
     * TODO 目前只保存这些，后续需要再添加
     *
     * @return
     */
    public Dashboard toModel() {
        Dashboard dashboard = new Dashboard()
                .setId(getDashboardId());
        if (StringUtils.isNotEmpty(getRemarks())) {
            dashboard.setRemarks(getRemarks());
        }
        if (StringUtils.isNotEmpty(getName())) {
            dashboard.setName(getName());
        }
        if (layoutConfig != null) {
            dashboard.setLayoutConfig(JSON.toJSONString(layoutConfig));
        }
        return dashboard;
    }
}

