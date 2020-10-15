package cn.shuangbofu.clairvoyance.web.vo;

import cn.shuangbofu.clairvoyance.web.entity.Datasource;
import cn.shuangbofu.clairvoyance.web.enums.DatasourceType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/4 12:06
 */
@Data
@Accessors(chain = true)
public class DatasourceSimpleVO {
    private String name;
    private String description;
    private Long id;
    private DatasourceType type;

    public static DatasourceSimpleVO toVO(Datasource datasource) {
        return new DatasourceSimpleVO()
                .setId(datasource.getId())
                .setDescription(datasource.getDescription())
                .setName(datasource.getName())
                .setType(datasource.getType());
    }

    public static List<DatasourceSimpleVO> toVOs(List<Datasource> datasources) {
        return datasources.stream().map(DatasourceSimpleVO::toVO).collect(Collectors.toList());
    }
}
