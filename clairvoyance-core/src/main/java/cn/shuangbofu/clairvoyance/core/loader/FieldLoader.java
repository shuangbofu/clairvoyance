package cn.shuangbofu.clairvoyance.core.loader;

import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.enums.FieldType;
import io.github.biezhi.anima.Anima;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/5 12:15
 */
public class FieldLoader {

    public static void insert(Field field) {
        field.insert();
    }

    public static void insertBatch(List<Field> fields) {
        Anima.saveBatch(fields);
    }

    public static List<Field> originFieldList(Long workSheetId) {
        return Field.from().where(Field::getWorkSheetId, workSheetId).where(Field::getFieldType, FieldType.origin).all();
    }

    public static void update(Field field) {
        field.update();
    }
}
