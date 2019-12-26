package io.renren.modules.lamp.communication;

import io.renren.modules.lamp.service.LampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @Date 2019/12/24 19:32
 * @ 固定时间执行时定时任务，硬代码,
 *  废弃类
 */

@Component
public class ScheduledTasks {

    private int brightness;
    private int  firstTime;
    private int  secondTime;

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(int firstTime) {
        this.firstTime = firstTime;
    }

    public int getSecondTime() {
        return secondTime;
    }

    public void setSecondTime(int secondTime) {
        this.secondTime = secondTime;
    }

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private LampService lampService;
    @Autowired
    private Server server;

    String first =  "0 0 "+getFirstTime()+" * * ?";

    String second = "0 0 "+getSecondTime()+" * * ?";

//    @Scheduled(cron="0 35 12 * * ?") // 可恶，corn的参数只能是常量
    public void firstCommand() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
        command(100);
    }

//    @Scheduled(cron="0 0 6 * * ?")
    public void secondCommand() {
        System.out.println("现在时间：" + dateFormat.format(new Date()));
        command(0);

    }

    private void command(int brightness){
        System.out.println("定时执行亮度修改操作");
        // 执行亮度修改操作
        ArrayList<Integer> list =lampService.getLampIds();
        list.add(brightness);
        lampService.batchByIds(list);
        lampService.bacthLG220(list,server);
    }

}