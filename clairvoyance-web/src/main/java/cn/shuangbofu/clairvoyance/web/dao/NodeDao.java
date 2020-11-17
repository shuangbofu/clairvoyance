package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.web.entity.Node;
import cn.shuangbofu.clairvoyance.web.enums.NodeType;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午9:46
 */
public class NodeDao extends BaseDao<Node> {

    private final WhereCondition<Node, NodeType> TYPE_WHERE = type -> q -> q.where(Node::getNodeType, type);
    private final TwoWhereCondition<Node, Long, NodeType> REFID_TYPE_WHERE = (refId, type) -> q -> TYPE_WHERE.where(type).apply(q.where(Node::getRefId, refId));

    public NodeDao() {
        super(Node.class);
    }

    public List<Node> getAllNodes(NodeType type) {
        return findListBy(q -> TYPE_WHERE.where(type).apply(q));
    }

    public Pair<List<Node>, List<Long>> getAllNodesPair(NodeType nodeType) {
        List<Node> allNodes = getAllNodes(nodeType);
        List<Long> ids = allNodes.stream().filter(i -> i.getRefId() != 0L)
                .map(Node::getRefId).collect(Collectors.toList());
        return new Pair<>(allNodes, ids);
    }

    public int removeByRefId(Long refId, NodeType type) {
        return deleteBy(q -> REFID_TYPE_WHERE.where(refId, type).apply(q));
    }

    public int renameByRefId(Long refId, NodeType type, String name) {
        return updateBy(q -> REFID_TYPE_WHERE.where(refId, type).apply(q.set(Node::getName, name)));
    }

    public List<Node> findNodesByParentId(Long id) {
        return findListBy(q -> q.where(Node::getParentId, id));
    }

    public List<Node> getAllChildrenNodes(Long id) {
        List<Node> nodes = findNodesByParentId(id);
        List<Node> all = Lists.newArrayList();
        nodes.forEach(node -> {
            if (node.getRefId() == null) {
                List<Node> childrenNodes = getAllChildrenNodes(node.getId());
                all.addAll(childrenNodes);
            }
        });
        all.addAll(nodes);
        return all;
    }

    public boolean existRoot(NodeType type) {
        return findCountBy(q -> TYPE_WHERE.where(type).apply(q)) > 0;
    }

    public int moveNode(Long nodeId, Long parentId) {
        return updateById(nodeId, q -> q.set(Node::getParentId, parentId));
    }

    public int moveNodeByRefId(Long refId, Long parentId) {
        return updateBy(q -> q.set(Node::getParentId, parentId).where(Node::getRefId, refId));
    }

    public int rename(Long nodeId, String name) {
        return updateById(nodeId, q -> q.set(Node::getName, name));
    }

    public int updateNode(Long nodeId, String name, Long parentId) {
        return updateById(nodeId, q -> q.set(Node::getName, name).set(Node::getParentId, parentId));
    }

    public Node findOneByName(String nodeName, NodeType nodeType) {
        return findOneBy(q -> q.where(Node::getName, nodeName).and(Node::getNodeType, nodeType));
    }

    public Node findOne(String nodeName, NodeType nodeType, Long parentId) {
        return findOneBy(q -> q.where(Node::getName, nodeName)
                .and(Node::getNodeType, nodeType)
                .and(Node::getParentId, parentId));
    }
}
