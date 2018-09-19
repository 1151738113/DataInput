package com.yzc.dao;

import com.yzc.model.Tsmnoticesendlogs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by wei.wang on 2018/9/12 0012.
 * 数据库查询
 */
@Repository
public interface INotifyInputMapper {

    /**
     * 分页查询
     * @param offSet
     * @param size
     * @return
     */
    List<Tsmnoticesendlogs> getNotify(@Param("offSet") int offSet, @Param("size") int size);

    /**
     * 获得总数
     * @return
     */
    long getCount();


}
