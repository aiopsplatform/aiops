package com.ai.platform.controller;

import com.ai.platform.repository.TailRepository;
import com.ai.pojo.Index;
import com.ai.pojo.Indexs;
import com.ai.pojo.Log;
import com.google.gson.Gson;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/index",method = RequestMethod.GET)
public class TailController {


    @Autowired
    private TailRepository tailRepository;

    //下拉框，查询所有索引文件的名称
    @GetMapping("/indexAll")
    public String tailList() throws UnknownHostException {

        List ls = tailRepository.tailList();

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
    public List getElkLogType() throws UnknownHostException{

        Gson gson2 = new Gson();
        //List ls2 = tailRepository.getElkLogType();
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
    public String selectByIndex(HttpServletRequest request) throws UnknownHostException{
        Gson selectGson = new Gson();

        String logs;
        String jsonLog = null;
        try {
            String indexes = request.getParameter("indexes");

            List selectByIndexList = tailRepository.selectByIndex(indexes);

            Object list1 = selectByIndexList.get(0);

            //将list转化为string字符串
            logs = list1.toString();
            //System.out.println(logs);

            //json第一层
            JSONObject pa = JSONObject.fromObject(logs);
            String _source = pa.getString("_source");

            //json第二层
            JSONObject log_source = JSONObject.fromObject(_source);

            //直接通过原始json获取字段
            String _index = pa.getString("_index");
            String _type = pa.getString("_type");
            String _id = pa.getString("_id");

            //通过原始json中_source中获取字段
            String remote_ip = log_source.getString("remote_ip");
            String timestamp = log_source.getString("@timestamp");
            String http_version = log_source.getString("http_version");
            String logtype = log_source.getString("logtype");
            String requestMessage = log_source.getString("request");
            String request_action = log_source.getString("request_action");
            String message = log_source.getString("message");
            String user_name = log_source.getString("user_name");

            Log selectLog = new Log(_index, _type, _id, remote_ip, timestamp, http_version, logtype, requestMessage, request_action, message, user_name);

            List logList = new ArrayList();
            logList.add(selectLog);

            jsonLog = selectGson.toJson(logList);

            //System.out.println(jsonLog);



        }catch (Exception e){
            e.printStackTrace();
        }



        return jsonLog;

    }





//    //根据前端传来的指定索引文件名称获取对应的所有日志文件
//    @GetMapping("/")
//    public String selectLog(@PathVariable String sysLog) throws UnknownHostException {
//
//        List<Indexs> ls1 = tailRepository.selectLog();
//        Gson gson1 = new Gson();
//        String st1 = gson1.toJson(ls1);
//        return st1;
//    }

}
