package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.core.utils.Pair;
import cn.shuangbofu.clairvoyance.web.entity.Node;
import cn.shuangbofu.clairvoyance.web.enums.NodeType;
import com.google.common.collect.Lists;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.utils.Functions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午9:46
 */
public class NodeDao {

    public static List<Node> getAllNodes(NodeType nodeType) {
        return Node.from().where(Node::getNodeType, nodeType).all();
    }

    public static Pair<List<Node>, List<Long>> getAllNodesPair(NodeType nodeType) {
        List<Node> allNodes = getAllNodes(nodeType);
        List<Long> ids = allNodes.stream().filter(i -> i.getRefId() != 0L)
                .map(Node::getRefId).collect(Collectors.toList());
        return new Pair<>(allNodes, ids);
    }

    public static Long newNode(Node node) {
        Functions.ifNotNullThen(node, node::insert);
        return node.getId();
    }

    public static void update(Node node) {
        Functions.ifNotNullThen(node, node::update);
    }

    public static Node findById(Long id) {
        return Node.from().where(Node::getId).one();
    }

    public static void removeNode(Long nodeId) {
        new Node().setId(nodeId).delete();
    }

    public static Node getNodeByRefId(Long id, NodeType type) {
        return Node.from().where(Node::getRefId, id).where(Node::getNodeType, type).one();
    }

    public static boolean removeByRefId(Long refId, NodeType type) {
        return Anima.update().from(Node.class)
                .set(Node::getDeleted, true)
                .set(Node::getGmtModified, System.currentTimeMillis())
                .where(Node::getRefId, refId).where(Node::getNodeType, type)
                .execute() > 0;
    }

    public static List<Node> findNodesByParentId(Long id) {
        return Node.from().where(Node::getParentId, id).all();
    }

    public static List<Node> getAllChildrenNodes(Long id) {
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
}
