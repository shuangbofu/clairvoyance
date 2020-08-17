package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.Filter;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.domain.field.GroupField;
import cn.shuangbofu.clairvoyance.core.enums.FieldType;
import cn.shuangbofu.clairvoyance.core.loader.FieldLoader;
import cn.shuangbofu.clairvoyance.core.loader.WorkSheetLoader;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.query.SqlQueryRunner;
import cn.shuangbofu.clairvoyance.web.vo.FieldSimpleVO;
import cn.shuangbofu.clairvoyance.web.vo.RangeResult;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/6 15:00
 */
public class FieldService {

    public static List<FieldSimpleVO> getAllFields(Long workSheetId) {
        List<Field> fields = FieldLoader.getAllFields(workSheetId);
        return FieldSimpleVO.toVOs(fields);
    }

    public static List<cn.shuangbofu.clairvoyance.core.domain.field.Field> getFields(Long workSheetId) {
        List<Field> allFields = FieldLoader.getAllFields(workSheetId);
        return cn.shuangbofu.clairvoyance.core.domain.field.Field.fromDb(allFields);
    }

    public static RangeResult getFieldRange(Long workSheetId, Long fieldId, List<ChartFilter> filters) {
        Field field = FieldLoader.getField(fieldId);
        List<cn.shuangbofu.clairvoyance.core.domain.field.Field> fields = getFields(workSheetId);

        RangeResult rangeResult = null;
        if (field.getFieldType().equals(FieldType.origin)) {
            String name = field.getName();
            WorkSheet sheet = WorkSheetLoader.getSheet(workSheetId);
            SourceTable table = SqlQueryRunner.getSourceTable(sheet);

            List<Map<String, Object>> result = table.run(new Sql() {
                @Override
                public List<String> selects() {
                    return Lists.newArrayList("`" + name + "`");
                }

                @Override
                public String wheres() {
                    if (filters != null && filters.size() > 0) {
                        return filters.stream().peek(filter -> filter.setRealFields(fields))
                                .map(Filter::where)
                                .collect(Collectors.joining(" AND "));
                    }
                    return null;
                }
            });
            rangeResult = new RangeResult(result, name);
        } else if (field.getFieldType().equals(FieldType.group)) {
            Optional<cn.shuangbofu.clairvoyance.core.domain.field.Field> any = fields.stream().filter(i -> i.getId().equals(field.getId())).findAny();
            if (any.isPresent()) {
                GroupField groupField = (GroupField) any.get();
                List<String> range = groupField.getRange();
                rangeResult = new RangeResult(range);
            }
        }
        return rangeResult;
    }
}
