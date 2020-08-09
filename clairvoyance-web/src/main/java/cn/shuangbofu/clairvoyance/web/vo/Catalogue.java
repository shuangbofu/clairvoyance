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
    private T t;
    private boolean folder;

    public Catalogue(Node node, List<Catalogue<T>> children) {
        this.node = node;
        this.children = children;
        folder = node.getRefId() == 0;
//        put("id", node.getId());
//        put("name", node.getName());
//        put("folder", node.getRefId() == 0);
//        put("parentId", node.getParentId());
//        put("children", children);
//        put("refId", node.getRefId());
    }

    private static <T extends IdVo> List<Catalogue<T>> getList0(Long parentId, List<Node> nodes, List<T> objects) {
        return nodes.stream().filter(i -> i.getParentId().equals(parentId))
                .map(node -> {
                    Catalogue<T> catalogue = new Catalogue<>(node, getList0(node.getId(), nodes, objects));
//                    Catalogue<T> catalogue = new Catalogue<>(node, getList0(node.getId(), nodes, objects));
                    Optional<T> first = objects.stream().filter(i -> i.getId().equals(node.getRefId())).findFirst();
                    first.ifPresent(o -> catalogue.setT(o));
                    return catalogue;
                })
                .collect(Collectors.toList());
    }

    public static <T extends IdVo> List<Catalogue<T>> getList(List<Node> nodes, List<T> objects) {
        return getList0(0L, nodes, objects);
    }

    public void setT(T t) {
        this.t = t;
    }
//
//    public boolean refFrom(IdVo vo) {
//        return get("refId") != null && getLong("refId") == vo.getId();
//    }
//
//    public void putData(IdVo vo) {
//        String className = vo.getClass().getSimpleName();
//        List<Field> fieldList = FIELD_MAP.get(className);
//        if (fieldList == null) {
//            Field[] fields = vo.getClass().getDeclaredFields();
//            fieldList = Arrays.asList(fields);
//            fieldList.forEach(f -> f.setAccessible(true));
//            FIELD_MAP.put(className, fieldList);
//        }
//        for (Field field : fieldList) {
//            try {
//                Object value = field.get(vo);
//                put(field.getName(), value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
