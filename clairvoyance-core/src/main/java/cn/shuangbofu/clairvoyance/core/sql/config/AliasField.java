package cn.shuangbofu.clairvoyance.core.sql.config;

import cn.shuangbofu.clairvoyance.core.db.Field;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/7 17:06
 */
public class AliasField {
    String aliasName;
    String fieldId;

    /**
     * 前端需要
     */

    String name;
    String title;

    public void setFields(List<Field> fields) {

    }

    private cn.shuangbofu.clairvoyance.core.domain.field.Field field() {
        return null;
    }
}
