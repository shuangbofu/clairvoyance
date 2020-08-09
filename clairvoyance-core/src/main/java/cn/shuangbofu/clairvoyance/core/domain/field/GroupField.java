package cn.shuangbofu.clairvoyance.core.domain.field;

import cn.shuangbofu.clairvoyance.core.utils.JSON;
import lombok.Data;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/6 14:42
 * 自定义分组字段
 */
@Data
public abstract class GroupField extends Field {

    private GroupMode mode;

    /**
     * 获取自定义分组字段的值区间
     *
     * @param config
     * @return
     */
    public static GroupField parseFromConf(String config) {
        GroupField groupField = JSON.parseObject(config, GroupField.class);
        if (groupField != null) {
            GroupMode mode = groupField.getMode();
            if (mode != null) {
                return mode.getGroup(config);
            }
        }
        throw new RuntimeException("parse group field error");
    }

    public abstract List<Object> getRange();
}
