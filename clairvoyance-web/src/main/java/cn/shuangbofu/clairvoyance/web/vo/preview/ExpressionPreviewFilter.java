package cn.shuangbofu.clairvoyance.web.vo.preview;

import cn.shuangbofu.clairvoyance.core.meta.utils.SqlUtil;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.vo.FieldSimpleVO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/9 下午6:06
 */
@Data
public class ExpressionPreviewFilter extends PreviewFilter {
    private String sql;

    @Override
    public String where() {
        return sql;
    }

    @Override
    public void checkParams(List<FieldSimpleVO> fields) {
        if (StringUtils.isEmpty(sql)) {
            return;
        }
        List<String> whereKeys = SqlUtil.getWhereKeys(sql);
        String notExistKeys = whereKeys.stream().filter(i -> !fields.stream().map(FieldSimpleVO::getTitle).collect(Collectors.toList()).contains(i))
                .collect(Collectors.joining(","));
        if (StringUtils.isNotEmpty(notExistKeys)) {
            throw new RuntimeException("not found fields  " + notExistKeys);
        }
        sql = SqlUtil.standardWhereSql(sql);
        for (FieldSimpleVO field : fields) {
            sql = sql.replace(String.format("[%s]", field.getTitle()), field.getName());
        }
    }
}
