package com.project.plan.service.plan;

import com.project.plan.dao.plan.IProjectTacheDao;
import com.project.plan.dao.plan.ITacheDao;
import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.ProjectTache;
import com.project.plan.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Barry on 2018/5/15.
 */
@Service
public class ProjectTacheServiceImpl extends BaseServiceImpl<ProjectTache,Integer> {
    @Autowired
    private IProjectTacheDao projectTacheDao;

    @Autowired
    private ITacheDao tacheDao;

    @Override
    public IBaseDao<ProjectTache, Integer> getBaseDao() {
        return this.projectTacheDao;
    }

    public void saveOrUpdate(ProjectTache projectTache) {
        if(projectTache.getId() != null){
            Date sysDate = new Date();

            ProjectTache dbProjectTache = super.find(projectTache.getId());

            dbProjectTache.setName(projectTache.getName());
            dbProjectTache.setSimpleName(projectTache.getSimpleName());
            dbProjectTache.setHaveStatus(projectTache.getHaveStatus());
            dbProjectTache.setSortIndex(projectTache.getSortIndex());
            dbProjectTache.setStage(projectTache.getStage());

            dbProjectTache.setStatus(projectTache.getStatus());
            dbProjectTache.setUpdateComment(projectTache.getUpdateComment());
            dbProjectTache.setUpdateTime(sysDate);

            super.update(dbProjectTache);

            // Tache 功能环节中 name 和 simpleName 继承 此全局环节,全局环节 name和 simpleName变了值后冗余数据也修改到
            tacheDao.updateNameAndSimpleNameByProjectTacheId(dbProjectTache.getId(),dbProjectTache.getName(),dbProjectTache.getSimpleName());
        }else{
            projectTache.setCreateTime(new Date());
            projectTache.setUpdateTime(new Date());
            projectTacheDao.save(projectTache);
        }
    }
    /**
     * 判断这个状态再系统中有没有用到过,主要判断 ProjectTache里面的 haveStatus字符串中有没有这个id
     * 里面的数据是有 id以逗号拼接而来,早知道要这样查询就建立中间表啦,
     * eg:  4,5,6,7
     * 传入 4,要判断是第一个,
     * */
    public boolean isStatusUse(Integer statusId) {
        long inCount = projectTacheDao.countInHaveStatus("%,"+statusId+",%");
        if(inCount>0){
            return true;
        }
        long startCount = projectTacheDao.countInHaveStatus("%,"+statusId);
        if(startCount>0){
            return true;
        }
        long endCount = projectTacheDao.countInHaveStatus(statusId+",%");
        if(endCount>0){
            return true;
        }
        return false;
    }


    /**
     * 根据传入的id顺序对状态下标做排序,
     * @param projectTacheIds
     */
    public void changeSortIndex(Integer[] projectTacheIds) {
        if(projectTacheIds==null||projectTacheIds.length<=0){
            return ;
        }
        List<ProjectTache> projectTacheList = projectTacheDao.findAll(Arrays.asList(projectTacheIds));
        for(int i=0;i<projectTacheIds.length;i++){// 将statusList中SortIndex以 statusIds的下标的顺序来排序
            for(ProjectTache s: projectTacheList){
                if(s.getId().equals(projectTacheIds[i])){
                    s.setSortIndex(i+1);
                    break;
                }
            }
        }
        projectTacheDao.save(projectTacheList);
    }

}
