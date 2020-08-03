package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.core.db.Node;
import cn.shuangbofu.clairvoyance.core.db.WorkSheet;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.domain.worksheet.Field;
import cn.shuangbofu.clairvoyance.core.enums.NodeType;
import cn.shuangbofu.clairvoyance.core.loader.NodeLoader;
import cn.shuangbofu.clairvoyance.core.loader.WorkSheetLoader;
import cn.shuangbofu.clairvoyance.web.vo.Catalogue;
import cn.shuangbofu.clairvoyance.web.vo.Result;
import cn.shuangbofu.clairvoyance.web.vo.WorkSheetSimpleVO;
import cn.shuangbofu.clairvoyance.web.vo.WorkSheetVO;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param id
     * @param field
     * @return
     */
    @PutMapping("/field/{id}")
    @ApiOperation("修改字段")
    @ApiParam(name = "id", defaultValue = "1", required = true)
    public Result<Boolean> updateField(@PathVariable("id") Long id, @RequestBody Field field) {
        WorkSheet workSheet = WorkSheetLoader.byId(id);
        String fieldInfos = workSheet.getFieldInfos();
        Pair<Boolean, String> updatePair = Field.updateOne(fieldInfos, field);

        if (updatePair.getFirst()) {
            WorkSheetLoader.updateField(workSheet.getId(), updatePair.getSecond());
        }

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
}
