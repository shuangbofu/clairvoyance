package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.chart.SqlBuiler;
import cn.shuangbofu.clairvoyance.core.chart.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.utils.JSON;
import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.dao.*;
import cn.shuangbofu.clairvoyance.web.entity.Datasource;
import cn.shuangbofu.clairvoyance.web.entity.Node;
import cn.shuangbofu.clairvoyance.web.entity.SheetField;
import cn.shuangbofu.clairvoyance.web.entity.WorkSheet;
import cn.shuangbofu.clairvoyance.web.enums.FieldType;
import cn.shuangbofu.clairvoyance.web.enums.NodeType;
import cn.shuangbofu.clairvoyance.web.enums.SheetType;
import cn.shuangbofu.clairvoyance.web.pojo.Model.WorkSheetField.ComputeFieldSaveModel;
import cn.shuangbofu.clairvoyance.web.pojo.Model.WorkSheetField.WorkSheetListQueryModel;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.RangeRequestForm;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetForm;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetImport;
import cn.shuangbofu.clairvoyance.web.vo.preview.PreviewFilter;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static cn.shuangbofu.clairvoyance.web.dao.Daos.*;

/**
 * Created by shuangbofu on 2020/10/15 10:10
 */
@Component
public class WorkSheetService {
    private final ChartDao chartDao = chart();
    private final WorkSheetDao workSheetDao = Daos.workSheet();
    private final NodeDao nodeDao = node();
    private final SheetFieldDao sheetFieldDao = Daos.sheetFieldDao();

    @Autowired
    private Converter converter;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private FieldService fieldService;

    @Autowired
    private SqlQueryRunner sqlQueryRunner;

    public List<WorkSheetVO> getWorkSheetsByDashboardId(Long dashboardId) {
        List<Long> workSheetIds = chartDao.getWorkSheetIdsByDshId(dashboardId);
        return converter.worksheets2VOList(workSheetDao.findListInIds(workSheetIds));
    }

    public List<Catalogue<WorkSheetSimpleVO>> catalogue() {
        Pair<List<Node>, List<Long>> allNodesPair = nodeDao.getAllNodesPair(NodeType.workSheet);
        return
                Catalogue.getList(allNodesPair.getFirst(),
                        WorkSheetSimpleVO.toSimpleVOList(
                                workSheetDao.findListInIds(allNodesPair.getSecond())
                        )
                );
    }

    public boolean updateField(FieldSimpleVO fieldSimpleVO) {
        sheetFieldDao.updateModel(fieldSimpleVO.toModel());
        return true;
    }

    public boolean updateWorkSheet(WorkSheetForm form) {
        return workSheetDao.updateModel(form.toModel()) > 0;
    }

    public WorkSheetVO getWorkSheet(Long workSheetId) {
        WorkSheet workSheet = workSheetDao.findOneById(workSheetId);
        return converter.worksheet2VO(workSheet);
    }


    public List<WorkSheetVO> getWorkSheetList(WorkSheetListQueryModel workSheetIdList) {
        List<WorkSheet> workSheetVOList = workSheetDao.findListInIds(workSheetIdList.getWorkSheetIdList());
        return converter.worksheets2VOList(workSheetVOList);
    }

    public boolean importFromDatasource(WorkSheetImport workSheetImport) {
        Long datasourceId = workSheetImport.getDatasourceId();

        Datasource datasource = datasource().findOneById(datasourceId);

        SourceDb sourceDb = SqlQueryRunner.getSourceDb(datasource);

        Long folderNodeId = workSheetImport.getFolderId();

        List<String> tables;
        List<String> allTables = sourceDb.tables();
        if (workSheetImport.isAllTables()) {
            tables = allTables;
        } else {
            tables = workSheetImport.getTables()
                    .stream().filter(allTables::contains)
                    .collect(Collectors.toList());
        }

        if (workSheetImport.isNewFolder()) {
            nodeService.createNode(StringUtils.emptyGet(datasource.getName(), sourceDb::name)
                    , NodeType.workSheet, 0L, folderNodeId);
        }

        for (String table : tables) {
            boolean exist = workSheetDao.existSheet(datasourceId, table);
            if (exist) {
                continue;
            }
            SourceTable sourceTable = sourceDb.sourceTable(table);
            String tableComment = sourceTable.comment();
            WorkSheet workSheet = new WorkSheet()
                    .setTitle(table)
                    .setSheetType(SheetType.source)
                    .setDescription(tableComment)
                    .setTableName(table)
                    .setDatasourceId(datasourceId)
                    .setLastSyncTime(System.currentTimeMillis());
            Long workSheetId = workSheetDao.insert(workSheet);

            List<Column> columns = sourceTable.columns();
            AtomicInteger count = new AtomicInteger();
            List<SheetField> sheetFields = columns.stream().map(col ->
                    SheetField.newColumn(col.getName(), col.getComment(), col.getType())
                            .setSeqNo(count.incrementAndGet())
                            .setWorkSheetId(workSheetId)
            )
                    .collect(Collectors.toList());
            sheetFieldDao.insertBatch(sheetFields);
            nodeService.createNode(workSheet.getTableName(), NodeType.workSheet, workSheetId, folderNodeId);
        }
        return true;
    }

