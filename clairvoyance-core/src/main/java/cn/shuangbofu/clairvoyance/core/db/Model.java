package cn.shuangbofu.clairvoyance.core.db;

import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.annotation.Column;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.core.ResultKey;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by shuangbofu on 2019-12-29 17:44
 */
@Accessors(chain = true)
public class Model<T extends Model<?>> extends io.github.biezhi.anima.Model {

    @Getter
    @Setter
    protected Long id;

    @Getter
    @Setter
    @Column(name = "gmt_create")
    protected Long gmtCreate;

    @Getter
    @Setter
    @Column(name = "gmt_modified")
    protected Long gmtModified;

    @Getter
    @Setter
    @Column(name = "status")
    protected Boolean deleted;

    public static <B extends Model<?>> AnimaQuery<B> s(Class<B> bClass) {
        return Anima.select().from(bClass);
    }

    @Override
    public int delete() {
        return setDeleted(true)
                .update();
    }

    @Override
    public ResultKey save() {
        if (getGmtCreate() == null) {
            setGmtCreate(System.currentTimeMillis());
        }
        if (getGmtModified() == null) {
            setGmtModified(System.currentTimeMillis());
        }
        if (getDeleted() == null) {
            setDeleted(false);
        }

        ResultKey key = super.save();
        setId(key.asBigInteger().longValue());
        return key;
    }

    public T insert() {
        save();
        return (T) this;
    }

    @Override
    public int update() {
        setGmtModified(System.currentTimeMillis());
        long _id = getId();
        int update = super.update();
        setId(_id);
        return update;
    }

    public AnimaQuery<? extends io.github.biezhi.anima.Model> select() {
        return Anima.select().from(getClass());
    }
}
