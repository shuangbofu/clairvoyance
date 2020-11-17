package cn.shuangbofu.clairvoyance.web.controller;

import cn.shuangbofu.clairvoyance.web.pojo.Model.WorkSheetField.ComputeFieldSaveModel;
import cn.shuangbofu.clairvoyance.web.pojo.Model.WorkSheetField.WorkSheetListQueryModel;
import cn.shuangbofu.clairvoyance.web.service.NodeService;
import cn.shuangbofu.clairvoyance.web.service.WorkSheetService;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import cn.shuangbofu.clairvoyance.web.vo.form.RangeRequestForm;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetForm;
import cn.shuangbofu.clairvoyance.web.vo.form.WorkSheetImport;
import cn.shuangbofu.clairvoyance.web.vo.preview.PreviewFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by shuangbofu on 2020/7/30 下午9:25
 */
@RestController
@RequestMapping("/api/workSheet")
public class WorkSheetController {

    @Autowired
    private WorkSheetService workSheetService;

    @Autowired
    private NodeService nodeService;

    /**
     * 工作表目录
     *
     * @return
     */
    @GetMapping("/catalogue")
    public Result<List<Catalogue<WorkSheetSimpleVO>>> workSheetFolders() {
        return Result.success(workSheetService.catalogue());
    }

    /**
     * 修改字段
     *
     * @param fieldSimpleVO
     * @return
     */
    @PutMapping("/field")
    public Result<Boolean> updateField(@RequestBody FieldSimpleVO fieldSimpleVO) {
        return Result.success(workSheetService.updateField(fieldSimpleVO));
    }

    /**
     * 删除字段
     *
     * @param fieldId
     * @return
     */
    @DeleteMapping("/field")
    public Result<Boolean> deleteField(@RequestParam("fieldId") Long fieldId) {
        workSheetService.deleteField(fieldId);
        return Result.success(true);
    }

    /**
     * 保存计算字段
     *
     * @param computeFieldSaveModel
     * @return
     */
    @PostMapping("/field/compute")
    public Result<Long> saveComputeField(@RequestBody ComputeFieldSaveModel computeFieldSaveModel) {
        return Result.success(workSheetService.saveComputeField(computeFieldSaveModel));
    }


    /**
     * 修改工作表备注标题
     *
     * @param form
     * @return
     */
    @PutMapping
    public Result<Boolean> updateWorkSheet(@RequestBody WorkSheetForm form) {
        return Result.success(workSheetService.updateWorkSheet(form));
    }


    /**
     * 获取工作表（包含字段）
     *
     * @return
     */
    @GetMapping
    public Result<WorkSheetVO> one(@RequestParam("workSheetId") Long workSheetId) {
        return Result.success(workSheetService.getWorkSheet(workSheetId));
    }


    @PostMapping("/list")
    public Result<List<WorkSheetVO>> list(@RequestBody WorkSheetListQueryModel workSheetListQueryModel) {
        return Result.success(workSheetService.getWorkSheetList(workSheetListQueryModel));
    }


    /**
     * 创建文件夹
     *
     * @param folder
     * @return
     */
    @PostMapping("/folder")
    public Result<Folder> createFolder(@RequestBody Folder folder) {
        nodeService.createFolder(folder);
        return Result.success(folder);
    }

    /**
     * 模糊搜索，需要权限 TODO
     *
     * @param name
     * @return
     */
    @GetMapping("/search")
    public Result<List<WorkSheetSimpleVO>> listSearch(@RequestParam(value = "name") String name, @RequestParam(required = false, defaultValue = "10") int limit) {
        return Result.success(workSheetService.search(name, limit));
    }

    /**
     * 从数据源导入工作表
     *
     * @param workSheetImport
     * @return
     */
    @PostMapping("/import")
    public Result<Boolean> importFromDatasource(@RequestBody WorkSheetImport workSheetImport) {
        return Result.success(workSheetService.importFromDatasource(workSheetImport));
    }

    /**
     * 数据预览
     *
     * @param condition
     * @return
     */
    @PostMapping("/preview/{workSheetId}")
    public Result<DataResult> previewData(@PathVariable(value = "workSheetId") Long workSheetId, @RequestBody PreviewFilter condition) {
        return Result.success(workSheetService.previewData(workSheetId, condition));
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
        return Result.success(workSheetService.getRangeData(form));
    }

    @PostMapping("/field/ranges")
    public Result<List<Object>> getRangeData2(@RequestBody List<RangeRequestForm> forms) {
        return Result.success(workSheetService.getRangeDatas(forms));
    }
}
