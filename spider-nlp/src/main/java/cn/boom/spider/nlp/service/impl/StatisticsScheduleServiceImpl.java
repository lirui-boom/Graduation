package cn.boom.spider.nlp.service.impl;

import cn.boom.spider.nlp.domain.*;
import cn.boom.spider.nlp.manager.ExtractManager;
import cn.boom.spider.nlp.manager.ParticipleManager;
import cn.boom.spider.nlp.service.QueryCollectionService;
import cn.boom.spider.nlp.service.QueryRedisService;
import cn.boom.spider.nlp.service.StatisticsScheduleService;
import cn.boom.spider.nlp.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class StatisticsScheduleServiceImpl implements StatisticsScheduleService {

    @Autowired
    private ExtractManager extractManager;

    @Autowired
    private QueryRedisService queryRedisService;

    @Autowired
    private QueryCollectionService queryCollectionService;

    @Autowired
    private ParticipleManager participleManager;

    private Map<String, ExecuteInfo> executeTaskMap = new ConcurrentHashMap<>();

    @Override
    public void execute() {
        System.out.println("[statistics] task begin to run. [executeTaskMap] = " + JsonUtils.toString(executeTaskMap));
        long start = System.currentTimeMillis();
        int count = 0;
        List<TaskRecord> finished = queryCollectionService.findTaskByStatus("finished");

        if (!CollectionUtils.isEmpty(finished)) {
            // 过滤不需要统计的task
            List<TaskRecord> execute = finished.stream().filter(record -> {
                return executeTaskMap.get(record.getId()) == null;
            }).collect(Collectors.toList());

            execute.forEach(record -> {
                ExecuteInfo info = new ExecuteInfo();
                Date begin = new Date();
                boolean ok = calculate(record);
                Date over = new Date();
                info.setBeginTime(begin);
                info.setEndTime(over);
                if (ok) {
                    executeTaskMap.put(record.getId(), info);
                }
            });
        }
        long end = System.currentTimeMillis();
        System.out.println("[statistics] task exec done. handler count: " + count + ", cost: " + (end - start) + "ms.");
    }

    private boolean calculate(TaskRecord task) {
        List<CommonRecord> records = new ArrayList<>();
        for (String collection : queryCollectionService.getCollectionNames()) {
            records.addAll(queryCollectionService.findByTaskId(collection, task.getId()));
        }
        if (CollectionUtils.isEmpty(records)) {
            return false;
        }
        TaskStatisticsRecord statistics = buildTaskStatisticsInfo(task, records);
        if (statistics == null) {
            return false;
        }
        System.out.println("save calculate info : " + JsonUtils.toString(statistics));
        queryRedisService.updateStatistics(statistics);
        return true;
    }

    private TaskStatisticsRecord buildTaskStatisticsInfo(TaskRecord task, List<CommonRecord> records) {
        int invalidCount = 0, negativeCount = 0, positiveCount = 0, neutralCount = 0;
        Map<String, KeyWord> keyWordsMap = new HashMap<>();
        List<KeyWord> keyWords = new ArrayList<>();
        for (CommonRecord record : records) {
            if (StringUtils.isEmpty(record.getEmotion())) {
                invalidCount++;
                continue;
            }
            switch (record.getEmotion()) {
                case "Negative":
                    negativeCount++;
                    if (!CollectionUtils.isEmpty(record.getKeywords())) {
                        for (String word : record.getKeywords()) {
                            if (keyWordsMap.get(word) == null) {
                                keyWordsMap.put(word, new KeyWord(word, 1, 1, 0, 0));
                            } else {
                                KeyWord keyWord = keyWordsMap.get(word);
                                keyWord.setTotalCount(keyWord.getTotalCount() + 1);
                                keyWord.setNegativeCount(keyWord.getNegativeCount() + 1);
                                keyWordsMap.put(word, keyWord);
                            }
                        }
                    }
                    break;
                case "Positive":
                    positiveCount++;
                    if (!CollectionUtils.isEmpty(record.getKeywords())) {
                        for (String word : record.getKeywords()) {
                            if (keyWordsMap.get(word) == null) {
                                keyWordsMap.put(word, new KeyWord(word, 1, 0, 1, 0));
                            } else {
                                KeyWord keyWord = keyWordsMap.get(word);
                                keyWord.setTotalCount(keyWord.getTotalCount() + 1);
                                keyWord.setPositiveCount(keyWord.getPositiveCount() + 1);
                                keyWordsMap.put(word, keyWord);
                            }
                        }
                    }
                    break;
                case "Neutral":
                    neutralCount++;
                    if (!CollectionUtils.isEmpty(record.getKeywords())) {
                        for (String word : record.getKeywords()) {
                            if (keyWordsMap.get(word) == null) {
                                keyWordsMap.put(word, new KeyWord(word, 1, 0, 0, 1));
                            } else {
                                KeyWord keyWord = keyWordsMap.get(word);
                                keyWord.setTotalCount(keyWord.getTotalCount() + 1);
                                keyWord.setNeutralCount(keyWord.getNeutralCount() + 1);
                                keyWordsMap.put(word, keyWord);
                            }
                        }
                    }
                    break;
            }
        }
        if (1 - invalidCount / (double) records.size() < 0.9) {
            System.out.println("invalid calculation task because valid record rate < 0.9 [spider]=" + task.getSpiderId() + ", [task]=" + task.getId() + ", [invalidCount]=" + invalidCount + ", [total]=" + records.size() + ", [rate]=" + (1 - invalidCount / (double) records.size()));
            return null;
        }
        for (String key : keyWordsMap.keySet()) {
            keyWords.add(keyWordsMap.get(key));
        }
        TaskStatisticsRecord statistics = new TaskStatisticsRecord();
        statistics.setTaskId(task.getId());
        statistics.setTotalCount(records.size());
        statistics.setNegativeCount(negativeCount);
        statistics.setPositiveCount(positiveCount);
        statistics.setNeutralCount(neutralCount);
        statistics.setKeyWords(keyWords);
        return statistics;
    }
}
