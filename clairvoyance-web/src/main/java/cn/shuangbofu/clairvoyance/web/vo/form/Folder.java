package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.db.Node;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/7/30 下午11:46
 */
@Data
@Accessors(chain = true)
@ApiModel("文件夹")
public class Folder {

    private Long id;
    @ApiModelProperty("名称")
    private String name;
//
//    @ApiModelProperty("文件夹类型")
//    private NodeType type;

    private Long refId;
    @ApiModelProperty("父文件夹")
    private Long parentId;
    @ApiModelProperty("层级")
    private int level = 0;


    public Node toNode() {
        if (level > 3) {
            throw new RuntimeException("超过三级");
        }

        return new Node()
                .setRefId(refId)
                .setParentId(parentId)
                .setName(name);
    }
}
