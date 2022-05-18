package cn.boom.spider.nlp.service;

import cn.boom.spider.nlp.domain.StatisticsRequest;
import cn.boom.spider.nlp.domain.TaskStatisticsRecord;
import cn.boom.spider.nlp.domain.TaskStatisticsResult;

public interface QueryRedisService {
    public abstract TaskStatisticsResult getKeyWordsResult(StatisticsRequest request);
    public abstract void updateStatistics(TaskStatisticsRecord record);
}
