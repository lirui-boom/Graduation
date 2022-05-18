package cn.boom.spider.nlp.manager.impl;

import cn.boom.spider.nlp.manager.ParticipleManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParticipleManagerImpl implements ParticipleManager {

    @Override
    public List<String> participle(String content) {
        List<String> res = new ArrayList<>();
        try {
            Analyzer anal = new IKAnalyzer(true);
            StringReader reader = new StringReader(content);
            //分词
            TokenStream ts = anal.tokenStream("", reader);
            ts.reset();
            CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
            //遍历分词数据
            while (ts.incrementToken()) {
                res.add(term.toString());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        ParticipleManagerImpl service = new ParticipleManagerImpl();
        System.out.println(service.participle("你好，我的世界!"));
    }
}


