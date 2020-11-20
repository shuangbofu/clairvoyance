package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.Model;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.core.AnimaQuery;
import io.github.biezhi.anima.core.ResultKey;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.anima.page.Page;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by shuangbofu on 2020/10/17 17:54
 */
public abstract class BaseDao<T extends Model<T>> {

    private static final String ID_NAME = "id";
    private static final String STATUS_NAME = "status";
    private static final String GMT_CREATE_NAME = "gmt_create";
    private static final String GMT_MODIFIED_NAME = "gmt_modified";
    private static final String ENV_NAME = "env";
    private static final Boolean STATUS_VALID = false;

    public static String ENV;
    private final Class<T> bClass;

    private final WhereCondition<T, Long> ID_WHERE = value -> q -> q.where(ID_NAME, value);
    private final QueryHandler<T> BASE_WHERE = q -> q.where(STATUS_NAME, STATUS_VALID)
//            .where(ENV_NAME, ENV)
            ;

    protected BaseDao(Class<T> bClass) {
        this.bClass = bClass;
//        if (StringUtils.isEmpty(ENV)) {
//            throw new RuntimeException("dao's env is empty!");
//        }
    }

    public Long insert(Model<T> model) {
        return inert(model, (m, l) -> {
            m.setId(l);
            return l;
        });
    }

    private <B> B inert(Model<T> model, BiFunction<Model<T>, Long, B> handler) {
        if (model != null) {
            long now = System.currentTimeMillis();
            model.setGmtCreate(now);
            model.setGmtModified(now);
//            model.setEnv(ENV);
            model.setDeleted(false);
            ResultKey resultKey = Anima.save(model);
            long id = resultKey.asBigInteger().longValue();
            return handler.apply(model, id);
        }
        return null;
    }

    public void insertBatch(List<T> models) {
        Anima.atomic(() -> models.forEach(this::insert)).catchException(e -> {
            throw new RuntimeException("batch insert error!");
        });
    }

    private AnimaQuery<T> update() {
        long now = System.currentTimeMillis();
        return BASE_WHERE.apply(
                Anima.update().from(bClass)
                        .set(GMT_MODIFIED_NAME, now));
    }

    private AnimaQuery<T> select() {
        return BASE_WHERE.apply(
                Anima.select().from(bClass));
    }

    private AnimaQuery<T> delete() {
        return update().set(STATUS_NAME, !STATUS_VALID);
    }

    protected <R> R findBy(QueryHandler<T> handler, ExecuteFunction<T, R> function) {
        return function.apply(handler.apply(select()).order(GMT_CREATE_NAME, OrderBy.DESC));
    }

    protected Page<T> findPageBy(int num, int size, QueryHandler<T> handler) {
        return handler.apply(select())
                .page(num, size);
    }

    protected long findCountBy(QueryHandler<T> handler) {
        return findBy(handler, AnimaQuery::count);
    }

    protected List<T> findListBy(QueryHandler<T> handler) {
        return findBy(handler, AnimaQuery::all);
    }

    public List<T> findAll() {
        return findListBy(q -> q);
    }

    protected List<T> findListLimitBy(QueryHandler<T> handler, int limit) {
        return findBy(handler, q -> q.limit(limit));
    }

    public List<T> findListLimit(int limit) {
        return findListLimitBy(q -> q, limit);
    }

    public T findOneById(Long id) {
        return findOneBy(q -> ID_WHERE.where(id).apply(q));
    }

    public List<T> findListInIds(List<Long> ids) {
        return findListBy(q -> q.in(ID_NAME, ids));
    }

    protected T findOneBy(QueryHandler<T> handler) {
        return findBy(handler, AnimaQuery::one);
    }

    protected int updateBy(QueryHandler<T> handler) {
        return handler.apply(update()).execute();
    }

    protected int updateById(Long id, QueryHandler<T> handler) {
        return updateBy(q -> handler.apply(ID_WHERE.where(id).apply(q)));
    }

    public int updateModel(Model<T> model) {
        Long id = model.getId();
        int i = new AnimaQuery<>(bClass).updateByModel(model);
        model.setId(id);
        return i;
    }

    public int deleteById(Long id) {
        return deleteBy(q -> ID_WHERE.where(id).apply(q));
    }

    protected int deleteBy(QueryHandler<T> handler) {
        return handler.apply(delete()).execute();
    }

    public interface QueryHandler<T extends Model<T>> extends Function<AnimaQuery<T>, AnimaQuery<T>> {
    }

    public interface ExecuteFunction<T extends Model<T>, R> extends Function<AnimaQuery<T>, R> {
    }

    @FunctionalInterface
    public interface WhereCondition<T extends Model<T>, R> {
        QueryHandler<T> where(R value);
    }

    @FunctionalInterface
    public interface TwoWhereCondition<T extends Model<T>, R, S> {
        QueryHandler<T> where(R r, S s);
    }

    @FunctionalInterface
    public interface ThreeWhereCondition<T extends Model<T>, R, S, X> {
        QueryHandler<T> where(R r, S s, X x);
    }
}
