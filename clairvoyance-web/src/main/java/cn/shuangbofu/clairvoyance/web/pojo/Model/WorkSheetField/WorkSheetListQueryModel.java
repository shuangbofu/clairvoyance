package cn.shuangbofu.clairvoyance.web.pojo.Model.WorkSheetField;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date: 2020/11/3 11:05 上午
 */
@Data
public class WorkSheetListQueryModel {

    //工作表idList
    List<Long> workSheetIdList = new ArrayList<>();
}
