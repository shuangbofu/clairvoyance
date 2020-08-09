package cn.shuangbofu.clairvoyance.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by shuangbofu on 2020/7/30 下午11:35
 */
public interface IdVo {

    @JsonIgnore
    Long getRefId();
}
