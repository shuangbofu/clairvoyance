package cn.shuangbofu.clairvoyance.core.loader;

import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import io.github.biezhi.anima.enums.OrderBy;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午10:03
 */
public class WorkSheetLoader {

    private static final String SIMPLE_SELECT = "id, title,table_name, description";

    public static WorkSheet byId(Long id) {
        return WorkSheet.from().where(WorkSheet::getId, id).one();
    }

    public static void updateField(Long id, String fields) {
        new WorkSheet().setId(id).setFieldInfos(fields).update();
    }

    public static boolean update(WorkSheet workSheet) {
        if (workSheet.getId() != 0L || WorkSheetLoader.byId(workSheet.getId()) != null) {
            workSheet.update();
            return true;
        }
        return false;
    }

    //---------------------------以下三个方法都只需要【SIMPLE_SELECT】里的几个字段----------------------------------

    public static List<WorkSheet> simpleInIds(List<Long> ids) {
        return WorkSheet.from().select(SIMPLE_SELECT).in(WorkSheet::getId, ids).all();
    }

    public static List<WorkSheet> simpleAllLimit(int limit) {
        return WorkSheet.from().select(SIMPLE_SELECT).order(WorkSheet::getGmtCreate, OrderBy.DESC).limit(limit);
    }

    public static List<WorkSheet> simpleSearchByName(String name) {
        return WorkSheet.from().select(SIMPLE_SELECT).like("%" + name).all();
    }

    //-------------------------------------------------------------------------------------------------------

    public static String getFields(Long id) {
        WorkSheet sheet = WorkSheet.from().select("field_infos").where(WorkSheet::getId, id).one();
        if (sheet != null) {
            return sheet.getFieldInfos();
        }
        return "[]";
    }

//    public static String getTableName(Long id) {
//        WorkSheet sheet = WorkSheet.from().select("table_name").where(WorkSheet::getId, id).one();
//        if (sheet != null) {
//            return sheet.getTableName();
//        }
//        return "";
//    }
}
