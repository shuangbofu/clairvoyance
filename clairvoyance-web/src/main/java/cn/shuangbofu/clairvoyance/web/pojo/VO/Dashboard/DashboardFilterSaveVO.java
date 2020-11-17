package cn.shuangbofu.clairvoyance.web.pojo.VO.Dashboard;

import cn.shuangbofu.clairvoyance.web.vo.DashboardFilterVO;
import lombok.Data;

import java.util.List;

/**
 * @Date: 2020/11/12 10:33 上午
 */
@Data
public class DashboardFilterSaveVO {
    private Long dashboardId;
    private List<DashboardFilterVO> dashboardFilterList;
}
