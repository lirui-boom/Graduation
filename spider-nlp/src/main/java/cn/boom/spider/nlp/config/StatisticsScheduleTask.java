package cn.boom.spider.nlp.config;

import cn.boom.spider.nlp.service.StatisticsScheduleService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class StatisticsScheduleTask {

    @Resource
    private StatisticsScheduleService statisticsScheduleService;

    //直接指定时间间隔，每120秒执行一次
    @Scheduled(fixedRate = 120000)
    private void configureTasks() {
        statisticsScheduleService.execute();
    }
}
