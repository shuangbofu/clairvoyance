package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Node;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/1 下午8:49
 */
@Data
public class Catalogue<T extends IdVo> {
    private static final Map<String, List<Field>> FIELD_MAP = new ConcurrentHashMap<>();
    List<Catalogue<T>> children;
    @JsonUnwrapped
    private Node node;
    @JsonUnwrapped
    private T data;
    private boolean folder;

    public Catalogue(Node node, List<Catalogue<T>> children) {
        this.node = node;
        this.children = children;
        folder = node.getRefId() == 0;
    }

    private static <T extends IdVo> List<Catalogue<T>> getList0(Long parentId, List<Node> nodes, List<T> objects) {
        return nodes.stream().filter(i -> i.getParentId().equals(parentId))
                .map(node -> {
                    Catalogue<T> catalogue = new Catalogue<>(node, getList0(node.getId(), nodes, objects));
                    Optional<T> first = objects.stream().filter(i -> i.getRefId().equals(node.getRefId())).findFirst();
                    first.ifPresent(catalogue::setData);
                    return catalogue;
                })
                .collect(Collectors.toList());
    }

    public static <T extends IdVo> List<Catalogue<T>> getList(List<Node> nodes, List<T> objects) {
        return getList0(0L, nodes, objects);
    }

    public void setData(T t) {
        data = t;
    }
}
