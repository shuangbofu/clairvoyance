package cn.shuangbofu.clairvoyance.core.loader;

import cn.shuangbofu.clairvoyance.core.db.Node;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.enums.NodeType;
import cn.shuangbofu.clairvoyance.core.utils.Functions;
import io.github.biezhi.anima.Anima;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/7/30 下午9:46
 */
public class NodeLoader {

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

    public static Node find(Long refId, NodeType nodeType) {
        return Node.from().where(Node::getRefId, refId).where(Node::getNodeType, nodeType).one();
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
}
