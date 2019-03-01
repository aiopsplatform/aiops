package com.ai.platform.controller;

import com.ai.platform.service.TailDao;
import com.ai.pojo.*;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/index")
public class TailController {


    @Autowired
    private TailDao tailDao;

    //下拉框，查询所有索引文件的名称
    @GetMapping("/indexAll")
    public String tailList() throws UnknownHostException {

        List ls = tailDao.tailList();

        Gson gson = new Gson();

        String st = gson.toJson(ls);

        return st;

        //System.out.println(st);
    }


    //使用假索引名称数据返回给前端
    @GetMapping("/indexList")
    public String getIndex() {
        Gson gson = new Gson();

        Index index = new Index(1, "indexlog", "string", "logstash", "logstash");
        Index index2 = new Index(2, "indexlog2", "string2", "nginx", "nginx");
        Index index3 = new Index(3, "indexlog3", "string3", "Tomcat", "Tomcat");
        List ls = new ArrayList();
        ls.add(index);
        ls.add(index2);
        ls.add(index3);

        String st = gson.toJson(ls);
        //System.out.println(st);

        return st;
    }


    //查询ElkLogType
    @GetMapping("/getElkLogType")
    public List getElkLogType() throws UnknownHostException {

        List elkLogTypeList = new ArrayList();

        Indexs indexs = new Indexs(0, "logstash-nginx-access-log");
        Indexs indexs1 = new Indexs(1, "filebeat-6.5.3-2019.01.23");
        Indexs indexs2 = new Indexs(2, ".kibana_1");

        elkLogTypeList.add(indexs);
        elkLogTypeList.add(indexs1);
        elkLogTypeList.add(indexs2);

        //System.out.println(elkLogTypeList);

        return elkLogTypeList;

    }


    /*
     * 接受前端传来的请求参数selectByIndex
     * @param   id -- 前端传递代表索引名称的字段
     *          使用post方式传递参数（@RequestParam注解，会以表单的形式接受数据）
     * @return
     * @throws UnknownHostException --端口未知异常
     *         @RequestParam(value = "id") int id
     */
    @PostMapping(value = "selectByIndex")
    @ResponseBody
    public String selectByIndex(@RequestBody JSONObject jsonObject) throws Exception {

        Gson selectGson = new Gson();


        //解析begin_time和end_time对应的开始时间
        String start_Time = jsonObject.get("begin_time").toString();
        String end_Time = jsonObject.get("end_time").toString();

        //解析索引名称对应的id
        String indexes = jsonObject.get("indexes").toString();

        IndexDate indexDate = new IndexDate(indexes, start_Time, end_Time);

        //将所有日志存放到list数组中
        List<SearchHit> selectIndexByTimeList = tailDao.selectByTime(indexDate);

        List list3 = new ArrayList();

        //需要将list转换成json格式
        for (SearchHit logMessage : selectIndexByTimeList) {
            String message = logMessage.getSourceAsMap().get("message").toString();
            list3.add(message);
        }
        //将list转换为json格式返回给前端
        String json = selectGson.toJson(list3);

        return json;
    }



    /**
     * 实时查询，每个一秒接受一个请求，从后台进行查询
     */
    @PostMapping(value = "selectRealTimeQuery")
    @ResponseBody
    public String selectRealTimeQuery(@RequestBody JSONObject jsonObject) throws UnknownHostException {

        Gson realTimeGson = new Gson();
        List realTimeList = new ArrayList();

        //解析索引名称对应的id
        String indexes = jsonObject.get("indexes").toString();

        List<SearchHit> selectRealTime = tailDao.selectRealTimeQuery(indexes);

        //需要将list转换成json格式
        for (SearchHit logMessage : selectRealTime) {
            String message = logMessage.getSourceAsMap().get("message").toString();
            realTimeList.add(message);
        }
        //将list转换为json格式返回给前端
        String realJson = realTimeGson.toJson(realTimeList);

        return realJson;

    }

    /**
     * 异常统计
     */
    @PostMapping(value = "exceptionCount")
    @ResponseBody
    public List exceptionCount(@RequestBody JSONObject jsonObject) throws Exception {

        /**
         * indexes -- 索引名称
         * type -- 索引类型
         * beginTime -- 开始时间
         * endTime -- 结束时间
         */
        String indexes = jsonObject.get("indexes").toString();
        String type = jsonObject.get("types").toString();
        String beginTime = jsonObject.get("begin_time").toString();
        String endTime = jsonObject.get("end_time").toString();

        System.out.println(indexes);
        System.out.println(type);
        System.out.println(beginTime);
        System.out.println(endTime);

        ExceptionCount exceptionCount = new ExceptionCount(indexes, type, beginTime, endTime);
        Map selectExceptionCount = tailDao.count(exceptionCount);
        List list = new ArrayList();
        for (Object key : selectExceptionCount.keySet()) {
            Object value = selectExceptionCount.get(key);
            ExceptionValue value1 = new ExceptionValue(key, value);
            list.add(value1);
        }
        return list;
    }


    /**
     * 慢统计
     */
    @PostMapping(value = "slowRequestCount")
    @ResponseBody
    public Map slowCount(@RequestBody JSONObject jsonObject) throws UnknownHostException{

        //从请求中获取对应字段的参数
        String index = jsonObject.get("indexes").toString();
        String beginTime = jsonObject.get("begin_time").toString();
        String endTime = jsonObject.get("end_time").toString();

        SlowCountBean slowCountBean = new SlowCountBean(index, beginTime, endTime);

        Map slowCountMap = tailDao.selectSlowCount(slowCountBean);

        JSONObject json = JSONObject.fromObject(slowCountMap);

        return json;
    }







}
