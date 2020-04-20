package com.cb.pmall.manage.mapper;

import com.cb.pmall.beans.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author 陈彬
 * date 2020/3/31 3:00 PM
 */
public interface PmsBaseAttrInfoMapper extends Mapper<PmsBaseAttrInfo> {
    List<PmsBaseAttrInfo> selectAttrInfoByValueId(@Param("valueIdStr") String valueIdStr);
}
