package cn.shuangbofu.clairvoyance.core.loader;

import cn.shuangbofu.clairvoyance.core.db.Node;
import cn.shuangbofu.clairvoyance.core.domain.Pair;
import cn.shuangbofu.clairvoyance.core.enums.NodeType;

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
        return node.insert().getId();
    }
}
