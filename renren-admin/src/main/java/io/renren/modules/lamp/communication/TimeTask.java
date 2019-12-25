package io.renren.modules.lamp.communication;

import io.renren.modules.lamp.service.LampService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Date 2019/12/23 10:04
 * @ Java原生定时任务Timer+TimerTask，远古方法，十分不推荐
 */
@Service("timeTask")
public class TimeTask {


    Timer timer;

    public TimeTask(){

    }

    public TimeTask(Integer hour, Integer minute, Integer brightness){
        Date time = getTime(hour,minute);
        System.out.println("指定时间time=" + time);
        timer = new Timer();
        timer.schedule(new TimerTaskBatch(brightness), time); // 大哥，这部递归了吗
    }

    private Date getTime(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 00);
        Date time = calendar.getTime();

        return time;
    }

    public static void main(String[] args) {
        new TimeTask(11,39,70);
    }
}

class TimerTaskBatch extends TimerTask{
    @Autowired
    private LampService lampService;
    @Autowired
    private Server server;

    Integer brightness;

    public TimerTaskBatch( Integer brightness) {
        this.brightness = brightness;
    }

    @Override
    public void run() {
        System.out.println("指定时间执行线程任务...");
        // 执行亮度修改操作
        ArrayList<Integer>  list =lampService.getLampIds();
        list.add(brightness);
        lampService.batchByIds(list);
        lampService.bacthLG220(list,server); // 此处存疑问，server能否是与controller层同一个server

    }
}


