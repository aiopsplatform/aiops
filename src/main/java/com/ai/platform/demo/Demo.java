package com.ai.platform.demo;

import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Demo {

    public static String clusterName = "cluster.name";
    public static String appName = "my-application";
    public static String inetAddr = "192.168.126.122";
    public static int clientPort = 9300;
    public static String elkIndex = "logstash-nginx-access-log";
    public static String elkType = "doc";

    //获取ELK客户端
    public static TransportClient getClient() throws UnknownHostException {
        //指定ES集群
        Settings settings = Settings.builder().put(clusterName, appName).build();
        //创建访问ES的客户端
        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName(inetAddr), clientPort));
        return client;
    }

    public static void main(String[] args) throws Exception{

        TransportClient client = getClient();

        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                .field("0", "web")
                .endObject();

        XContentBuilder builder1 = XContentFactory.jsonBuilder()
                .startObject()
                .field("1", "nginx")
                .endObject();

        XContentBuilder builder2 = XContentFactory.jsonBuilder()
                .startObject()
                .field("2", "tomcat")
                .endObject();

        IndexResponse response = client.prepareIndex("elk_log_type", "info").setSource(builder).get();
        IndexResponse response1 = client.prepareIndex("elk_log_type", "info").setSource(builder1).get();
        IndexResponse response2 = client.prepareIndex("elk_log_type", "info").setSource(builder2).get();


    }

}
