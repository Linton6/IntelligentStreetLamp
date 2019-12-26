package io.renren.modules.lamp.controller;

import java.util.Arrays;
import java.util.Map;

import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.lamp.communication.Server;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.modules.lamp.entity.LampEntity;
import io.renren.modules.lamp.service.LampService;
import io.renren.common.utils.PageUtils;
import io.renren.common.utils.R;



/**
 * 
 *
 * @author linton
 * @email lintonhank@gmail.com
 * @date 2019-12-17 15:04:40
 */
@RestController
@RequestMapping("lamp")
public class LampController {
    @Autowired
    private LampService lampService;
    @Autowired
    private Server server;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lampService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
        LampEntity lamp = lampService.getById(id);

        return R.ok().put("lamp", lamp);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody LampEntity lamp){
        lampService.save(lamp);

        return R.ok();
    }



    /**
     * 修改 / 向lamp发送控制信号，通过NIO
     */
    @RequestMapping("/update")
    public R update(@RequestBody LampEntity lamp){
        ValidatorUtils.validateEntity(lamp);

        if (lamp.getBrightness() == 0) {
            lamp.setStatus(0);
        }
        if (lamp.getStatus() == 0) {  // 调整亮度前，必须打开灯
            lamp.setBrightness(0);
        }
        lampService.updateById(lamp);
        // 获取需要向NIO传送的有效数据
        String data = lampService.getData(lamp);
        server.setData(data);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
        lampService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }
    /**
     * 批处理功能
     */
    @RequestMapping("/batch")
    public R batch(@RequestBody Integer[] ids) {  // ids中最后一位是亮度
        lampService.batchByIds(Arrays.asList(ids));// 数据库
        lampService.bacthLG220(Arrays.asList(ids), server);// 集中器

        return R.ok() ;
    }

    /**
     * 启动线程，开启服务器6060端口，与lora集中器通信
     */
    @RequestMapping("/server")
    public R server() {
        server.handler();
        return R.ok();
    }

}
