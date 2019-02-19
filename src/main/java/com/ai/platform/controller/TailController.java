package com.ai.platform.controller;

import com.ai.platform.repository.TailRepository;
import com.ai.pojo.Indexs;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/tail")
public class TailController {


    @Autowired
    private TailRepository tailRepository;

    //下拉框，查询所有索引文件的名称
    @GetMapping("/taillist")
    public String tailList() throws UnknownHostException {

        List ls = tailRepository.tailList();

        Gson gson = new Gson();

        String st = gson.toJson(ls);

        return st;

        //System.out.println(st);
    }


    //选定索引名称后，根据指定索引文件名称获取对应的所有日志文件
    @GetMapping("/{sysLog}")
    public String selectLog(@PathVariable String sysLog) throws UnknownHostException {

        List<Indexs> ls1 = tailRepository.selectLog();
        Gson gson1 = new Gson();
        String st1 = gson1.toJson(ls1);
        return st1;
    }


    //查询ElkLogType
    @GetMapping("/getElkLogType")
    public String getElkLogType() throws UnknownHostException{

        Gson gson2 = new Gson();
        List ls2 = tailRepository.getElkLogType();
        String type = gson2.toJson(ls2).replace("\\n", "");
        String type1 = type.replace("\\","");
        String type2 = type1.replace(" ", "");
        System.out.println(type2);
        return type2;
    }







//    @GetMapping("/taillist")
//    public String getIndex() {
//        Gson gson = new Gson();
//
//        Index index = new Index(1, "indexlog", "string", "logstash", "logstash");
//        Index index2 = new Index(2, "indexlog2", "string2", "nginx", "nginx");
//        Index index3 = new Index(3, "indexlog3", "string3", "Tomcat", "Tomcat");
//        List ls = new ArrayList();
//        ls.add(index);
//        ls.add(index2);
//        ls.add(index3);
//
//        String st = gson.toJson(ls);
//
//
//        return st;
//    }
}
