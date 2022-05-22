package cn.boom.spider.nlp.service.impl;

import cn.boom.spider.nlp.domain.CommonRecord;
import cn.boom.spider.nlp.manager.ExtractManager;
import cn.boom.spider.nlp.manager.ParticipleManager;
import cn.boom.spider.nlp.manager.PredictModelManager;
import cn.boom.spider.nlp.service.PredictScheduleService;
import cn.boom.spider.nlp.service.QueryCollectionService;
import cn.boom.spider.nlp.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PredictScheduleServiceImpl implements PredictScheduleService {

    @Autowired
    private QueryCollectionService queryCollectionService;

    @Autowired
    private PredictModelManager predictModelManager;

    @Autowired
    private ParticipleManager participleManager;

    @Autowired
    private ExtractManager extractManager;

    // 最大任务处理个数
    private static final int MAX_HANDLER_COUNT = 50;

    @Override
    public void execute() {
        System.out.println("[predict] task begin to run.");
        long start = System.currentTimeMillis();
        int count = 0;
        for (String collection : queryCollectionService.getCollectionNames()) {
            // 查询未被分析的数据
            List<CommonRecord> records = queryCollectionService.findByEmptyNegative(collection);
            if (records == null || records.size() == 0) {
                continue;
            }
            for (CommonRecord record : records) {
                // 1.分词
                if (StringUtils.isEmpty(record.getTitle())) {
                    continue;
                }
                List<String> participle = participleManager.participle(record.getTitle());
                // 2.过滤停用词
                participle = extractManager.filterStopWords(participle);
                StringBuilder content = new StringBuilder();
                for (String s : participle) {
                    content.append(s).append(" ");
                }
                // 3.计算消极得分
                float negativeScore = predictModelManager.predictNegative(content.toString());
                // 4.标记情感偏向
                String emotion = "";
                if (negativeScore >= 0.7) {
                    emotion = "Negative";
                } else if (negativeScore <= 0.3) {
                    emotion = "Positive";
                } else {
                    emotion = "Neutral";
                }
                // 5.关键词提取
                List<String> keywords = extractManager.extract(content.toString());
                System.out.println(", keywords: " + JsonUtils.toString(keywords));
                record.setNegative(negativeScore);
                record.setEmotion(emotion);
                record.setKeywords(keywords);
                // 更新mongo数据
                queryCollectionService.update(collection, record);
                if (++count > MAX_HANDLER_COUNT) {
                    long end = System.currentTimeMillis();
                    System.out.println("[predict] task interrupt. max handler count : " + MAX_HANDLER_COUNT + ", cost: " + (end - start) + "ms.");
                    return;
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("[predict] task exec done. handler count: " + count + ", cost: " + (end - start) + "ms.");
    }

}

