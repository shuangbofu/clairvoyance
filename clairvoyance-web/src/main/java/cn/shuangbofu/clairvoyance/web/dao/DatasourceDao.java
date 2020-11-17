package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.Datasource;
import cn.shuangbofu.clairvoyance.web.enums.DatasourceType;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/4 上午10:51
 */
public class DatasourceDao extends BaseDao<Datasource> {
    public DatasourceDao() {
        super(Datasource.class);
    }

    public List<Datasource> simpleList() {
        return findListBy(q -> q.select("id, name, description, type"));
    }

    public Datasource findByDbNameAndType(String dbName, DatasourceType datasourceType) {
        return findOneBy(q -> q.where(Datasource::getDbName, dbName).where(Datasource::getType, datasourceType));
    }
}
