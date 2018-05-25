package com.project.plan.entity.plan;

import com.project.plan.entity.support.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by Barry on 2018/5/15.
 * 环节管理： 环节名称 环节顺序 环节简称 环节状态 ....
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_project_tache")
public class ProjectTache extends AbstractEntity {
    private static final long serialVersionUID = 1234124241L;

    public static final Integer STAT_DEFAULT = 0;//正常
    public static final Integer STAT_UNUSUAL =1;//异常

    public static final Integer STAGE_PRODUCT = 0;//产品
    public static final Integer STAGE_DEVELOP =1;//开发
    public static final Integer STAGE_TEST =2;  //测试



    @NotNull(message="环节名称不能为空")
    @Column(name="name",length=500)
    private String name;

    @NotNull(message="环节简称不能为空")
    @Column(name="simple_name",length=100)
    private String simpleName;

    @NotNull(message="环节顺序不能为空")
    @Column(name="sort_index",length=2,nullable = false)
    private Integer sortIndex ;

    @NotNull(message="环节状态不能为空")
    @Column(name="stat",length=2,nullable = false)
    private Integer status ;

    @NotNull(message="本环节拥有状态不能为空")//用字符串存放 状态id,多个逗号分隔 eg： 2,3,5,1
    @Column(name="have_stat",length=200,nullable = false)
    private String haveStatus ;

    @NotNull(message="阶段不能为空")
    @Column(name="stage",length=2,nullable = false)
    private Integer stage;

}
