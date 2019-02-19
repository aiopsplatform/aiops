package com.ai.platform.repository;


import com.ai.pojo.Indexs;
import com.ai.pojo.LogType;
import com.ai.pojo.Tail;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsRequest;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
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

    //根据指定索引文件名称获取对应的所有日志文件
    @Override
    public List<Indexs> selectLog() throws UnknownHostException {
        List ls = new ArrayList();
        TransportClient client = getClient();
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse sr = client.prepareSearch(elkIndex).setQuery(qb).setQuery(qb).setScroll(TimeValue.timeValueMinutes(2)).get();
        SearchHits hits = sr.getHits();
        for (SearchHit hit : hits) {
            ls.add(hit);
            //System.out.println(hit.getSourceAsString());
        }
        return ls;
    }


    //根据指定的索引文件
    @Override
    public List getElkLogType() throws UnknownHostException{
        List ls = new ArrayList();
        TransportClient client = getClient();
        QueryBuilder qb = QueryBuilders.matchAllQuery();
        SearchResponse sr = client.prepareSearch("elk_log_type").setQuery(qb).setQuery(qb).setScroll(TimeValue.timeValueMinutes(2)).get();
        SearchHits hits = sr.getHits();
        String i = "\n";
        for (SearchHit hit : hits) {
            ls.add(hit.getSourceAsString());
            //map.put(i,hit.getSourceAsString());
            //System.out.println(hit.getSourceAsString());
            //System.out.println(ls);
        }

        return ls;
    }







}
