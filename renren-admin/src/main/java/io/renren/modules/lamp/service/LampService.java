package io.renren.modules.lamp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.lamp.entity.LampEntity;

import java.util.Map;

/**
 * 
 *
 * @author linton
 * @email lintonhank@gmail.com
 * @date 2019-12-17 15:04:40
 */
public interface LampService extends IService<LampEntity> {

    PageUtils queryPage(Map<String, Object> params);

    // 获取向NIO传送的数据
    String getData(LampEntity lamp);
}



