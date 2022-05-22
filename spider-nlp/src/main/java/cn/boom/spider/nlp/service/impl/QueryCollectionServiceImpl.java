package cn.boom.spider.nlp.service.impl;

import cn.boom.spider.nlp.domain.CommonRecord;
import cn.boom.spider.nlp.domain.TaskRecord;
import cn.boom.spider.nlp.service.QueryCollectionService;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service
public class QueryCollectionServiceImpl implements QueryCollectionService {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 获取【集合名称】列表
     *
     * @return 集合名称列表
     */
    public Set<String> getCollectionNames() {
        // 执行获取集合名称列表
        return mongoTemplate.getCollectionNames();
    }

    /**
     * 检测集合【是否存在】
     *
     * @return 集合是否存在
     */
    public boolean collectionExists(String collectionName) {
        // 检测新的集合是否存在，返回检测结果
        return mongoTemplate.collectionExists(collectionName);
    }

    /**
     * 查询集合中的【全部】文档数据
     *
     * @return 全部文档列表
     */
    public List<CommonRecord> findAll(String collectionName) {
        // 执行查询集合中全部文档信息
        return mongoTemplate.findAll(CommonRecord.class, collectionName);
    }

    /**
     * 根据【文档ID】查询集合中文档数据
     *
     * @return 文档信息
     */
    public CommonRecord findById(String collectionName, String id) {
        // 根据文档ID查询集合中文档数据，并转换为对应 Java 对象
        return mongoTemplate.findById(id, CommonRecord.class, collectionName);
    }


    /**
     * 根据【条件】查询集合中【符合条件】的文档，获取其【文档列表】
     *
     * @return 符合条件的文档列表
     */
    public List<CommonRecord> findByTaskId(String collectionName, String taskId) {
        // 创建条件对象
        Criteria criteria = Criteria.where("taskId").is(taskId);
        // 查询并返回结果
        return mongoTemplate.find(new Query(criteria), CommonRecord.class, collectionName);
    }

    @Override
    public List<CommonRecord> findByEmptyNegative(String collectionName) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.orOperator(Criteria.where("negative").is(null), Criteria.where("keywords").is(null)));
        return mongoTemplate.find(query, CommonRecord.class, collectionName);
    }

    /**
     * 更新集合中【匹配】查询到的文档数据
     *
     * @return 执行更新的结果
     */
    public Long update(String collectionName, CommonRecord record) {
        CommonRecord before = findById(collectionName, record.getId());
        if (before == null) {
            return 0L;
        }
        Criteria criteria = Criteria.where("id").is(record.getId());
        Query query = new Query(criteria);
        Update update = new Update().set("negative", record.getNegative()).set("emotion", record.getEmotion()).set("keywords", record.getKeywords());
        UpdateResult result = mongoTemplate.upsert(query, update, CommonRecord.class, collectionName);
        // 输出结果信息
        return result.getMatchedCount();
    }

    /**
     * 清空集合情感分析、关键词摘要数据
     *
     * @return 执行更新的结果
     */
    public Long clean(String collectionName) {
        Query query = new Query();
        Update update = new Update().set("negative", null).set("emotion", null).set("keywords", null);
        UpdateResult result = mongoTemplate.updateMulti(query, update, CommonRecord.class, collectionName);
        // 输出结果信息
        return result.getMatchedCount();
    }

    @Override
    public List<TaskRecord> findTaskByStatus(String status) {
        Criteria criteria = Criteria.where("status").is(status);
        return mongoTemplate.find(new Query(criteria), TaskRecord.class, "tasks");
    }
}