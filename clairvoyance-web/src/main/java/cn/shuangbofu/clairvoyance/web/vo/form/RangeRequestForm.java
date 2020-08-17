package cn.shuangbofu.clairvoyance.web.vo.form;

import lombok.Data;

import java.util.Objects;

/**
 * Created by shuangbofu on 2020/8/6 11:50
 */
@Data
public class RangeRequestForm {

    private Long workSheetId;
    private Long fieldId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RangeRequestForm that = (RangeRequestForm) o;
        return Objects.equals(workSheetId, that.workSheetId) &&
                Objects.equals(fieldId, that.fieldId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workSheetId, fieldId);
    }
}
