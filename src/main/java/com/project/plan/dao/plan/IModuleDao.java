package com.project.plan.dao.plan;

import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by Barry on 2018/4/20.
 */
@Repository
public interface IModuleDao extends IBaseDao<Module, Integer> {


//    @Query("SELECT new Module(ao,a) FROM ApkOptions ao,Apks a where ao.apkId = a.id and  ao.status in (:status)  ORDER BY ao.createTime DESC")
//    Page<Module> selectWithProject();

}
