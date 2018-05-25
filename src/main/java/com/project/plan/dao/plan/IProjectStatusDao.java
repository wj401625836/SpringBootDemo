package com.project.plan.dao.plan;

import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.ProjectStatus;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Barry on 2018/5/15.
 */
public interface IProjectStatusDao extends IBaseDao<ProjectStatus, Integer> {

    //重写 jpa的查询所有方法,让其按照自己想要的顺序排序
    @Query(" select s from ProjectStatus s where 1=1 order by status asc,sortIndex asc ")
    List<ProjectStatus> findAll();

}
