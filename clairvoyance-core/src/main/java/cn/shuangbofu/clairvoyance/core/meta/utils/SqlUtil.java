package cn.shuangbofu.clairvoyance.core.meta.utils;

import cn.shuangbofu.clairvoyance.core.domain.chart.sql.base.WhereOperation;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

/**
 * Created by shuangbofu on 2020/8/4 21:18
 */
public class SqlUtil {

    /**
     * 检查并标准化where sql
     *
     * @param sql
     * @return
     */
    public static String standardWhereSql(String sql) {
        sql = sql.trim().replace("and", "AND").replace("or", "OR");
        return Arrays.stream(sql.split("AND"))
                .map(fragment1 -> Arrays.stream(fragment1.split("OR"))
                        .map(SqlUtil::parseWhereFragment)
                        .collect(Collectors.joining(" OR ")))
                .collect(Collectors.joining(" AND "));
    }

    /**
     * 获取where sql中所有key
     *
     * @param sql
     * @return
     */
    public static List<String> getWhereKeys(String sql) {
        String regex = "\\[(.*?)\\]";
        Pattern pattern = compile(regex);
        Matcher matcher = pattern.matcher(sql);
        List<String> result = Lists.newArrayList();
        while (matcher.find()) {
            String key = matcher.group(1);
            result.add(key);
        }
        return result;
    }

    private static boolean isInteger(String str) {
        Pattern pattern = compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String clearQuotationMark(String origin) {
        return origin.replace("'", "").replace("\"", "");
    }

    /**
     * 检查并标准化每个where片段 (xxx [=,>=,<= etc] xxx)
     *
     * @param fragment
     * @return
     */
    public static String parseWhereFragment(String fragment) {
        String regex = "(\\[.*\\])\\s*([><!=|]{1,2})\\s*(.+)";
        Pattern pattern = compile(regex);
        Matcher matcher = pattern.matcher(fragment.trim());
        while (matcher.find()) {
            String key = matcher.group(1);
            String op = matcher.group(2);
            String value = matcher.group(3);
            return WhereOperation.valueOfSymbol(op).where(key, value);
        }
        throw new RuntimeException("parse sql error");
    }
}
