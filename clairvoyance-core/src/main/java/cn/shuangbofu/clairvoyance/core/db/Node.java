package cn.shuangbofu.clairvoyance.core.db;

import cn.shuangbofu.clairvoyance.core.enums.NodeType;
import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.annotation.Table;
import io.github.biezhi.anima.core.AnimaQuery;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午8:41
 */
@Data
@Accessors(chain = true)
@Table(name = "node")
public class Node extends Model<Node> {

    private Long id;
    private Long gmtCreate;
    private Long gmtModified;
    @Column(name = "`status`")
    private Boolean deleted;

    private String name;
    private NodeType nodeType;
    private Long refId;
    private Long parentId;

    private String createUser;
    private String modifyUser;

    public static AnimaQuery<Node> from() {
        return s(Node.class)
                .where(Node::getDeleted, false);
    }
}
