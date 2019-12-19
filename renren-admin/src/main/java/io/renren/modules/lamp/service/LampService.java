package io.renren.modules.lamp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.common.utils.PageUtils;
import io.renren.modules.lamp.communication.Server;
import io.renren.modules.lamp.entity.LampEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
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

    // 从集中器获得的信息修改数据库
    void  updateOnline (String num,  Integer online) ;

    void batchByIds(List<Integer> asList);

    void bacthLG220(List<Integer> asList, Server server);
}



