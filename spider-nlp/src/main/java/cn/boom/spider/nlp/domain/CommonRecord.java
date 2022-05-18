package cn.boom.spider.nlp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class CommonRecord {
    private String id;
    @Field("task_id")
    private String taskId;
    private String title;
    private String url;
    private Integer clicks;
    private Float negative;
    private String emotion;
    @Field("keywords")
    private List<String> keywords;
}
