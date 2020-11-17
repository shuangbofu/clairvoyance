package cn.shuangbofu.clairvoyance.web.vo.preview;

import cn.shuangbofu.clairvoyance.core.chart.base.WhereCondition;
import cn.shuangbofu.clairvoyance.core.utils.StringUtils;
import cn.shuangbofu.clairvoyance.web.vo.FieldSimpleVO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2020/8/9 下午6:12
 */
@Data
public class ConditionPreviewFilter extends PreviewFilter {

    private List<WhereCondition> wheres;
    private WhereLinker linker;

    @Override
    public String where() {
        if (wheres == null) {
            return null;
        }
        return wheres.stream()
                .map(WhereCondition::toString)
                .collect(Collectors.joining(linker.get()));
    }

    /**
     * 解析SQL 检查where condition条件是否合法
     *
     * @param fields
     */
    @Override
    public void checkParams(List<FieldSimpleVO> fields) {
        if (StringUtils.isEmpty(whereType)) {
            return;
        }
        if (wheres == null || wheres.size() == 0) {
            return;
        }
        wheres.forEach(cond -> {
            long count = fields.stream().filter(i -> i.getName().equals(cond.getName())).count();
            if (count == 0) {
                throw new RuntimeException("not found field " + cond.getName());
            }
        });
    }

    enum WhereLinker {
        /**
         *
         */
        and, or;

        String get() {
            return String.format(" %s ", toString().toUpperCase());
        }
    }
}
