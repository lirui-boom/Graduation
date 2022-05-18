package cn.boom.spider.nlp.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class ExecuteInfo {
    private Date beginTime;
    private Date endTime;
}
