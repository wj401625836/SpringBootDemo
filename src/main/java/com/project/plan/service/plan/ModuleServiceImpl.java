package com.project.plan.service.plan;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.project.plan.dao.plan.*;
import com.project.plan.dao.support.IBaseDao;
import com.project.plan.entity.plan.Module;
import com.project.plan.entity.plan.Openate;
import com.project.plan.entity.plan.Project;
import com.project.plan.entity.plan.Tache;
import com.project.plan.service.support.impl.BaseServiceImpl;
import org.aspectj.weaver.tools.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Barry on 2018/4/20.
 */
@Service
public class ModuleServiceImpl extends BaseServiceImpl<Module,Integer> {
    @Autowired
    private IModuleDao moduleDao;
    @Autowired
    private IProjectDao projectDao;
    @Autowired
    private IOpenateDao openateDao;
    @Autowired
    private ITacheDao tacheDao;
    @Autowired
    private ITacheUserDao tacheUserDao;

    @Autowired
    private  TacheServiceImpl tacheService;


    @Override
    public IBaseDao<Module, Integer> getBaseDao() {
        return this.moduleDao;
    }

    public void saveOrUpdate(Module module,Integer[]haveTacheIds) {
        if(module.getId() != null){
            Module dbModule = super.find(module.getId());

            Project p = new Project();//如果项目有修改,修改项目
            p.setId(module.getProject().getId());
            dbModule.setProject(p);

            dbModule.setName(module.getName());
            dbModule.setStartTime(module.getStartTime());
            dbModule.setWishTime(module.getWishTime());
            dbModule.setStatus(module.getStatus());

//            dbModule.setCreateComment(module.getCreateComment());//创建描述不让修改
            dbModule.setUpdateComment(module.getUpdateComment());

            dbModule.setUpdateTime(new Date());
            super.update(dbModule);
        }else{
            module.setCreateTime(new Date());
            module.setUpdateTime(new Date());
            save(module);

            tacheService.saveTache(module,haveTacheIds);
        }

    }

    /** */
    public Page<Module> findAllWithProject(Specification<Module> moduleSpecification, PageRequest pageRequest) {
        Page<Module>  modules =   super.findAll(moduleSpecification,pageRequest);
//        for (Module m: modules) {
////            Project p = projectDao.findOne(m.getProjectId());
////            m.setProject(p);
//            Project p = m.getProject();
//        }
        return modules;
//        return moduleDao.selectWithProject();
        //return super.findAll(moduleSpecification,pageRequest);
    }

    /**
     * 删除模块,
     * 1.删除模块下面的环节的操作记录 t_openate
     * 2.删除模块下面的环节 t_tache
     * 3.先删除模块,t_module
     * @param id
     */
    @Transactional
    public void deleteModule(Integer id) {
        Module m = moduleDao.findOne(id);
        List<Tache> taches = tacheDao.findByModuleId(m.getId());
        Integer[] tacheIds = new Integer[taches.size()];
        for(int i=0;i<taches.size();i++){
            tacheIds[i] = taches.get(i).getId();
        }
        if(tacheIds!=null&&tacheIds.length>0){

            tacheUserDao.deleteByTacheIds(tacheIds);
            openateDao.deleteByTacheIds(tacheIds);
        }
        tacheDao.deleteByModuleId(m.getId());
        super.delete(m.getId()); //moduleDao.delete(id); //用moduleDao.delete(id) 删除不成功,因为 module 和tache是在同一个事务里面删除,里面出事务时候是一起提交的,用supper.delete就能删除,这相当于调用了service的内部方法,所以不会走事务
        System.out.println("delete successful !!! ");
    }

//    @Transactional(readOnly = true)
    public void saveOneTest() {

        Date d = new Date();

        Module module = new Module();
        Project p = new Project();
        p.setId(1);
        module.setProject(p);
        module.setStatus(0);
        module.setCreateTime(d);
        module.setWishTime(d);
        module.setStartTime(d);
        module.setName("hhh");

        try {
            moduleDao.save(module);
        }catch (Exception e){
            System.out.println("--插入t_module失败");
        }

        try{
            tacheService.saveOneTest(module.getId());
        }catch (Exception e){
            System.out.println("--插入tache失败");
        }

        System.out.println("--插入成功");
    }
}
