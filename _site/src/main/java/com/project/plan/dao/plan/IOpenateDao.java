package com.project.plan.dao.plan;

import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.Openate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Barry on 2018/4/20.
 */
@Repository
public interface IOpenateDao extends IBaseDao<Openate, Integer> {

    //查询某个环节下面的所有记录
    @Query(value = " select  new Openate(o,u) from Openate o , User u where o.userId = u.id and o.tacheId = :tacheId ")
    List<Openate> findByTacheId(@Param("tacheId") Integer tacheId);

    @Modifying
    @Query("delete FROM Openate where tacheId in (:tacheIds) ")
    void deleteByTacheIds(@Param("tacheIds") Integer[] tacheIds);
}
