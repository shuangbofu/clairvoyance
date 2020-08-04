package cn.shuangbofu.clairvoyance.core.loader;

import cn.shuangbofu.clairvoyance.core.db.DataSource;

/**
 * Created by shuangbofu on 2020/8/4 上午10:51
 */
public class DataSourceLoader {

    public static DataSource getSource(Long id) {
        return DataSource.from().where(DataSource::getId, id).one();
    }
}
