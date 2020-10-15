package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.web.dao.ChartDao;
import cn.shuangbofu.clairvoyance.web.dao.WorkSheetDao;
import cn.shuangbofu.clairvoyance.web.vo.WorkSheetVO;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by shuangbofu on 2020/10/15 10:10
 */
@Component
public class WorkSheetService {

    public List<WorkSheetVO> getWorkSheetsByDashboardId(Long dashboardId) {
        List<Long> workSheetIds = ChartDao.getWorkSheetIdsByDashboardId(dashboardId);
        return WorkSheetVO.toVOList(WorkSheetDao.simpleInIds(workSheetIds));
    }
}
