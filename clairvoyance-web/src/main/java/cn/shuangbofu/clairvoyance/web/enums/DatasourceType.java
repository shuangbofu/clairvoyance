package cn.shuangbofu.clairvoyance.web.enums;

import io.github.biezhi.anima.annotation.EnumMapping;
import lombok.AllArgsConstructor;

/**
 * Created by shuangbofu on 2020/8/4 12:15
 */
@EnumMapping("datasourceTypeValue")
@AllArgsConstructor
public enum DatasourceType {
    /**
     *
     */
    hive(1),
    mysql(2),
    presto(3),
    none(0),


    ;

    private final int datasourceTypeValue;

    public boolean isJdbc() {
        return datasourceTypeValue < 8;
    }
}
