package cn.shuangbofu.clairvoyance.web.dao;

import cn.shuangbofu.clairvoyance.web.entity.Model;
import com.google.common.collect.Maps;
import io.github.biezhi.anima.Anima;

import java.util.Map;

/**
 * Created by shuangbofu on 2020/10/17 17:59
 */
public class Daos {

    private static final Map<Class<? extends BaseDao<?>>, BaseDao> CACHES = Maps.newConcurrentMap();

    private static <T extends BaseDao<R>, R extends Model<R>> T create(Class<T> tClass) {
        try {
            return tClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T extends BaseDao<R>, R extends Model<R>> T get(Class<T> tClass) {
        return (T) CACHES.computeIfAbsent(tClass, i -> create(tClass));
    }

    public static NodeDao node() {
        return get(NodeDao.class);
    }

    public static DashBoardDao dashboard() {
        return get(DashBoardDao.class);
    }

    public static DatasourceDao datasource() {
        return get(DatasourceDao.class);
    }

    public static DashboardLinkDao dashboardLinkDao() {
        return get(DashboardLinkDao.class);
    }

    public static ChartDao chart() {
        return get(ChartDao.class);
    }

    public static WorkSheetDao workSheet() {
        return get(WorkSheetDao.class);
    }

    public static SheetFieldDao sheetFieldDao() {
        return get(SheetFieldDao.class);
    }

    public static DashboardFilterDao dashboardFilter() {
        return get(DashboardFilterDao.class);
    }

    public static DashboardFilterSelectedDao dashboardFilterSelectedDao() {
        return get(DashboardFilterSelectedDao.class);
    }

    public static void atomic(Runnable runnable, String errMsg) {
        Anima.atomic(runnable).catchException(e -> {
            throw new RuntimeException(errMsg, e);
        });
    }
}
