package cn.shuangbofu.clairvoyance.web.pojo.Model.Dashboard;

import lombok.Data;

import java.util.List;

/**
 * @Date: 2020/10/25 12:25 下午
 */
@Data
public class LinkModel {

    private Long linkedChartId;
    private List<LinkFieldModel> fieldMappings;
}
