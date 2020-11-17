package cn.shuangbofu.clairvoyance.web.pojo.Model.Dashboard;


import lombok.Data;

import java.util.List;

/**
 * @Date: 2020/10/25 12:27 下午
 */
@Data
public class ChartLinkModel {

    private Long chartId;
    private List<LinkModel> links;

}
