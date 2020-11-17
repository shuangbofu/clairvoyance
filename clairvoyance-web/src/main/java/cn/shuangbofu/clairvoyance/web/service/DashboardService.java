package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.core.chart.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.chart.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.web.config.CurrentLoginUser;
import cn.shuangbofu.clairvoyance.web.dao.*;
import cn.shuangbofu.clairvoyance.web.entity.*;
import cn.shuangbofu.clairvoyance.web.enums.NodeType;
import cn.shuangbofu.clairvoyance.web.pojo.Model.Dashboard.ChartLinkModel;
import cn.shuangbofu.clairvoyance.web.pojo.Model.Dashboard.LinkFieldModel;
import cn.shuangbofu.clairvoyance.web.pojo.Model.Dashboard.LinkModel;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Chart.ChartVO;
import cn.shuangbofu.clairvoyance.web.pojo.VO.Dashboard.DashboardFilterSaveVO;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.DashboardForm;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/10/15 10:00
 */
@Component
public class DashboardService {

    private final DashBoardDao dashBoardDao = Daos.dashboard();
    private final ChartDao chartDao = Daos.chart();
    private final DashboardFilterDao dashboardFilterDao = Daos.dashboardFilter();
    private final DashboardFilterSelectedDao dashboardFilterSelectedDao = Daos.dashboardFilterSelectedDao();
    private final DashboardLinkDao dashboardLinkDao = Daos.dashboardLinkDao();

    @Autowired
    private cn.shuangbofu.clairvoyance.web.service.FieldService fieldService;
    @Autowired
    private Converter converter;

    @Autowired
    private NodeService nodeService;

    public List<Catalogue<DashboardSimpleVO>> getCatalogues() {
        Pair<List<Node>, List<Long>> allNodesPair = nodeService.getAllNodesPair(NodeType.dashboard);
        List<DashboardSimpleVO> vos = dashBoardDao.findListInIds(allNodesPair.getSecond()).stream().map(DashboardSimpleVO::toSimpleVO).collect(Collectors.toList());
        return Catalogue.getList(allNodesPair.getFirst(), vos);
    }

    public Map<String, Long> createDashboard(DashboardForm form) {
        // 创建仪表盘
        Dashboard dashboard = form.toModel();
        dashboard.setCreateUser(CurrentLoginUser.getUser());
        dashboard.setModifyUser(CurrentLoginUser.getUser());

        Long id = dashBoardDao.insert(dashboard);

        // 创建对应节点
        long nodeId = nodeService.createNode(form.getName(), NodeType.dashboard, id, form.getParentId());

        Map<String, Long> object = new HashMap<>();
        object.put("nodeId", nodeId);
        object.put("dashboardId", id);
        return object;
    }

    public DashboardVO getDashboard(Long dashboardId) {
        List<ChartVO> charts = chartDao.findChartsByDshId(dashboardId).stream()
                .map(chart -> converter.chart2VO(chart)).collect(Collectors.toList());

        Dashboard dashboard = dashBoardDao.findOneById(dashboardId);
        return converter.dashboard2VO(dashboard, charts);
    }

    public boolean removeDashboard(Long dashboardId) {
        AtomicBoolean result = new AtomicBoolean(false);
        Daos.atomic(() -> {
            int delete = dashBoardDao.deleteById(dashboardId);
            boolean b = nodeService.removeByRefId(dashboardId, NodeType.dashboard);
            result.set(delete > 0 && b);
        }, "remove dashboard error");
        return result.get();
    }

    public boolean saveDashboard(DashboardVO vo) {
        Dashboard dashboard = dashBoardDao.findOneById(vo.getDashboardId());
        if (dashboard != null) {
            if (dashboard.getName().equals(vo.getName())) {
                Daos.atomic(() -> {
                    dashBoardDao.rename(vo.getDashboardId(), vo.getName());
                    nodeService.renameByRefId(vo.getDashboardId(), NodeType.dashboard, vo.getName());
                    if (vo.getDashboardId() != null && vo.getParentId() != null) {
                        nodeService.moveNodeByRefId(vo.getDashboardId(), vo.getParentId());
                    }
                }, "save dashboard error");
            }
            dashBoardDao.updateModel(vo.toModel());
            return true;
        }
        return false;
    }

    public List<DashboardFilterVO> saveBatchDashboardFilter(DashboardFilterSaveVO dashboardFilterSaveVO) {
        List<DashboardFilterVO> filters = new ArrayList<>();
        if (dashboardFilterSaveVO != null) {
            if (dashboardFilterSaveVO.getDashboardFilterList() != null && dashboardFilterSaveVO.getDashboardFilterList().size() > 0) {
                deleteDashboardFilter(dashboardFilterSaveVO.getDashboardId());
                for (DashboardFilterVO dashboardFilterVO : dashboardFilterSaveVO.getDashboardFilterList()) {
                    create(dashboardFilterVO.setParentId(0L));
                }

                List<DashboardFilter> dashboardFilters = dashboardFilterDao.getListByDashboardId(dashboardFilterSaveVO.getDashboardId());
                List<DashboardFilterVO> resultVOList = new ArrayList<>();
                for (DashboardFilter dashboardFilter : dashboardFilters) {
                    List<DashboardFilterSelected> dashboardFilterSelectedList = dashboardFilterSelectedDao.getListByDashboardFilterId(dashboardFilter.getId());
                    resultVOList.add(DashboardFilterVO.toVO(dashboardFilter, dashboardFilterSelectedList));
                }

                filters = DashboardFilterVO.toTreeList(resultVOList);

            } else if (dashboardFilterSaveVO.getDashboardFilterList() != null) {
                deleteDashboardFilter(dashboardFilterSaveVO.getDashboardId());
            }
        }
        return filters;
    }

