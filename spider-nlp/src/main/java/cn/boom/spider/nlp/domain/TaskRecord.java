package cn.boom.spider.nlp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@NoArgsConstructor
@Getter
@Setter
public class TaskRecord {
    private String id;
    @Field("spider_id")
    private String spiderId;
    @Field("result_count")
    private Integer resultCount;
    private String status;
}
