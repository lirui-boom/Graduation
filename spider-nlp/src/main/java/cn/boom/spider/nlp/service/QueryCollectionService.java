package cn.boom.spider.nlp.service;

import cn.boom.spider.nlp.domain.CommonRecord;
import cn.boom.spider.nlp.domain.TaskRecord;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface QueryCollectionService {

    public Set<String> getCollectionNames();

    public boolean collectionExists(String collectionName);

    public List<CommonRecord> findAll(String collectionName);

    public CommonRecord findById(String collectionName, String id);

    public List<CommonRecord> findByTaskId(String collectionName, String taskId);

    public List<CommonRecord> findByEmptyNegative(String collectionName);

    public Long update(String collectionName, CommonRecord record);

    public Long clean(String collectionName);

    public List<TaskRecord> findTaskByStatus(String status);
}