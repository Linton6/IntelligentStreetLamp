package io.renren.modules.lamp.dao;

import io.renren.modules.lamp.entity.LampEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * 
 * 
 * @author linton
 * @email lintonhank@gmail.com
 * @date 2019-12-17 15:04:40
 */
@Mapper
public interface LampDao extends BaseMapper<LampEntity> {

    void  updateOnline (@Param("num") String num, @Param("online") Integer online) ;

    // 批处理亮度为0
    void updateBatch0(@Param("ids")ArrayList<Integer> list);

    // 批处理亮度 > 0
    void updateBatch1(@Param("ids")ArrayList<Integer> list, @Param("brightness")int brightness);

    LampEntity judgeNum(@Param("Snum") String Snum);

    ArrayList<Integer> getLampIds();
}
