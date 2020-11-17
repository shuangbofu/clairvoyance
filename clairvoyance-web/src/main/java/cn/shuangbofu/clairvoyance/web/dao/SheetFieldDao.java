package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.core.field.FieldType;
import cn.shuangbofu.clairvoyance.web.entity.SheetField;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/5 12:15
 */
public class SheetFieldDao extends BaseDao<SheetField> {

    public SheetFieldDao() {
        super(SheetField.class);
    }

    public List<SheetField> getAllByWorkSheetId(Long workSheetId) {
        return findListBy(q -> q.where(SheetField::getWorkSheetId, workSheetId));
    }

    public List<SheetField> getAllByWorkSheetId4Origin(Long workSheetId) {
        return findListBy(q -> q.where(SheetField::getWorkSheetId, workSheetId).and(SheetField::getFieldType, FieldType.origin));
    }

    public List<SheetField> getComputeFieldList(Long chartId) {
        return findListBy(q -> q.where(SheetField::getChartId, chartId));
    }

    public long countComputeField(Long workSheetId) {
        return findCountBy(q -> q.where(SheetField::getWorkSheetId, workSheetId)
                .where(SheetField::getFieldType, FieldType.computed));
    }

    /**
     * 为计算字段关联图表
     *
     * @param chartId
     * @param computeFieldIdList
     * @return
     */
    public Boolean linkChart4ComputeField(Long chartId, List<Long> computeFieldIdList) {
        Daos.atomic(() -> computeFieldIdList.forEach(id -> updateById(id, q -> q.set(SheetField::getChartId, chartId))), "update error");
        return true;
    }

    /**
     * 查询工作表title相同的字段数量
     *
     * @param workSheetId
     * @param title
     * @return
     */
    public long countFieldByTitle(Long workSheetId, String title) {
        return findCountBy(q ->
                q.where(SheetField::getWorkSheetId, workSheetId)
                        .where(SheetField::getTitle, title)
                        .where(SheetField::getFieldType, FieldType.computed));
    }
}
