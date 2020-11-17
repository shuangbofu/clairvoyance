package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.entity.WorkSheet;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午10:03
 */
public class WorkSheetDao extends BaseDao<WorkSheet> {

    public WorkSheetDao() {
        super(WorkSheet.class);
    }

    public boolean existSheet(Long datasourceId, String tableName) {
        return findCountBy(q -> q.where(WorkSheet::getDatasourceId, datasourceId).where(WorkSheet::getTableName, tableName)) > 0;
    }

    public List<WorkSheet> searchByNameAndLimit(String name, int limit) {
        return findListLimitBy(q -> {
            if (StringUtils.isNotEmpty(name)) {
                q.like(WorkSheet::getTableName, "%" + name + "%");
            }
            return q;
        }, limit);
    }

    public WorkSheet findWorkSheet(Long datasourceId, String tableName) {
        return findOneBy(q -> q.where(WorkSheet::getDatasourceId, datasourceId).where(WorkSheet::getTableName, tableName));
    }
}
