package cn.boom.spider.nlp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class KeyWordsResult implements Serializable {
    private Integer total; // 关键词总数
    private Boolean isSort; // 是否按照关键词数量降序排序
    private Integer num;    // 请求个数
    private List<KeyWord> keyWords;// 关键词
}
