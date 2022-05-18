package cn.boom.spider.nlp.config;

import cn.boom.spider.nlp.service.PredictScheduleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class PredictScheduleTask {

    @Resource
    private PredictScheduleService predictScheduleService;

    //直接指定时间间隔，每30秒执行一次
    @Scheduled(fixedRate = 30000)
    private void configureTasks() {
        predictScheduleService.execute();
    }
}
