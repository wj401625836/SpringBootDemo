package com.project.plan.dao.plan;

import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.Project;
import com.project.plan.entity.plan.Tache;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Barry on 2018/4/20.
 */
@Repository
public interface ITacheDao extends IBaseDao<Tache, Integer> {

    List<Tache> findByModuleId(Integer id);

    void deleteByModuleId(Integer id);

    @Query("select count(1) from Tache t ,Module m  where t.module.id = m.id and m.status ="+ Module.STAT_DEFAULT+"  and  t.status in (:status) and t.tacheIndex in (:tacheIndexs)")
    long countByIndex(@Param("status") Integer[] status,@Param("tacheIndexs") Integer[] tacheIndexs);

    @Query("select t from Tache t left join t.user  where t.module.id = :moduleId ")
    List<Tache> findAllByModuleIdWithUser(@Param("moduleId") Integer moduleId);
}
