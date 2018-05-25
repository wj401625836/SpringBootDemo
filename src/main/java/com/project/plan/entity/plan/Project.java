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
 * Created by Barry on 2018/4/20.
 * 项目
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_project")
public class Project extends AbstractEntity{
    public static final int STAT_DEFAULT = 0;//正常
    public static final int STAT_ABNORMAL = 1;//异常

    @NotNull(message="项目名称不能为空")
    @Column(name="name",length=500)
    private String name;

    @NotNull(message="项目状态不能为空")
    @Column(name="stat",length=2,nullable = false)
    private Integer status ;


}
