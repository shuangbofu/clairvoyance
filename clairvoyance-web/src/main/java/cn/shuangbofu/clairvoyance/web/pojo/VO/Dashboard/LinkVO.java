package cn.shuangbofu.clairvoyance.web.pojo.VO.Dashboard;

import lombok.Data;

import java.util.List;

/**
 * @Date: 2020/10/25 12:25 下午
 */
@Data
public class LinkVO {

    private Long linkedChartId;
    private List<LinkFieldVO> fieldMappings;
}