    public DataResult previewData(Long workSheetId, PreviewFilter condition) {
        condition.checkParams(fieldService.getAllFields(workSheetId));
        WorkSheet workSheet = workSheetDao.findOneById(workSheetId);
        SourceTable table = sqlQueryRunner.getSourceTable(workSheet);

        List<Map<String, Object>> result = table.run(condition);

        Sql selectCount = SqlBuiler.select("COUNT(1) AS COUNT");

        List<Map<String, Object>> countResult = table.run(selectCount);
        long total = (Long) countResult.get(0).get("COUNT");
        return new DataResult(result, total);
    }

    public List<Object> getRangeData(RangeRequestForm form) {
        List<ChartFilter> filters = Optional.ofNullable(form.getFilters()).orElse(Lists.newArrayList());
        RangeResult fieldRange = fieldService.getFieldRange(form.getWorkSheetId(), form.getFieldId(), Lists.newArrayList(filters), form.getAggregator());
        return fieldRange.getRange();
    }

    public List<Object> getRangeDatas(List<RangeRequestForm> forms) {
        forms = forms.stream().distinct().collect(Collectors.toList());
        RangeResult result = new RangeResult();
        for (RangeRequestForm form : forms) {
            List<ChartFilter> filters = Lists.newArrayList(Optional.ofNullable(form.getFilters()).orElse(Lists.newArrayList()));
            RangeResult fieldRange = fieldService.getFieldRange(form.getWorkSheetId(), form.getFieldId(), filters, form.getAggregator());
            result.concat(fieldRange);
        }
        return result.getRange();
    }

    public List<WorkSheetSimpleVO> search(String name, int limit) {
        return WorkSheetSimpleVO.toSimpleVOList(workSheetDao.searchByNameAndLimit(name, limit));
    }

    /**
     * 保存计算字段
     *
     * @param computeFieldSaveModel
     */
    public long saveComputeField(ComputeFieldSaveModel computeFieldSaveModel) {

        if (!checkComputeField(computeFieldSaveModel)) {
            throw new RuntimeException("请校验字段参数是否正确");
        }

        SheetField sheetField = new SheetField();
        sheetField.setId(computeFieldSaveModel.getFieldId());
        sheetField.setTitle(computeFieldSaveModel.getTitle());
        sheetField.setColumnType(computeFieldSaveModel.getType());
        sheetField.setFieldType(FieldType.computed);
        sheetField.setRemarks(computeFieldSaveModel.getRemark());
        Map<String, Object> config = new HashMap<>();
        config.put("formula", computeFieldSaveModel.getFormula());
        config.put("fieldList", computeFieldSaveModel.getFieldList());
        sheetField.setConfig(JSON.toJSONString(config));
        sheetField.setWorkSheetId(computeFieldSaveModel.getWorkSheetId());
        if (computeFieldSaveModel.getChartId() != null) {
            sheetField.setChartId(computeFieldSaveModel.getChartId());
        }
        if (sheetField.getId() != null) {
            sheetField.update();
        } else {
            sheetField.setName(getComputeFieldName(computeFieldSaveModel.getWorkSheetId()));
            sheetField.setId(sheetField.insert().getId());
        }
        return sheetField.getId();
    }

    /**
     * 拼装计算字段name
     *
     * @param workSheetId
     * @return
     */
    private String getComputeFieldName(Long workSheetId) {
        long computeCount = sheetFieldDao.countComputeField(workSheetId);
        return "COMPUTE_" + computeCount;
    }

    /**
     * 删除字段
     *
     * @param fieldId
     * @return
     */
    public void deleteField(Long fieldId) {
        SheetField sheetField = sheetFieldDao.findOneById(fieldId);
        if (FieldType.origin.equals(sheetField.getFieldType())) {
            throw new RuntimeException("can't delete system field");
        }
        sheetFieldDao.deleteById(fieldId);
    }


    /**
     * 校验计算字段
     *
     * @param computeFieldSaveModel
     * @return
     */
    private Boolean checkComputeField(ComputeFieldSaveModel computeFieldSaveModel) {
        long fieldCount = sheetFieldDao.countFieldByTitle(computeFieldSaveModel.getWorkSheetId(), computeFieldSaveModel.getTitle());
        if (fieldCount > 0) {
            throw new RuntimeException("fieldTitle is repeated");
        }

        if (sqlValidate(computeFieldSaveModel.getFormula())) {
            throw new RuntimeException("Formula is illegal");
        }

        return true;
    }


    /**
     * 参数校验
     *
     * @param str
     */
    private boolean sqlValidate(String str) {
        if (StringUtils.isNotEmpty(str)) {
            str = str.toLowerCase();//统一转为小写
            String badStr = "select,update,and,or,delete,insert,truncate,char,into,ascii,declare,exec,master,into,drop,execute,table";
            String[] badStrs = badStr.split(",");
            for (int i = 0; i < badStrs.length; i++) {
                //循环检测，判断在请求参数当中是否包含SQL关键字
                if (str.indexOf(badStrs[i]) >= 0) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }


}
