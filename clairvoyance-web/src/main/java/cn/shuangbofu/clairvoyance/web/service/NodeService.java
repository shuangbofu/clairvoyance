package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.web.config.CurrentLoginUser;
import cn.shuangbofu.clairvoyance.web.dao.Daos;
import cn.shuangbofu.clairvoyance.web.dao.NodeDao;
import cn.shuangbofu.clairvoyance.web.entity.Node;
import cn.shuangbofu.clairvoyance.web.enums.NodeType;
import cn.shuangbofu.clairvoyance.web.vo.form.Folder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by shuangbofu on 2020/10/19 10:06
 */
@Component
public class NodeService {
    private final NodeDao nodeDao = Daos.node();

    public Folder createFolder(Folder folder) {
        Long id = nodeDao.insert(folder.toNode()
                .setNodeType(NodeType.dashboard)
                .setCreateUser(CurrentLoginUser.getUser())
                .setModifyUser(CurrentLoginUser.getUser()));
        return folder.setId(id);
    }

    public boolean moveNode(Long nodeId, Long parentId) {
        return nodeDao.moveNode(nodeId, parentId) > 0;
    }

    public boolean moveNodeByRefId(Long refId, Long parentId) {
        return nodeDao.moveNodeByRefId(refId, parentId) > 0;
    }

    public boolean rename(Long nodeId, String name) {
        return nodeDao.rename(nodeId, name) > 0;
    }

    public boolean update4Folder(Folder folder) {
        return nodeDao.updateNode(folder.getId(), folder.getName(), folder.getParentId()) > 0;
    }

    public boolean renameByRefId(Long refId, NodeType type, String name) {
        return nodeDao.renameByRefId(refId, type, name) > 0;
    }

    public boolean removeByRefId(Long refId, NodeType type) {
        return nodeDao.removeByRefId(refId, type) > 0;
    }

    public Pair<List<Node>, List<Long>> getAllNodesPair(NodeType dashboard) {
        return nodeDao.getAllNodesPair(dashboard);
    }

    public long createNode(String name, NodeType type, Long refId, Long parentId) {
        Node node = new Node().setName(name).setNodeType(type).setRefId(refId).setParentId(parentId).setCreateUser(CurrentLoginUser.getUser())
                .setModifyUser(CurrentLoginUser.getUser());
        return nodeDao.insert(node);
    }

    public boolean removeFolder(Long id, Consumer<Long> consumer) {
        Daos.atomic(() -> {
            nodeDao.deleteById(id);
            List<Node> allNodes = nodeDao.getAllChildrenNodes(id);
            allNodes.forEach(node -> {
                if (node.getRefId() != null) {
                    consumer.accept(node.getRefId());
                }
                nodeDao.deleteById(node.getId());
            });
        }, "remove folder error");
        return true;
    }
}
