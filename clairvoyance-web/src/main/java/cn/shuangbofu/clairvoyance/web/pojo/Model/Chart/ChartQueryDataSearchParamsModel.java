package cn.shuangbofu.clairvoyance.web.pojo.Model.Chart;

import lombok.Data;

import java.util.List;

/**
 * @Date: 2020/11/5 4:26 下午
 */
@Data
public class ChartQueryDataSearchParamsModel {

    private Long id;
    private Boolean included;
    private List<Object> range;

}
