package cn.boom.spider.nlp.manager.impl;


import cn.boom.spider.nlp.domain.WOD;
import cn.boom.spider.nlp.manager.ExtractManager;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ExtractManagerImpl implements ExtractManager {

    private static float min_diff = 0.001f; //差值最小
    private static int max_iter = 200;//最大迭代次数
    private static int k = 2;  //窗口大小/2
    private static float d = 0.85f;

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
        ParticipleManagerImpl participleManager = new ParticipleManagerImpl();
        StringBuilder input = new StringBuilder();
        for (String s : participleManager.participle(field)) {
            input.append(s).append(" ");
        }
        List<String> keywords = textRank(input.toString(), 10);
        System.out.println("关键词：" + keywords);
    }

    @Override
    public List<String> extract(String content) {
        return textRank(content, 3);
    }
}
