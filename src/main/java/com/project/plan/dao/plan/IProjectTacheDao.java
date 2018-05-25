package com.project.plan.dao.plan;

import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.ProjectTache;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Barry on 2018/5/15.
 */
public interface IProjectTacheDao  extends IBaseDao<ProjectTache, Integer> {

    @Query("select count(*) from ProjectTache where haveStatus like :statusIdStr ")
    long countInHaveStatus(@Param("statusIdStr") String statusIdStr);

    //重写 jpa的查询所有方法,让其按照自己想要的顺序排序
    @Query(" select s from ProjectTache s where 1=1 order by sortIndex asc ")
    List<ProjectTache> findAll();

}
