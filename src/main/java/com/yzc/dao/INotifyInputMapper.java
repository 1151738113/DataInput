package com.yzc.dao;

import com.yzc.model.Tsmnoticesendlogs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wei.wang on 2018/9/12 0012.
 * 数据库查询  '2018-09-11 14:24:36'
 */
@Repository
public interface INotifyInputMapper {

    /**
     * 分页查询
     * @param offSet
     * @param size
     * @return
     */
    List<Tsmnoticesendlogs> getNotify(@Param("offSet") int offSet, @Param("size") int size, @Param("time") String time);

    /**
     * 获得总数
     * @return
     */
    long getCount(@Param("time") String time);


}
