package cn.shuangbofu.clairvoyance.core.meta.source;

import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcParam;
import cn.shuangbofu.clairvoyance.core.meta.utils.JdbcUtil;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by shuangbofu on 2020/8/7 12:06
 */
public class SqlCache {

    private static final SqlCache INSTANCE = new SqlCache();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, Future<List<Map<String, Object>>>> queryCache = new ConcurrentHashMap<>();
    private final Cache<String, List<Map<String, Object>>> resultCache = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.SECONDS).build();

    public static SqlCache getInstance() {
        return INSTANCE;
    }

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
