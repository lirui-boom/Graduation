package cn.boom.spider.nlp.manager;

import java.util.List;

public interface ExtractManager {
    public abstract List<String> filterStopWords(List<String> content);
    public abstract List<String> extract(String content);
}
