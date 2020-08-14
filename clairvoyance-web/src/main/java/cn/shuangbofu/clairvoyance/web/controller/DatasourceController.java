package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Datasource;
import cn.shuangbofu.clairvoyance.core.loader.DatasourceLoader;
import cn.shuangbofu.clairvoyance.core.meta.source.SourceDb;
import cn.shuangbofu.clairvoyance.core.meta.table.Column;
import cn.shuangbofu.clairvoyance.core.query.SqlQueryRunner;
import cn.shuangbofu.clairvoyance.web.vo.DatasourceSimpleVO;
import cn.shuangbofu.clairvoyance.web.vo.Result;
import cn.shuangbofu.clairvoyance.web.vo.form.DatasourceForm;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 12:02
 */
@RestController
@RequestMapping("/api/datasource")
public class DatasourceController {

    @GetMapping("/list")
    public Result<List<DatasourceSimpleVO>> listAll() {
        return Result.success(DatasourceSimpleVO.toVOs(DatasourceLoader.simpleList()));
    }

    @PostMapping("/ping")
    public Result<Boolean> ping(@RequestParam(value = "datasourceId", required = false) Long datasourceId, @RequestBody(required = false) DatasourceForm form) {
        Datasource datasource;
        if (datasourceId != null) {
            datasource = DatasourceLoader.getSource(datasourceId);
        } else if (form != null) {
            datasource = form.pingDatasource();
        } else {
            throw new RuntimeException("no param");
        }
        boolean connectivity = SqlQueryRunner.checkConnectivity(datasource);
        return Result.success(connectivity);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DatasourceForm form) {
        Datasource datasource = form.toModel();
        boolean connectivity = SqlQueryRunner.checkConnectivity(datasource);
        if (!connectivity) {
            throw new RuntimeException("datasource connect error");
        }
        DatasourceLoader.insert(datasource);
        return Result.success(true);
    }

    @GetMapping("/tables")
    public Result<List<String>> tables(@RequestParam("datasourceId") Long datasourceId) {
        SourceDb sourceDb = SqlQueryRunner.getSourceDb(datasourceId);
        List<String> tables = sourceDb.tables();
        return Result.success(tables);
    }

    @GetMapping("/table/columns")
    public Result<List<Column>> columns(@RequestParam("datasourceId") Long datasourceId, @RequestParam("table") String table) {
        SourceDb sourceDb = SqlQueryRunner.getSourceDb(datasourceId);
        List<Column> columns = sourceDb.sourceTable(table).columns();
        return Result.success(columns);
    }
}
