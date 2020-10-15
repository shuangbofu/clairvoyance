package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.chart.GlobalFilterParam;
import cn.shuangbofu.clairvoyance.core.chart.sql.filter.ChartFilter;
import cn.shuangbofu.clairvoyance.core.chart.sql.filter.ExactChartFilter;
import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.web.dao.ChartDao;
import cn.shuangbofu.clairvoyance.web.dao.DashBoardDao;
import cn.shuangbofu.clairvoyance.web.dao.DashboardFilterDao;
import cn.shuangbofu.clairvoyance.web.dao.NodeDao;
import cn.shuangbofu.clairvoyance.web.entity.Dashboard;
import cn.shuangbofu.clairvoyance.web.entity.DashboardFilter;
import cn.shuangbofu.clairvoyance.web.entity.Node;
import cn.shuangbofu.clairvoyance.web.enums.NodeType;
import cn.shuangbofu.clairvoyance.web.vo.*;
import cn.shuangbofu.clairvoyance.web.vo.form.DashboardForm;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import com.google.common.collect.Lists;
import io.github.biezhi.anima.Anima;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/10/15 10:00
 */
@Component
public class DashboardService {

    public List<Catalogue<DashboardSimpleVO>> getCatalogues() {
        Pair<List<Node>, List<Long>> allNodesPair = NodeDao.getAllNodesPair(NodeType.dashboard);
        List<DashboardSimpleVO> vos = DashBoardDao.inIds(allNodesPair.getSecond()).stream().map(DashboardSimpleVO::toSimpleVO).collect(Collectors.toList());
        return Catalogue.getList(allNodesPair.getFirst(), vos);
    }

    public Map<String, Long> createDashboard(DashboardForm form) {
        // 创建仪表盘
        Dashboard dashboard = form.toModel();
//        dashboard.setCreateUser(CurrentLoginUser.getUser());
//        dashboard.setModifyUser(CurrentLoginUser.getUser());

        Long id = DashBoardDao.create(dashboard);

        // 创建对应节点
        Long nodeId = NodeDao.newNode(new Node().setName(form.getName())
                .setNodeType(NodeType.dashboard)
                .setRefId(id)
                .setParentId(form.getParentId()));

        Map<String, Long> object = new HashMap<>();
        object.put("nodeId", nodeId);
        object.put("dashboardId", id);
        return object;
    }

    public DashboardVO getDashboard(Long dashboardId) {
        List<ChartVO> charts = ChartDao.getChartsByDshId(dashboardId).stream().map(ChartVO::toVO).collect(Collectors.toList());

        Dashboard dashboard = DashBoardDao.byId(dashboardId);

        return DashboardVO.toVO(dashboard, charts);
    }

    public Folder createFolder(Folder folder) {
        Long id = NodeDao.newNode(folder.toNode()
                .setNodeType(NodeType.dashboard))
//                .setCreateUser(CurrentLoginUser.getUser())
//                .setModifyUser(CurrentLoginUser.getUser()))
                ;
        return folder.setId(id);
    }

    public boolean moveNode(Folder folder) {
        NodeDao.update(folder.toNode());
        return true;
    }

    public boolean removeDashboard(Long dashboardId) {
        AtomicBoolean result = new AtomicBoolean(false);
        Anima.atomic(() -> {
            boolean delete = DashBoardDao.delete(dashboardId);
            boolean b = NodeDao.removeByRefId(dashboardId, NodeType.dashboard);
            result.set(delete && b);
        }).catchException(e -> {
            throw new RuntimeException(e);
        });
        return result.get();
    }

    public boolean saveDashboard(DashboardVO vo) {
        DashBoardDao.update(vo.toModel());
        return true;
    }

    public boolean saveDashboardFilter(DashboardFilterVO dashboardFilterVO) {
        create(dashboardFilterVO.setParentId(0L));
        return true;
    }

    private void create(DashboardFilterVO filterVO) {
        DashboardFilter dashboardFilter = filterVO.toModel();
        Long id = DashboardFilterDao.create(dashboardFilter);
        List<DashboardFilterVO> children = filterVO.getChildren();
        if (children != null && children.size() > 0) {
            children.forEach(fVo -> create(fVo.setParentId(id)));
        }
    }

    public List<Object> getFilterRange(Long dashboardFilterId, List<GlobalFilterParam> params) {
        List<DashboardFilterVO> filterVOS = null;
        RangeResult rangeResult = new RangeResult();
        if (params != null && params.size() > 0) {
            List<Long> ids = params.stream().map(GlobalFilterParam::getDashboardFilterId).collect(Collectors.toList());
            List<DashboardFilter> filters = DashboardFilterDao.inIds(ids);
            filterVOS = DashboardFilterVO.toVos(filters);
        }

        List<Object> range = Lists.newArrayList();
        DashboardFilter dashboardFilter = DashboardFilterDao.byId(dashboardFilterId);
        DashboardFilterVO vo = DashboardFilterVO.toVO(dashboardFilter);
        List<String> template = vo.getTemplate();
        if (template != null && template.size() > 0) {
            range.addAll(template);
            return range;
        }
        for (Long workSheetId : vo.getSheetFieldMap().keySet()) {
            List<ChartFilter> chartFilters = Lists.newArrayList();
            if (filterVOS != null && filterVOS.size() > 0) {
                filterVOS.forEach(filterVO -> {
                    Long fieldId = filterVO.getSheetFieldMap().get(workSheetId);
                    if (fieldId != null) {
                        Optional<GlobalFilterParam> any = params.stream().filter(i -> i.getDashboardFilterId().equals(filterVO.getId())).findAny();
                        any.ifPresent(globalFilterParam -> chartFilters.add(new ExactChartFilter(globalFilterParam.getRange(), filterVO.getIncluded(), fieldId)));
                    }
                });
            }
            RangeResult result = FieldService.getFieldRange(workSheetId, vo.getSheetFieldMap().get(workSheetId), chartFilters);
            rangeResult.concat(result);
        }
        return rangeResult.getRange();
    }

    public boolean removeFolder(Long id) {
        Anima.atomic(() -> {
            Node folderNode = NodeDao.findById(id);
            folderNode.delete();
            List<Node> allNodes = NodeDao.getAllChildrenNodes(id);
            allNodes.forEach(node -> {
                if (node.getRefId() != null) {
                    Dashboard dashboard = DashBoardDao.byId(node.getRefId());
                    if (dashboard != null) {
                        dashboard.delete();
                    }
                }
                node.delete();
            });
        }).catchException(e -> {
            throw new RuntimeException(e);
        });
        return true;
    }
}