    public boolean saveDashboardFilter(DashboardFilterVO dashboardFilterVO) {
        deleteDashboardFilter(dashboardFilterVO.getDashboardId());
        create(dashboardFilterVO.setParentId(0L));
        return true;
    }

    private void create(DashboardFilterVO filterVO) {
        DashboardFilter dashboardFilter = filterVO.toModel();
        Long id = dashboardFilterDao.insert(dashboardFilter);
        List<DashboardFilterSelected> selectedList = new ArrayList<>();
        for (DashboardFilterVO.ChartConf chartConf : filterVO.getSelectedCharts()) {
            DashboardFilterSelected selected = new DashboardFilterSelected();
            selected.setDashboardId(dashboardFilter.getDashboardId());
            selected.setDashboardFilterId(id);
            selected.setWorkSheetId(chartConf.getWorkSheetId());
            selected.setChartId(chartConf.getChartId());
            selected.setFieldId(chartConf.getFieldId());
            selectedList.add(selected);
        }
        dashboardFilterSelectedDao.insertBatch(selectedList);
        List<DashboardFilterVO> children = filterVO.getChildren();
        if (children != null && children.size() > 0) {
            children.forEach(fVo -> create(fVo.setParentId(id)));
        }
    }

    public List<Object> getFilterRange(Long dashboardFilterId, List<GlobalFilterParam> params) {
        List<DashboardFilterVO> filterVOS = new ArrayList<>();
        RangeResult rangeResult = new RangeResult();
        if (params != null && params.size() > 0) {
            List<Long> ids = params.stream().map(GlobalFilterParam::getDashboardFilterId).collect(Collectors.toList());
            List<DashboardFilter> filters = dashboardFilterDao.findListInIds(ids);
            for (DashboardFilter dashboardFilter : filters) {
                List<DashboardFilterSelected> dashboardFilterSelectedList = dashboardFilterSelectedDao.getListByDashboardFilterId(dashboardFilter.getId());
                filterVOS.add(DashboardFilterVO.toVO(dashboardFilter, dashboardFilterSelectedList));
            }
        }
        DashboardFilter dashboardFilter = dashboardFilterDao.findOneById(dashboardFilterId);
        if (dashboardFilter == null) {
            throw new RuntimeException("dashboardFilter is null");
        }
        List<DashboardFilterSelected> dashboardFilterSelectedList = dashboardFilterSelectedDao.getListByDashboardFilterId(dashboardFilter.getId());
        DashboardFilterVO vo = DashboardFilterVO.toVO(dashboardFilter, dashboardFilterSelectedList);
        for (DashboardFilterVO.ChartConf conf : vo.getDistinctWorksheetAndFields()) {
            List<ChartFilter> chartFilters = Lists.newArrayList();
            Long fieldId = conf.getFieldId();
            if (filterVOS.size() > 0) {
                filterVOS.forEach(filterVO -> {
                    if (fieldId != null) {
                        Optional<GlobalFilterParam> any = params.stream().filter(i -> i.getDashboardFilterId().equals(filterVO.getId())).findAny();
                        any.ifPresent(param -> chartFilters.add(new ExactChartFilter(param.getRange(), param.getIncluded(), fieldId)));
                    }
                });
            }
            RangeResult result = fieldService.getFieldRange(conf
                    .getWorkSheetId(), fieldId, chartFilters);
            rangeResult.concat(result);
        }
        return rangeResult.getRange();
    }

    public boolean removeFolder(Long nodeId) {
        return nodeService.removeFolder(nodeId, dashBoardDao::deleteById);
    }

    /**
     * 设置图表关联信息
     *
     * @param chartLinkModel
     * @return
     */
    public Boolean setChartLink(ChartLinkModel chartLinkModel) {
        List<DashboardLink> dashboardLinkList = new ArrayList<>();
        Chart chart = chartDao.findOneById(chartLinkModel.getChartId());
        if (chart == null) {
            throw new RuntimeException("chart is not exist");
        }
        for (LinkModel linkModel : chartLinkModel.getLinks()) {
            for (LinkFieldModel linkFieldModel : linkModel.getFieldMappings()) {
                DashboardLink dashboardLink = new DashboardLink();
                dashboardLink.setDashboardId(chart.getDashboardId());
                dashboardLink.setChartId(chartLinkModel.getChartId());
                dashboardLink.setLinkedChartId(linkModel.getLinkedChartId());
                dashboardLink.setFieldId(linkFieldModel.getFieldId());
                dashboardLink.setLinkedFieldId(linkFieldModel.getLinkedFieldId());
                dashboardLinkList.add(dashboardLink);
            }
        }
        dashboardLinkDao.insertBatch(dashboardLinkList);
        return true;
    }

    /**
     * 根据图表删除图表关联
     *
     * @param chartId
     * @return
     */
    public Boolean removeChartLink(Long chartId) {
        return dashboardLinkDao.removeChartLink(chartId) > 0;
    }


    /**
     * 删除仪表盘全局筛选相关
     *
     * @param dashboardId
     * @return
     */
    public Boolean deleteDashboardFilter(Long dashboardId) {
        dashboardFilterDao.deleteByDashboardId(dashboardId);
        return dashboardFilterSelectedDao.deleteByDashboardId(dashboardId) > 0;
    }
}
