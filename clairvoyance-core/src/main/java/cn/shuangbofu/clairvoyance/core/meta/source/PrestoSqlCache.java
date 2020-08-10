package cn.shuangbofu.clairvoyance.core.meta.source;

import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shuangbofu on 2020/8/7 12:06
 */
public class PrestoSqlCache {

    private static final PrestoSqlCache INSTANCE = new PrestoSqlCache();
    private final Map<String, Future<List<Map<String, Object>>>> queryCache = new ConcurrentHashMap<>();
    private final Cache<String, List<Map<String, Object>>> resultCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES).build();
    private final AtomicInteger counter = new AtomicInteger(1);
    private final ExecutorService executorService = Executors.newCachedThreadPool(r -> new Thread(r, "PRESTO-QUERY-" + counter.getAndIncrement()));

    public static PrestoSqlCache getInstance() {
        return INSTANCE;
    }

    /**
     * TODO 避免短时间提交多个查询
     *
     * @param param
     * @param sql
     * @return
     */
    public List<Map<String, Object>> getQueryResult(JdbcParam param, String sql) {
        synchronized (sql) {
            List<Map<String, Object>> result = resultCache.getIfPresent(sql);
            if (result != null) {
                return result;
            }
            Future<List<Map<String, Object>>> future = queryCache.computeIfAbsent(sql, k -> executorService.submit(() -> JdbcUtil.query(param, sql)));
            try {
                List<Map<String, Object>> res = future.get();
                resultCache.put(sql, res);
                return res;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                if (future.isDone()) {
                    queryCache.remove(sql);
                }
            }
            return Lists.newArrayList();
        }
    }
}
