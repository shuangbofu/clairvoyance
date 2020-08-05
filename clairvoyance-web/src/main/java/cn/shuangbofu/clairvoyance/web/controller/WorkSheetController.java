package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Datasource;
import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.db.Node;
import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.enums.NodeType;
import cn.shuangbofu.clairvoyance.core.loader.DatasourceLoader;
import cn.shuangbofu.clairvoyance.core.loader.FieldLoader;
import cn.shuangbofu.clairvoyance.core.loader.NodeLoader;
import cn.shuangbofu.clairvoyance.core.loader.WorkSheetLoader;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceDb;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceTable;
import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.query.SqlQueryRunner;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetForm;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetImport;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午9:25
 */
@RestController
@RequestMapping("/workSheet")
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
     * @param fieldVO
     * @return
     */
    @PutMapping("/field")
    @ApiOperation("修改字段")
    @ApiParam(name = "id", defaultValue = "1", required = true)
    public Result<Boolean> updateField(@RequestBody FieldVO fieldVO) {
        FieldLoader.update(fieldVO.toModel());
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
    public Result<WorkSheetVO> one(@RequestParam("id") Long id) {
        WorkSheet workSheet = WorkSheetLoader.byId(id);
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
    public Result<List<WorkSheetSimpleVO>> listLimit(@RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {
        return Result.success(WorkSheetSimpleVO.toSimpleVOList(WorkSheetLoader.simpleAllLimit(limit)));
    }

    /**
     * 模糊搜索，需要权限 TODO
     *
     * @param name
     * @return
     */
    @GetMapping("/search")
    public Result<List<WorkSheetSimpleVO>> listSearch(@RequestParam(value = "name") String name) {
        return Result.success(WorkSheetSimpleVO.toSimpleVOList(WorkSheetLoader.simpleSearchByName(name)));
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
        if (workSheetImport.isNewFolder()) {
            Node folder = new Node()
                    .setName(StringUtils.emptyGet(datasource.getName(), sourceDb::name))
                    .setNodeType(NodeType.workSheet)
                    .setParentId(folderNodeId);
            folderNodeId = NodeLoader.newNode(folder);
        }
        List<String> tables;
        List<String> allTables = sourceDb.tables();
        if (workSheetImport.isAllTables()) {
            tables = allTables;
        } else {
            tables = workSheetImport.getTables()
                    .stream().filter(allTables::contains)
                    .collect(Collectors.toList());
        }
        for (String table : tables) {
            boolean exist = WorkSheetLoader.existSheet(datasourceId, table);
            if (exist) {
                continue;
            }
            SourceTable sourceTable = sourceDb.sourceTable(table);
            String tableComment = sourceTable.comment();
            List<Column> columns = sourceTable.columns();
            AtomicInteger count = new AtomicInteger();
            List<Field> fields = columns.stream().map(col ->
                    Field.newColumn(col.getName(), col.getComment(), col.getType())
                            .setSeqNo(count.incrementAndGet()))
                    .collect(Collectors.toList());
            FieldLoader.insertBatch(fields);

            WorkSheet workSheet = new WorkSheet()
                    .setTitle(table)
                    .setDescription(tableComment)
                    .setTableName(table)
                    .setDatasourceId(datasourceId)
                    .setLastSyncTime(System.currentTimeMillis())
                    .setSheetType(workSheetImport.getSheetType());
            Long id = WorkSheetLoader.insert(workSheet);

            NodeLoader.newNode(
                    new Node()
                            .setName(workSheet.getTableName())
                            .setRefId(id)
                            .setParentId(folderNodeId)
                            .setNodeType(NodeType.workSheet));
        }
        return Result.success(true);
    }

    @PostMapping("/preview")
    public Result<PreviewResult> previewData(@RequestBody PreviewCondition condition) {
        Long workSheetId = condition.getWorkSheetId();
        List<Field> fields = FieldLoader.originFieldList(workSheetId);
        condition.checkParams(fields);
        WorkSheet workSheet = WorkSheetLoader.byId(workSheetId);
        SourceTable table = SqlQueryRunner.getSourceTable(workSheet);

        List<Map<String, Object>> result = table.run(condition);

        List<Map<String, Object>> countResult = table.run(() -> Lists.newArrayList("COUNT(1) AS COUNT"));
        long total = (Long) countResult.get(0).get("COUNT");
        return Result.success(new PreviewResult(result, total));
    }
}
