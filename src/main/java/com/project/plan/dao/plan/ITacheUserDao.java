package com.project.plan.dao.plan;

import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.Tache;
import com.project.plan.entity.plan.TacheUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Barry on 2018/5/9.
 */
@Repository
public interface ITacheUserDao extends IBaseDao<TacheUser, Integer> {

    /**获取某环节下面某人的最后一次操作,如果他有做多次也只取最后一条*/
    @Query(" from TacheUser where tache.id=:tacheId and user.id=:userId order by id desc ")
    public List<TacheUser> findTacheLastUser(@Param("tacheId") Integer tacheId, @Param("userId") Integer userId);


    @Query(" from TacheUser where tache.id = :tacheId")
    List<TacheUser> findByTacheId(@Param("tacheId") Integer tacheId);

    @Modifying
    @Query("delete FROM TacheUser where tache.id in (:tacheIds) ")
    void deleteByTacheIds(@Param("tacheIds") Integer[] tacheIds);
}
