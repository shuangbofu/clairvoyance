package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.web.entity.Node;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:46
 */
@Data
@Accessors(chain = true)
public class Folder {

    private Long id;
    private String name;
    private Long refId;
    private Long parentId;
    private int level = 0;


    public Node toNode() {
        if (level > 3) {
            throw new RuntimeException("more than level 3");
        }

        return new Node()
                .setRefId(refId)
                .setParentId(parentId)
                .setName(name);
    }
}
