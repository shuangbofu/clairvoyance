package cn.shuangbofu.clairvoyance.core.loader;

import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.enums.OrderBy;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午10:03
 */
public class WorkSheetLoader {

    private static final String SIMPLE_SELECT = "id, title, table_name, description";

    public static WorkSheet getSheet(Long id) {
        return WorkSheet.from().where(WorkSheet::getId, id).one();
    }

    public static boolean update(WorkSheet workSheet) {
        if (workSheet.getId() != 0L || WorkSheetLoader.getSheet(workSheet.getId()) != null) {
            workSheet.update();
            return true;
        }
        return false;
    }

    public static Long insert(WorkSheet workSheet) {
        return workSheet.insert().getId();
    }

    public static boolean existSheet(Long datasourceId, String tableName) {
        return WorkSheet.from().where(WorkSheet::getDatasourceId, datasourceId).where(WorkSheet::getTableName, tableName).count() > 0;
    }

    //---------------------------以下三个方法都只需要【SIMPLE_SELECT】里的几个字段----------------------------------

    public static List<WorkSheet> simpleInIds(List<Long> ids) {
        return WorkSheet.from().select(SIMPLE_SELECT).in(WorkSheet::getId, ids).all();
    }

    public static List<WorkSheet> simpleAllLimit(int limit) {
        return WorkSheet.from().select(SIMPLE_SELECT).order(WorkSheet::getGmtCreate, OrderBy.DESC).limit(limit);
    }

    public static List<WorkSheet> simpleSearchByNameLimit(String name, int limit) {
        AnimaQuery<WorkSheet> query = WorkSheet.from().select(SIMPLE_SELECT);
        if (StringUtils.isNotEmpty(name)) {
            query.like(WorkSheet::getTableName, "%" + name);
        }
        return query.limit(limit);
    }

    //-------------------------------------------------------------------------------------------------------
}
