package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.SheetField;
import cn.shuangbofu.clairvoyance.web.enums.FieldType;
import io.github.biezhi.anima.Anima;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/5 12:15
 */
public class SheetFieldDao {

    public static void insert(SheetField sheetField) {
        sheetField.insert();
    }

    public static void insertBatch(List<SheetField> sheetFields) {
        Anima.saveBatch(sheetFields);
    }

    /**
     * 原始字段列表，只需要部分字段
     *
     * @param workSheetId
     * @return
     */
    public static List<SheetField> getOriginFields(Long workSheetId) {
        return SheetField.from()
                .select("id, name, title, column_type, remarks, seq_no")
                .where(SheetField::getWorkSheetId, workSheetId)
                .where(SheetField::getFieldType, FieldType.origin).all();
    }

    public static void update(SheetField sheetField) {
        sheetField.update();
    }

    public static SheetField getField(Long id) {
        return SheetField.from().where(SheetField::getId, id).one();
    }

    /**
     * 自定义字段列表
     *
     * @param workSheetId
     * @return
     */
    public static List<SheetField> getCustomFields(Long workSheetId) {
        return SheetField.from().where(SheetField::getWorkSheetId, workSheetId)
                .gt(SheetField::getFieldType, 1).all();
    }

    public static List<SheetField> getAllFields(Long workSheetId) {
        return SheetField.from().where(SheetField::getWorkSheetId, workSheetId).all();
    }
}
