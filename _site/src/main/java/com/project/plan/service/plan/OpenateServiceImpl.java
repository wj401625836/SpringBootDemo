package com.project.plan.service.plan;

import com.project.plan.dao.plan.IModuleDao;
import com.project.plan.dao.plan.IOpenateDao;
import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.Openate;
import com.project.plan.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Barry on 2018/4/20.
 */
@Service
public class OpenateServiceImpl extends BaseServiceImpl<Openate,Integer> {
    @Autowired
    private IOpenateDao openateDao;

    @Override
    public IBaseDao<Openate, Integer> getBaseDao() {
        return this.openateDao;
    }


    public List<Openate> findByTacheId(Integer tacheId) {
        return openateDao.findByTacheId(tacheId);
    }
}
