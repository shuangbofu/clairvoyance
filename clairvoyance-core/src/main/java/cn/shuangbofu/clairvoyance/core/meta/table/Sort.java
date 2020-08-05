package cn.shuangbofu.clairvoyance.core.meta.table;

import lombok.Data;

/**
 * Created by shuangbofu on 2020/8/4 上午10:22
 */
@Data
public class Sort {
    private String name;
    private OrderType orderType;

    enum OrderType {
        /**
         *
         */
        desc, asc;

        public String get() {
            return toString().toUpperCase();
        }
    }
}
