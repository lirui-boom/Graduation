package cn.boom.spider.nlp.manager.impl;


import cn.boom.spider.nlp.domain.WOD;
import cn.boom.spider.nlp.manager.ExtractManager;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

@Component
public class ExtractManagerImpl implements ExtractManager {

    private static float min_diff = 0.001f; //差值最小
    private static int max_iter = 200;//最大迭代次数
    private static int k = 2;  //窗口大小/2
    private static float d = 0.85f;
    private static Map<String, String> stopWords;

    static {
        stopWords = new HashMap<>();
        // 中文 停用词 .txt 文件路径
        String filePath = "/Users/bytedance/Desktop/graduation/spider-nlp/src/main/java/cn/boom/spider/nlp/data/cn_stopwords.txt";
        File file = new File(filePath);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) {
                stopWords.put(temp.trim(), temp.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> textRank(String field, int keywordNum) {
        //分词
        List<WOD<String>> wods = ToAnalysisParse(field);
        // StopWord.filter(wods);//过滤掉不需要的分词，可省略

        Map<String, Set<String>> relationWords = new HashMap<>();
        //获取每个关键词 前后k个的组合
        for (int i = 0; i < wods.size(); i++) {
            String keyword = wods.get(i).getName();
            Set<String> keySets = relationWords.computeIfAbsent(keyword, k1 -> new HashSet<>());
            for (int j = i - k; j <= i + k; j++) {
                if (!(j < 0 || j >= wods.size() || j == i)) {
                    keySets.add(wods.get(j).getName());
                }
            }
        }

        Map<String, Float> score = new HashMap<>();
        //迭代
        for (int i = 0; i < max_iter; i++) {
            Map<String, Float> m = new HashMap<>();
            float max_diff = 0;
            for (String key : relationWords.keySet()) {
                Set<String> value = relationWords.get(key);
                //先给每个关键词一个默认rank值
                m.put(key, 1 - d);
                //一个关键词的TextRank由其它成员投票出来
                for (String other : value) {
                    int size = relationWords.get(other).size();
                    if (!(key.equals(other) || size == 0)) {
                        m.put(key, m.get(key) + d / size * (score.get(other) == null ? 0 : score.get(other)));
                    }
                }
                max_diff = Math.max(max_diff, Math.abs(m.get(key) - (score.get(key) == null ? 0 : score.get(key))));
            }
            score = m;
            if (max_diff <= min_diff) {
//                System.out.println("迭代次数：" + i);
                break;
            }
        }
        List<WOD<String>> scores = new ArrayList<>();
        for (String s : score.keySet()) {
            WOD<String> score1 = new WOD(s, score.get(s));
            scores.add(score1);
        }

        scores.sort(new Comparator<WOD<String>>() {
            @Override
            public int compare(WOD<String> o1, WOD<String> o2) {
                return o1.compareTo(o2);
            }
        });
        List<String> keywords = new ArrayList<>();
        int index = 0;
        for (WOD<String> score1 : scores) {
            keywords.add(score1.getName());
            index++;
            if (index == keywordNum)
                break;
        }
        return keywords;
    }

    public static List<WOD<String>> ToAnalysisParse(String str) {
        List<WOD<String>> wods = new ArrayList<>();
        String[] strs = str.split(" ");
        for (String s : strs) {
            wods.add(new WOD(s));
        }
        return wods;
    }


    public static void main(String[] args) {
        String field = "5月17日，腾讯音乐娱乐集团在财报电话会中宣布，下一个视频号演唱会将为周杰伦演唱会，演唱会时间目前定为5月20日。此前，崔健曾在微信视频号举办线上演唱会“继续撒点野”，直播间点赞量超过1亿。";
//        String field = "哈利·波特，40岁生日快乐！\n" +
//                "\n" +
//                "1991年7月31日，11岁的哈利·波特收到一份特殊的生日礼物——霍格沃兹魔法学校的录取通知书，由此踏上他的魔法之旅……2020年7月31日，陪伴无数青少年长大的哈利迎来了他的40岁生日。 今年也是“哈利·波特”系列小说进入中国20周年，人民文学出版社推出《哈利·波特与魔法石》学院纪念版，包括格兰芬多、斯莱特林、赫奇帕奇和拉文克劳四个学院版本。 31日晚，该社将举办迄今为止最大型的线上直播暨哈利·波特学院杯争夺赛。与此同时，“哈利·波特”系列八部电影正在第23届上海国际电影节展映，第一部电影《哈利·波特与魔法石》4K修复3D版，定档8月14日在内地重映。";

        ParticipleManagerImpl participleManager = new ParticipleManagerImpl();
        StringBuilder input = new StringBuilder();
        List<String> participle = participleManager.participle(field);
        for (String s : participle) {
            input.append(s).append(" ");
        }
        new ExtractManagerImpl().filterStopWords(participle);
        List<String> keywords = textRank(input.toString(), 10);
        System.out.println("关键词：" + keywords);
    }

    @Override
    public List<String> filterStopWords(List<String> words) {
        List<String> res = new ArrayList<>(words.size());
        for (String word: words) {
            if (stopWords.get(word.trim()) != null) {
                continue;
            }
            res.add(word);
        }
        return res;
    }

    @Override
    public List<String> extract(String content) {
        return textRank(content, 3);
    }
}
