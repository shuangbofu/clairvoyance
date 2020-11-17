package cn.shuangbofu.clairvoyance.web.config;

/**
 * Created by shuangbofu on 2020-05-19 16:59
 *
 * @author shuangbofu
 */
public class CurrentLoginUser {
    private static final ThreadLocal<String> USER_THREAD_LOCAL;

    static {
        USER_THREAD_LOCAL = new ThreadLocal<>();
    }

    public static String getUser() {
        String jobNo = USER_THREAD_LOCAL.get();
        if (jobNo == null) {
            return "test";
        }
        return jobNo;
    }

    public static void setUser(String jobNo) {
        USER_THREAD_LOCAL.set(jobNo);
    }

    public static void clearUser() {
        if (USER_THREAD_LOCAL.get() != null) {
            USER_THREAD_LOCAL.remove();
        }
    }
}
