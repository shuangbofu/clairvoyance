package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Datasource;
import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.db.Node;
import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.chart.SqlBuiler;
import cn.shuangbofu.clairvoyance.core.domain.chart.sql.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.enums.NodeType;
import cn.shuangbofu.clairvoyance.core.enums.SheetType;
import cn.shuangbofu.clairvoyance.core.loader.DatasourceLoader;
import cn.shuangbofu.clairvoyance.core.loader.FieldLoader;
import cn.shuangbofu.clairvoyance.core.loader.NodeLoader;
import cn.shuangbofu.clairvoyance.core.loader.WorkSheetLoader;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.meta.table.Sql;
import cn.shuangbofu.clairvoyance.core.query.SqlQueryRunner;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.service.FieldService;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import cn.shuangbofu.clairvoyance.web.vo.form.RangeRequestForm;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetForm;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetImport;
import cn.shuangbofu.clairvoyance.web.vo.preview.PreviewFilter;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午9:25
 */
@RestController
@RequestMapping("/api/workSheet")
@Api(tags = "工作表接口")
public class WorkSheetController {

    /**
     * 工作表目录
     *
     * @return
     */
    @GetMapping("/catalogue")
    @ApiOperation("工作表目录")
    public Result<List<Catalogue<WorkSheetSimpleVO>>> workSheetFolders() {
        Pair<List<Node>, List<Long>> allNodesPair = NodeLoader.getAllNodesPair(NodeType.workSheet);
        return Result.success(
                Catalogue.getList(allNodesPair.getFirst(),
                        WorkSheetSimpleVO.toSimpleVOList(
                                WorkSheetLoader.simpleInIds(allNodesPair.getSecond())
                        )
                )
        );
    }

    /**
     * 修改字段
     *
     * @param fieldSimpleVO
     * @return
     */
    @PutMapping("/field")
    @ApiOperation("修改字段")
    @ApiParam(name = "id", defaultValue = "1", required = true)
    public Result<Boolean> updateField(@RequestBody FieldSimpleVO fieldSimpleVO) {
        FieldLoader.update(fieldSimpleVO.toModel());
        return Result.success(true);
    }


    /**
     * 修改工作表备注标题
     *
     * @param form
     * @return
     */
    @PutMapping
    @ApiOperation("修改工作表备注和标题，只修改title和description")
    public Result<Boolean> updateWorkSheet(@RequestBody WorkSheetForm form) {
        return Result.success(WorkSheetLoader.update(form.toModel()));
    }


    /**
     * 获取工作表（包含字段）
     *
     * @return
     */
    @GetMapping
    @ApiOperation(("根据ID获取工作表"))
    public Result<WorkSheetVO> one(@RequestParam("workSheetId") Long workSheetId) {
        WorkSheet workSheet = WorkSheetLoader.getSheet(workSheetId);
        return Result.success(WorkSheetVO.toVO(workSheet));
    }

    /**
     * 创建文件夹
     *
     * @param folder
     * @return
     */
    @PostMapping("/folder")
    @ApiOperation("创建文件夹")
    public Result<Folder> createFolder(@RequestBody Folder folder) {
        Long id = NodeLoader.newNode(folder.toNode().setNodeType(NodeType.workSheet));
        return Result.success(folder.setId(id));
    }


    /**
     * TODO 需要权限
     *
     * @param limit
     * @return
     */
    @GetMapping("/list")
    public Result<List<WorkSheetSimpleVO>> listLimit(@RequestParam(value = "limit", required = false, defaultValue = "10000") int limit) {
        return Result.success(WorkSheetSimpleVO.toSimpleVOList(WorkSheetLoader.simpleAllLimit(limit)));
    }

    /**
     * 模糊搜索，需要权限 TODO
     *
     * @param name
     * @return
     */
    @GetMapping("/search")
    public Result<List<WorkSheetSimpleVO>> listSearch(@RequestParam(value = "name") String name, @RequestParam(required = false, defaultValue = "10") int limit) {
        return Result.success(WorkSheetSimpleVO.toSimpleVOList(WorkSheetLoader.simpleSearchByNameLimit(name, limit)));
    }

    /**
     * 从数据源导入工作表
     *
     * @param workSheetImport
     * @return
     */
    @PostMapping("/import")
    public Result<Boolean> importFromDatasource(@RequestBody WorkSheetImport workSheetImport) {
        Long datasourceId = workSheetImport.getDatasourceId();

        Datasource datasource = DatasourceLoader.getSource(datasourceId);

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
            Node folder = new Node()
                    .setName(StringUtils.emptyGet(datasource.getName(), sourceDb::name))
                    .setNodeType(NodeType.workSheet)
                    .setParentId(folderNodeId);
            folderNodeId = NodeLoader.newNode(folder);
        }

        for (String table : tables) {
            boolean exist = WorkSheetLoader.existSheet(datasourceId, table);
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
            Long workSheetId = WorkSheetLoader.insert(workSheet);

            List<Column> columns = sourceTable.columns();
            AtomicInteger count = new AtomicInteger();
            List<Field> fields = columns.stream().map(col ->
                    Field.newColumn(col.getName(), col.getComment(), col.getType())
                            .setSeqNo(count.incrementAndGet())
                            .setWorkSheetId(workSheetId)
            )
                    .collect(Collectors.toList());
            FieldLoader.insertBatch(fields);

            NodeLoader.newNode(
                    new Node()
                            .setName(workSheet.getTableName())
                            .setRefId(workSheetId)
                            .setParentId(folderNodeId)
                            .setNodeType(NodeType.workSheet));
        }
        return Result.success(true);
    }

    /**
     * 数据预览
     *
     * @param condition
     * @return
     */
    @PostMapping("/preview/{workSheetId}")
    public Result<DataResult> previewData(@PathVariable(value = "workSheetId") Long workSheetId, @RequestBody PreviewFilter condition) {
        condition.checkParams(FieldService.getAllFields(workSheetId));
        WorkSheet workSheet = WorkSheetLoader.getSheet(workSheetId);
        SourceTable table = SqlQueryRunner.getSourceTable(workSheet);

        List<Map<String, Object>> result = table.run(condition);

        Sql selectCount = SqlBuiler.select("COUNT(1) AS COUNT");

        List<Map<String, Object>> countResult = table.run(selectCount);
        long total = (Long) countResult.get(0).get("COUNT");
        return Result.success(new DataResult(result, total));
    }

    /**
     * 工作表获取某字段记录集
     * 考虑分组字段的情况
     *
     * @param form
     * @return
     */
    @PostMapping("/field/range")
    public Result<List<Object>> getRangeData(@RequestBody RangeRequestForm form) {
        List<ExactChartFilter> filters = Optional.ofNullable(form.getFilters()).orElse(Lists.newArrayList());
        filters.forEach(f -> f.setIncluded(true));
        RangeResult fieldRange = FieldService.getFieldRange(form.getWorkSheetId(), form.getFieldId(), Lists.newArrayList(filters));
        return Result.success(fieldRange.getRange());
    }

    @PostMapping("/field/ranges")
    public Result<List<Object>> getRangeData2(@RequestBody List<RangeRequestForm> forms) {
        forms = forms.stream().distinct().collect(Collectors.toList());
        RangeResult result = new RangeResult();
        for (RangeRequestForm form : forms) {
            RangeResult fieldRange = FieldService.getFieldRange(form.getWorkSheetId(), form.getFieldId(), null);
            result.concat(fieldRange);
        }
        return Result.success(result.getRange());
    }
}
