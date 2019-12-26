package io.renren.modules.lamp.communication;

import io.renren.modules.job.task.ITask;
import io.renren.modules.lamp.service.LampService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.logging.Logger.*;

/**
 * @Date 2019/12/25 12:52
 * @ 自定义定时任务，软代码
 * 采用类
 */
@Component("timingTask")
public class TimingTask  implements ITask {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private LampService lampService;
    @Autowired
    private Server server;


    @Override
    public void run(String params) {
        // 传递进来的是亮度
        int brightness = Integer.valueOf(params);

        logger.debug("TimingTask定时任务正在执行，亮度参数为{}",params);
        command(brightness);

    }

    private void command(int brightness){
        System.out.println("定时执行亮度修改操作");
        // 执行亮度修改操作
        ArrayList<Integer> list =lampService.getLampIds();
        list.add(brightness);
        lampService.bacthLG220(list,server);
        lampService.batchByIds(list);
    }
}

