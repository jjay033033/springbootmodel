package top.lmoon.schedule;

import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务配置，添加多个定时任务若需要同时执行，注意修改此类下的线程池线程数
 * @author gzy
 * @date 2018年8月31日
 *
 */
@Configuration
public class ScheduleConfig implements SchedulingConfigurer {
	
	@Value("${schedule.thread.total}")
	private int total;
	
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {        
        taskRegistrar.setScheduler(Executors.newScheduledThreadPool(total));
    }
}
