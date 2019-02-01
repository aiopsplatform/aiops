package com.ai.platform.repository;

import com.ai.pojo.Tail;

import java.net.UnknownHostException;
import java.util.List;

public interface TailRepository  {

    List<Tail> tailList () throws UnknownHostException; //获取所有用户的列表

}
