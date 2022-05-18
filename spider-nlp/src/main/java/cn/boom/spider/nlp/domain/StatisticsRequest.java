package cn.boom.spider.nlp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class StatisticsRequest implements Serializable {
    private String taskId;
    private Boolean isSort; // 是否按照关键词数量降序排序
    private Integer num;    // 请求个数
}
