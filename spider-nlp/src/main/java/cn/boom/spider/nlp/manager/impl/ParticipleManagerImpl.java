package cn.boom.spider.nlp.manager.impl;

import cn.boom.spider.nlp.manager.ParticipleManager;
import org.ansj.domain.Term;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.ansj.splitWord.analysis.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ParticipleManagerImpl implements ParticipleManager {

    @Override
    public List<String> participle(String content) {
        return IKParticiple(content);
    }

    private List<String> IKParticiple(String content) {
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

    private List<String> ANSJParticiple(String content) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }
        List<Term> terms = ToAnalysis.parse(content).getTerms();
        List<String> words = new ArrayList<>(terms.size());
        for (Term term : terms) {
            words.add(term.getName());
        }
        return words;
    }

    public static void main(String[] args) {
        ParticipleManagerImpl service = new ParticipleManagerImpl();
        String content = "哈利·波特，40岁生日快乐！\n" +
                "\n" +
                "1991年7月31日，11岁的哈利·波特收到一份特殊的生日礼物——霍格沃兹魔法学校的录取通知书，由此踏上他的魔法之旅……2020年7月31日，陪伴无数青少年长大的哈利迎来了他的40岁生日。 今年也是“哈利·波特”系列小说进入中国20周年，人民文学出版社推出《哈利·波特与魔法石》学院纪念版，包括格兰芬多、斯莱特林、赫奇帕奇和拉文克劳四个学院版本。 31日晚，该社将举办迄今为止最大型的线上直播暨哈利·波特学院杯争夺赛。与此同时，“哈利·波特”系列八部电影正在第23届上海国际电影节展映，第一部电影《哈利·波特与魔法石》4K修复3D版，定档8月14日在内地重映。";
        System.out.println(service.IKParticiple(content));
        System.out.println(service.ANSJParticiple(content));
    }
}


