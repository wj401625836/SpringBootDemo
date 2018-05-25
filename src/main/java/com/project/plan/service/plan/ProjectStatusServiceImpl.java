package com.project.plan.service.plan;

import com.project.plan.dao.plan.IProjectStatusDao;
import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.ProjectStatus;
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
public class ProjectStatusServiceImpl extends BaseServiceImpl<ProjectStatus,Integer> {
    @Autowired
    private IProjectStatusDao statusDao;
    @Override
    public IBaseDao<ProjectStatus, Integer> getBaseDao() {
        return this.statusDao;
    }

    public void saveOrUpdate(ProjectStatus status) {
        if(status.getId() != null){
            Date sysDate = new Date();
            ProjectStatus dbStatus = super.find(status.getId());
            dbStatus.setName(status.getName());

            dbStatus.setStatus(status.getStatus());
            dbStatus.setUpdateComment(status.getUpdateComment());
            dbStatus.setUpdateTime(sysDate);

            super.update(dbStatus);
        }else{
            status.setCreateTime(new Date());
            status.setUpdateTime(new Date());
            statusDao.save(status);
        }
    }

    /**
     * 根据传入的id顺序对状态下标做排序,
     * @param statusIds
     */
    public void changeSortIndex(Integer[] statusIds) {
        if(statusIds==null||statusIds.length<=0){
            return ;
        }
        List<ProjectStatus> statusList = statusDao.findAll(Arrays.asList(statusIds));
        for(int i=0;i<statusIds.length;i++){// 将statusList中SortIndex以 statusIds的下标的顺序来排序
            for(ProjectStatus s: statusList){
                if(s.getId().equals(statusIds[i])){
                    s.setSortIndex(i+1);
                    break;
                }
            }
        }
        statusDao.save(statusList);
    }
}
