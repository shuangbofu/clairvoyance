package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.core.db.Node;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/1 下午8:49
 */
public class Catalogue<T extends IdVo> extends JSONObject {
    private static final Map<String, List<Field>> FIELD_MAP = new ConcurrentHashMap<>();

    public Catalogue(Node node, List<Catalogue<T>> children) {
        put("id", node.getId());
        put("name", node.getName());
        put("folder", node.getRefId() == 0);
        put("parentId", node.getParentId());
        put("children", children);
        put("refId", node.getRefId());
    }

    private static <T extends IdVo> List<Catalogue<T>> getList0(Long parentId, List<Node> nodes, List<T> objects) {
        return nodes.stream().filter(i -> i.getParentId().equals(parentId))
                .map(node -> {
                    Catalogue<T> catalogue = new Catalogue<>(node, getList0(node.getId(), nodes, objects));
                    Optional<T> first = objects.stream().filter(catalogue::refFrom).findFirst();
                    first.ifPresent(o -> catalogue.putData(first.get()));
                    return catalogue;
                })
                .collect(Collectors.toList());
    }

    public static <T extends IdVo> List<Catalogue<T>> getList(List<Node> nodes, List<T> objects) {
        return getList0(0L, nodes, objects);
    }

    public boolean refFrom(IdVo vo) {
        return containsKey("refId") && getLong("refId").equals(vo.getId());
    }

    public void putData(IdVo vo) {
        String className = vo.getClass().getSimpleName();
        List<Field> fieldList = FIELD_MAP.get(className);
        if (fieldList == null) {
            Field[] fields = vo.getClass().getDeclaredFields();
            fieldList = Arrays.asList(fields);
            fieldList.forEach(f -> f.setAccessible(true));
            FIELD_MAP.put(className, fieldList);
        }
        for (Field field : fieldList) {
            try {
                Object value = field.get(vo);
                put(field.getName(), value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
