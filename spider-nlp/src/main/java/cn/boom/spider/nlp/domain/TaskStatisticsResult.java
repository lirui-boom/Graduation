package cn.boom.spider.nlp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class TaskStatisticsResult implements Serializable {
    private String taskId;
    private Integer totalCount;
    private Integer negativeCount;
    private Integer positiveCount;
    private Integer neutralCount;
    private KeyWordsResult keyWordsResult;// 关键词
}
