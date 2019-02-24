package com.ai.platform.repository;

import com.ai.pojo.IndexDate;
import com.ai.pojo.Indexs;
import com.ai.pojo.Tail;
import net.sf.json.JSONObject;
import org.elasticsearch.search.SearchHit;

import java.net.UnknownHostException;
import java.util.List;

public interface TailRepository  {

    List<Tail> tailList () throws UnknownHostException ; //获取所有用户的列表

    List<Indexs> selectLog() throws UnknownHostException ;//根据指定索引文件名称获取对应的所有日志文件

    List getElkLogType() throws UnknownHostException;

    List<SearchHit> selectByIndex(String indexes) throws UnknownHostException;//根据索引名称、开始时间和结束时间进行查询

    List<SearchHit> selectByTime(IndexDate indexDate) throws UnknownHostException;//根据开始时间和结束时间进行查询

    List<SearchHit> selectRealTimeQuery(String indexes) throws UnknownHostException ;//实时查询
}
