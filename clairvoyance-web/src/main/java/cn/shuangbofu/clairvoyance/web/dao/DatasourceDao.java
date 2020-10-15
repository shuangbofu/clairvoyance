package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.Datasource;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 上午10:51
 */
public class DatasourceDao {

    public static Datasource getSource(Long id) {
        return Datasource.from().where(Datasource::getId, id).one();
    }

    public static List<Datasource> simpleList() {
        return Datasource.from().select("id, name, description, type").all();
    }

    public static void insert(Datasource datasource) {
        datasource.insert();
    }
}
