package com.project.plan.service.plan;

import com.project.plan.dao.plan.IProjectDao;
import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.Resource;
import com.project.plan.entity.plan.Project;
import com.project.plan.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Barry on 2018/4/20.
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<Project,Integer> {
    @Autowired
    private IProjectDao projectDao;
    @Override
    public IBaseDao<Project, Integer> getBaseDao() {
        return this.projectDao;
    }


//    @CacheEvict(value = "projectCache")
    public void saveOrUpdate(Project project) {
        if(project.getId() != null){
            Project dbProject = super.find(project.getId());
            dbProject.setUpdateTime(new Date());
            dbProject.setName(project.getName());
            dbProject.setStatus(project.getStatus());
//            dbProject.setCreateComment(project.getCreateComment());//创建描述不让修改
            project.setUpdateComment(project.getUpdateComment());

            dbProject.setUpdateTime(new Date());
            super.update(dbProject);
        }else{
            project.setCreateTime(new Date());
            project.setUpdateTime(new Date());
            save(project);
        }
    }
}
