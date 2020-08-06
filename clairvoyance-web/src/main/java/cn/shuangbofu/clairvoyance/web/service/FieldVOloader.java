package cn.shuangbofu.clairvoyance.web.service;

import cn.shuangbofu.clairvoyance.core.db.Field;
import cn.shuangbofu.clairvoyance.core.loader.FieldLoader;
import cn.shuangbofu.clairvoyance.web.vo.FieldSimpleVO;

import java.util.List;

/**
 * Created by shuangbofu on 2020/8/6 15:00
 */
public class FieldVOloader {

    public static List<FieldSimpleVO> getOriginFields(Long workSheetId) {
        List<Field> originFields = FieldLoader.getOriginFields(workSheetId);
        return FieldSimpleVO.toVOs(originFields);
    }
}
