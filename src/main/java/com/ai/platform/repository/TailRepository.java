package com.ai.platform.repository;

import com.ai.pojo.Indexs;
import com.ai.pojo.Tail;

import java.net.UnknownHostException;
import java.util.List;

public interface TailRepository  {

    List<Tail> tailList () throws UnknownHostException ; //获取所有用户的列表

    List<Indexs> selectLog() throws UnknownHostException ;//根据指定索引文件名称获取对应的所有日志文件

    List getElkLogType() throws UnknownHostException;

    List selectByIndex(String indexes) throws UnknownHostException;
}
