package com.ai.platform.controller;

import com.ai.platform.repository.TailRepository;
import com.ai.pojo.Index;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import netscape.javascript.JSObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tail")
public class TailController {


    @Autowired
    private TailRepository tailRepository;

    //查询所有索引文件的名称
//    @GetMapping("/taillist")
//    public List tailList() throws UnknownHostException {
//
//        List ls = tailRepository.tailList();
//
//        //System.out.println(st);
//
//        return ls;
//    }



    @GetMapping("/taillist")
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


        return st;
    }
}
