package cn.shuangbofu.clairvoyance.core.field;

/**
 * Created by shuangbofu on 2020/8/6 14:40
 */
public interface Field {

    String getName();

    Long getId();

    String getTitle();

    ColumnType getType();

    String getRealName();

    String getRealAliasName();
}
