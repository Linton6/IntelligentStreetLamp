package io.renren.modules.lamp.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.Query;

import io.renren.modules.lamp.dao.LampDao;
import io.renren.modules.lamp.entity.LampEntity;
import io.renren.modules.lamp.service.LampService;


@Service("lampService")
public class LampServiceImpl extends ServiceImpl<LampDao, LampEntity> implements LampService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LampEntity> page = this.page(
                new Query<LampEntity>().getPage(params),
                new QueryWrapper<LampEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public String getData(LampEntity lamp) {
        // 网关ID，节点ID
        String A12 = "";
        // 数据长度
        String A3 = "";
        // 有效数据
        String A4 = lamp.getName()+ "-" + lamp.getStatus()+ "-" + lamp.getBrightness();

        return A12+A3+A4;
    }

}
