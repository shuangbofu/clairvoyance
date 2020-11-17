package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.chart.base.Filter;
import cn.shuangbofu.clairvoyance.core.chart.field.AggregatorFunc;
import cn.shuangbofu.clairvoyance.core.chart.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.field.*;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.dao.Daos;
import cn.shuangbofu.clairvoyance.web.dao.SheetFieldDao;
import cn.shuangbofu.clairvoyance.web.dao.WorkSheetDao;
import cn.shuangbofu.clairvoyance.web.entity.SheetField;
import cn.shuangbofu.clairvoyance.web.entity.WorkSheet;
import cn.shuangbofu.clairvoyance.web.vo.FieldSimpleVO;
import cn.shuangbofu.clairvoyance.web.vo.RangeResult;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/6 15:00
 */
@Component
public class FieldService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FieldService.class);
    private final SheetFieldDao sheetFieldDao = Daos.sheetFieldDao();
    private final WorkSheetDao workSheetDao = Daos.workSheet();

    @Autowired
    private SqlQueryRunner sqlQueryRunner;

    private static List<String> getSelectNames(String fieldName, String title, AggregatorFunc... funcs) {
        if (funcs == null) {
            return Lists.newArrayList(getSelectName(fieldName, title, null));
        }
        if (funcs.length == 1) {
            AggregatorFunc func = funcs[0];
            if (AggregatorFunc.MAX.equals(func) || AggregatorFunc.MIN.equals(func)) {
                return getSelectNames(fieldName, title, AggregatorFunc.MIN, AggregatorFunc.MAX);
            } else if (func != null) {
                // TODO 先补个零，没想明白需要什么
                List<String> res = Lists.newArrayList("0");
                res.add(getSelectName(fieldName, title, func));
                return res;
            }
        }
        return Arrays.stream(funcs).map(i -> getSelectName(fieldName, title, i)).collect(Collectors.toList());
    }

    private static String getSelectName(String fieldName, String title, AggregatorFunc func) {
        if (func != null) {
            title = func.wrapWithTitle(title);
            fieldName = func.wrapWithField(fieldName);
        }
        return String.format(" %s AS `%s` ", fieldName, title);
    }

    private static List<Field> fromDb(List<SheetField> sheetFields) {
        return sheetFields.stream().map(i -> fromDb(sheetFields, i)).collect(Collectors.toList());
    }

    private static Field fromDb(List<SheetField> sheetFields, SheetField sheetField) {
        AbstractField finalField = null;
        String config = sheetField.getConfig();
        switch (sheetField.getFieldType()) {
            case origin:
                finalField = new OriginField();
                break;
            case group:
                GroupField groupField = JSON.parseObject(config, GroupField.class);
                Optional<SheetField> any = sheetFields.stream().filter(i -> i.getId().equals(groupField.getRefId())).findAny();
                any.ifPresent(value -> groupField.setRefField(fromDb(sheetFields, value)));
                finalField = groupField;
                break;
            case computed:
                ComputeField computeField = JSON.parseObject(config, ComputeField.class);
                List<SheetField> fieldList = sheetFields.stream().filter(field -> computeField.getFieldList().contains(field.getId())).collect(Collectors.toList());
                computeField.setFields(fromDb(fieldList));
                finalField = computeField;
                break;
            default:
                break;
        }
        if (finalField != null) {
            return finalField.setId(sheetField.getId())
                    .setName(sheetField.getName())
                    .setTitle(sheetField.getTitle())
                    .setType(ColumnType.valueOf(sheetField.getColumnType().name()))
                    .setFieldType(FieldType.valueOf(sheetField.getFieldType().name()));
        }
        return null;
    }

    public List<FieldSimpleVO> getAllFields(Long workSheetId) {
        List<SheetField> fields = sheetFieldDao.getAllByWorkSheetId(workSheetId);
        return FieldSimpleVO.toVOs(fields);
    }

    public List<FieldSimpleVO> getAllField4Origin(Long workSheetId) {
        List<SheetField> fields = sheetFieldDao.getAllByWorkSheetId4Origin(workSheetId);
        return FieldSimpleVO.toVOs(fields);
    }

    public List<Field> getFields(Long workSheetId) {
        List<SheetField> allSheetFields = sheetFieldDao.getAllByWorkSheetId(workSheetId);
        return fromDb(allSheetFields);
    }

    public RangeResult getFieldRange(Long workSheetId, Long fieldId, List<ChartFilter> filters, AggregatorFunc func) {
        SheetField sheetField = sheetFieldDao.findOneById(fieldId);
        List<Field> fields = getFields(workSheetId);

        RangeResult rangeResult = null;
        if (sheetField.getFieldType().name().equals(FieldType.origin.name())) {
            String fieldName = sheetField.getName();
            String title = sheetField.getTitle();
            WorkSheet sheet = workSheetDao.findOneById(workSheetId);
            SourceTable table = sqlQueryRunner.getSourceTable(sheet);

            List<Map<String, Object>> result = table.run(new Sql() {
                @Override
                public List<String> selects() {
                    return getSelectNames(fieldName, title, func);
                }

                @Override
                public String wheres() {
                    if (filters != null && filters.size() > 0) {
                        return filters.stream()
                                .peek(filter -> filter.setRealFields(fields))
                                .map(Filter::where)
                                .filter(StringUtils::isNotEmpty)
                                .collect(Collectors.joining(" AND "));
                    }
                    return null;
                }
            });
            rangeResult = new RangeResult(result);
        } else if (sheetField.getFieldType().name().equals(FieldType.group.name())) {
            if (func != null) {
                rangeResult = RangeResult.newRangeResult(Lists.newArrayList(1));
            }
            Optional<Field> any = fields.stream().filter(i -> i.getId().equals(sheetField.getId())).findAny();
            if (any.isPresent()) {
                GroupField groupField = (GroupField) any.get();
                List<String> range = groupField.getRange();
                rangeResult = RangeResult.newRangeResult(range);
            }
        }
        return rangeResult;
    }

    public RangeResult getFieldRange(Long workSheetId, Long fieldId, List<ChartFilter> filters) {
        return getFieldRange(workSheetId, fieldId, filters, null);
    }

}
