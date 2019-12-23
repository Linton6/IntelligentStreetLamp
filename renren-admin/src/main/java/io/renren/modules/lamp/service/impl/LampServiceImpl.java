package io.renren.modules.lamp.service.impl;

import io.renren.modules.lamp.communication.Server;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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
          String A1 = "25A38FC0";
          String A2 = lamp.getNum();
        // 数据长度
        String A3 = "01";
        // 有效数据
        Integer num = lamp.getBrightness() / 20;
        String A4;
        if (num == 10) {
            A4 = ""+num;
        } else {
            A4 = "0"+ num ;
        }

        return A1+A2+A3+A4;
    }

    @Override
    public void updateOnline(String num, Integer online) {
        baseMapper.updateOnline(num, online);
    }

    @Override
    public void batchByIds(List<Integer> asList) {
        int len = asList.size();
        int num = asList.get(len-1);
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < len -1; i++) {
            list.add(asList.get(i));
        }

        if (num == 0) { // 最后一位数为 0 status，brightness设为0
            baseMapper.updateBatch0(list);
        } else { // 最后一位数 为 > 0 status 设置为 1，brightness设为其值
            baseMapper.updateBatch1(list, num);
        }
    }

    @Override
    public void bacthLG220(List<Integer> asList, Server server) {
        int len = asList.size();
        int brightness = asList.get(len -1);
        LampEntity lamp;

        for (int i = 0; i < len -1; i++) {
            lamp = baseMapper.selectById(asList.get(i));
            String data  = getData(lamp);
            server.setData(data);
        }

    }

    @Override
    public LampEntity judgeNum(String num) {
        return baseMapper.judgeNum( num);
    }
}
