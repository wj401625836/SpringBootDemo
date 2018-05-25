package com.project.plan.service.plan;

import com.project.plan.dao.plan.ITacheDao;
import com.project.plan.dao.plan.ITacheUserDao;
import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.Tache;
import com.project.plan.entity.plan.TacheUser;
import com.project.plan.service.support.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Barry on 2018/5/9.
 */
@Service
public class TacheUserServiceImpl extends BaseServiceImpl<TacheUser,Integer> {

    @Autowired
    private ITacheUserDao tacheUserDao;

    @Override
    public IBaseDao<TacheUser, Integer> getBaseDao() {
        return this.tacheUserDao;
    }


    public List<TacheUser> findByTacheId(Integer tacheId) {
        List<TacheUser> tacheUsers = tacheUserDao.findByTacheId(tacheId);
        return tacheUsers;
    }
}
