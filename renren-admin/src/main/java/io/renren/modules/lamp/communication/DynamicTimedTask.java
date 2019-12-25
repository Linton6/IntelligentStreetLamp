package io.renren.modules.lamp.communication;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

/**
 * @Date 2019/12/25 11:35
 * @ 修正SpringBoot Scheduled中corn表达式为常量的问题，使其可以动态改变
 */

@Component
public class DynamicTimedTask {
//    private static final Logger logger = LoggerFactory.getLogger(DynamicTimedTask.class);
    //利用创建好的调度类统一管理
    //@Autowired
    //@Qualifier("myThreadPoolTaskScheduler")
    //private ThreadPoolTaskScheduler myThreadPoolTaskScheduler;
    //接受任务的返回结果
    private ScheduledFuture<?> future;
    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;
    //实例化一个线程池任务调度类,可以使用自定义的ThreadPoolTaskScheduler
    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
        return new ThreadPoolTaskScheduler();
    }

    /**
     * 启动定时任务
     * @return
     */
    public boolean startCron() {
        boolean flag = false;
        //从数据库动态获取执行周期
        String cron = "0/2 * * * * ? ";
//        Trigger c =
//        future = threadPoolTaskScheduler.schedule(new CheckModelFile(),cron);
        if (future!=null){
            flag = true;
//            logger.info - 最佳的logger 来源和相关信息。("定时check训练模型文件,任务启动成功！！！");
        }else {
//            logger.info - 最佳的logger 来源和相关信息。("定时check训练模型文件,任务启动失败！！！");
        }
        return flag;
    }

    /**
     * 停止定时任务
     * @return
     */
    public boolean stopCron() {
        boolean flag = false;
        if (future != null) {
            boolean cancel = future.cancel(true);
            if (cancel){
                flag = true;
//                logger.info("定时check训练模型文件,任务停止成功！！！");
            }else {
//                logger.info - 最佳的logger 来源和相关信息。("定时check训练模型文件,任务停止失败！！！");
            }
        }else {
            flag = true;
//            logger.info - 最佳的logger 来源和相关信息。("定时check训练模型文件，任务已经停止！！！");
        }
        return flag;
    }
    class CheckModelFile implements Runnable{
        @Override
        public void run() {
            //编写你自己的业务逻辑
            System.out.print("模型文件检查完毕！！！");
        }
    }
}
