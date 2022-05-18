package cn.boom.spider.nlp.controller;

import cn.boom.spider.nlp.domain.R;
import cn.boom.spider.nlp.domain.StatisticsRequest;
import cn.boom.spider.nlp.service.QueryRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spider-nlp/statistics")
public class StatisticsController {

    @Autowired
    private QueryRedisService queryRedisService;

    @PostMapping("/get")
    public R getNid(@RequestBody StatisticsRequest request) {
        return R.ok().put("data", queryRedisService.getKeyWordsResult(request));
    }
}
