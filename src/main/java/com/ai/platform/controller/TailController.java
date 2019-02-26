package com.ai.platform.controller;

import com.ai.platform.service.TailDao;
import com.ai.pojo.IndexDate;
import com.ai.pojo.Index;
import com.ai.pojo.Indexs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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

        Gson gson2 = new Gson();
        List ElkLogTypeList = new ArrayList();
        Indexs indexs = new Indexs(0, "logstash-nginx-access-log");
        Indexs indexs1 = new Indexs(1, "filebeat-6.5.3-2019.01.23");
        Indexs indexs2 = new Indexs(2, ".kibana_1");
        ElkLogTypeList.add(indexs);
        ElkLogTypeList.add(indexs1);
        ElkLogTypeList.add(indexs2);
        return ElkLogTypeList;


//        System.out.println(ls2.get(0).getClass);
//
//        Indexs indexs1 = new Indexs(0, ls2.get(0).getClass().getName());
//        List ls = new ArrayList();
//        ls.add(indexs1);
//
//        String st = gson2.toJson(ls);
//        Indexs indexs2 = new Indexs(1, ls2.get(1).toString());
//        Indexs indexs3 = new Indexs(2, ls2.get(1).toString());

//        System.out.println(ls2);
//        String type = gson2.toJson(ls2).replace("\\n", "");
//        String type1 = type.replace("\\","");
//        String type2 = type1.replace(" ", "");
//        String type3 = type2.replace("\"","");
        //return ls2;
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

//        //System.out.println(selectByIndexList);
//
//        //List<SearchHit> selectByTime = tailDao.selectByTime(start_Time, end_Time);
//
//
//        List list2 = new ArrayList();
////        for (SearchHit logMessage  : selectByTime){
////            String message = logMessage.getSourceAsMap().get("message").toString();
////            list2.add(message);
////            String allJson = selectGson.toJson(list2);
////            System.out.println(allJson);
////        }
//
//        //日志为多条，只取出第一条日志
//        Object list1 = selectByIndexList.get(0);
//        //System.out.println(list1);
//        //将list转化为string字符串
//        logs = list1.toString();
//        //json第一层
//        JSONObject pa = JSONObject.fromObject(logs);
//        String _source = pa.getString("_source");
//        //json第二层
//        JSONObject log_source = JSONObject.fromObject(_source);
//        //直接通过原始json获取字段
//        String _index = pa.getString("_index");
//        String _type = pa.getString("_type");
//        String _id = pa.getString("_id");
//        //通过原始json中_source中获取字段
//        String remote_ip = log_source.getString("remote_ip");
//        String time = log_source.getString("time");
//        String http_version = log_source.getString("http_version");
//        String logtype = log_source.getString("logtype");
//        String requestMessage = log_source.getString("request");
//        String request_action = log_source.getString("request_action");
//        String message = log_source.getString("message");
//        String user_name = log_source.getString("user_name");
//        Log selectLog = new Log(_index, _type, _id, remote_ip, time, http_version, logtype, requestMessage, request_action, message, user_name);
//        List logList = new ArrayList();
//        logList.add(selectLog);
//        jsonLog = selectGson.toJson(logList);
//        String responseBody = jsonLog.replace("[", "");
//        String finallyResponse = responseBody.replace("]", "");
//        //System.out.println(finallyResponse);
//        return finallyResponse;

    }

    /**
     * 该方法是使用时间作为范围查询的条件
     *
     * @param jsonObject
     * @return
     * @throws Exception
     */
    @PostMapping("selectByTime")
    @ResponseBody
    public String selectByTime(@RequestParam JSONObject jsonObject) throws Exception {

        //解析indexes对应的id
        String indexes = jsonObject.get("indexes").toString();
        //解析begin_time对应的开始时间
        String startTime = jsonObject.get("begin_time").toString();
        //解析end_time对应的结束时间
        String endTime = jsonObject.get("end_time").toString();
        IndexDate indexDate = new IndexDate(indexes, startTime, endTime);
        List<SearchHit> selectIndexByTimeList = tailDao.selectByTime(indexDate);
        Map<String, String> map = new HashMap();
        //Gson selectGson = new Gson();
        String json = null;
        //需要将map转换成json格式
        for (SearchHit logMessage : selectIndexByTimeList) {
            String message = logMessage.getSourceAsMap().get("message").toString();
            map.put("message", message);
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(map);
        }
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
    public String exceptionCount(@RequestBody JSONObject jsonObject) {


        return null;
    }

}
