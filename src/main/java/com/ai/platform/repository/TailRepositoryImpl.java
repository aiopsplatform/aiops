package com.ai.platform.repository;


import com.ai.pojo.Tail;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.stereotype.Repository;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

@Repository
public class TailRepositoryImpl implements TailRepository{

    public static String clusterName = "cluster.name";
    public static String appName = "my-application";
    public static String inetAddr = "192.168.126.122";
    public static int clientPort = 9300;
    public static String elkIndex = "logstash-nginx-access-log";
    public static String elkType = "doc";
    public static Set set;
    public static Map map;


    //获取ELK客户端
    public static TransportClient getClient() throws UnknownHostException {
        //指定ES集群
        Settings settings = Settings.builder().put(clusterName, appName).build();
        //创建访问ES的客户端
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(inetAddr), clientPort));
        return client;
    }

    //查询ES中所有的索引
    private Map<Integer,String> getIndex() throws UnknownHostException{
        TransportClient client = getClient();
        ActionFuture<IndicesStatsResponse> isr = client.admin().indices().stats(new IndicesStatsRequest().all());
        Set<String> set = isr.actionGet().getIndices().keySet();

        Map<Integer ,String> map = new HashMap<>();
        int i =0;
        for(String set1: set ){
            map.put(i, set1);
            i++;
        }
        return map ;
    }

    //获取所有索引名称的方法
    @Override
    public List<Tail> tailList() throws UnknownHostException {

        Map map1 = this.getIndex();

        return new ArrayList<Tail>(map1.values());
    }


}
