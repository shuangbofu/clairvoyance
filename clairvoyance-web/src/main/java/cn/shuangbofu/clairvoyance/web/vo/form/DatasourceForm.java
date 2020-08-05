package cn.shuangbofu.clairvoyance.web.vo.form;

import cn.shuangbofu.clairvoyance.core.db.Datasource;
import cn.shuangbofu.clairvoyance.core.enums.DatasourceType;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2020/8/4 12:11
 */
@Data
@Accessors(chain = true)
public class DatasourceForm {
    private String name;
    private String description;
    private DatasourceType type;

    /**
     * JDBC
     */
    private String jdbcUrl;
    private String username;
    private String password;

    /**
     * 其他
     */

    public Datasource toModel() {
        if (type == null) {
            throw new RuntimeException("not supported source type");
        }
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException("name is empty");
        }
        return pingDatasource().setName(name).setDescription(description);
    }

    public Datasource pingDatasource() {
        Datasource datasource = new Datasource().setType(type);
        if (type.isJdbc()) {
            JdbcParam jdbcParam = new JdbcParam(jdbcUrl, username, password);
            datasource.setConfig(JSON.toJSONString(jdbcParam));
        } else {
            // TODO 其他类型
        }
        return datasource;
    }
}
