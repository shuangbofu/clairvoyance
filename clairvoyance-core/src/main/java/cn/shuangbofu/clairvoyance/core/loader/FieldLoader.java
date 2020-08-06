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

    /**
     * 原始字段列表，只需要部分字段
     *
     * @param workSheetId
     * @return
     */
    public static List<Field> getOriginFields(Long workSheetId) {
        return Field.from()
                .select("id, name, title, column_type, remarks, seq_no")
                .where(Field::getWorkSheetId, workSheetId)
                .where(Field::getFieldType, FieldType.origin).all();
    }

    public static void update(Field field) {
        field.update();
    }

    public static Field getField(Long id) {
        return Field.from().where(Field::getId, id).one();
    }

    /**
     * 自定义字段列表
     *
     * @param workSheetId
     * @return
     */
    public static List<Field> getCustomFields(Long workSheetId) {
        return Field.from().where(Field::getWorkSheetId, workSheetId)
                .gt(Field::getFieldType, 1).all();
    }

    public static List<Field> getAllFields(Long workSheetId) {
        return Field.from().where(Field::getWorkSheetId, workSheetId).all();
    }
}
