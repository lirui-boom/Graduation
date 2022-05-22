package cn.boom.spider.nlp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class KeyWord {
    private String content;
    private List<String> sources;
    private Integer totalCount;
    private Integer negativeCount;
    private Integer positiveCount;
    private Integer neutralCount;
}
