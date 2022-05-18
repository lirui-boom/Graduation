package cn.boom.spider.nlp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TaskStatisticsRecord {
    private String taskId;
    private Integer totalCount;
    private Integer negativeCount;
    private Integer positiveCount;
    private Integer neutralCount;
    private List<KeyWord> keyWords;// 关键词
}
