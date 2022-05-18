package cn.boom.spider.nlp.service.impl;

import cn.boom.spider.nlp.domain.*;
import cn.boom.spider.nlp.service.QueryRedisService;
import cn.boom.spider.nlp.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QueryRedisServiceImpl implements QueryRedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public TaskStatisticsResult getKeyWordsResult(StatisticsRequest request) {
        String data = (String) redisTemplate.opsForHash().get("statistics", request.getTaskId());
        TaskStatisticsRecord statistics = JsonUtils.toBean(data, TaskStatisticsRecord.class);

        TaskStatisticsResult ret = new TaskStatisticsResult();
        ret.setTaskId(request.getTaskId());

        KeyWordsResult result = new KeyWordsResult();
        result.setNum(request.getNum());
        result.setIsSort(request.getIsSort());

        if (statistics == null) {
            return null;
        }
        ret.setTotalCount(statistics.getTotalCount());
        ret.setNegativeCount(statistics.getNegativeCount());
        ret.setPositiveCount(statistics.getPositiveCount());
        ret.setNeutralCount(statistics.getNeutralCount());

        if (!CollectionUtils.isEmpty(statistics.getKeyWords())) {
            List<KeyWord> keyWords = statistics.getKeyWords();
            result.setTotal(keyWords.size());
            if (request.getIsSort() == Boolean.TRUE) {
                keyWords.sort(new Comparator<KeyWord>() {
                    @Override
                    public int compare(KeyWord o1, KeyWord o2) {
                        return o2.getTotalCount() - o1.getTotalCount();
                    }
                });
            }
            result.setKeyWords(keyWords.stream().limit(result.getNum()).collect(Collectors.toList()));
            ret.setKeyWordsResult(result);
            return ret;
        }
        result.setTotal(0);
        result.setKeyWords(null);
        ret.setKeyWordsResult(result);
        return ret;
    }

    @Override
    public void updateStatistics(TaskStatisticsRecord record) {
        redisTemplate.opsForHash().put("statistics", record.getTaskId(), JsonUtils.toString(record));
    }
}
