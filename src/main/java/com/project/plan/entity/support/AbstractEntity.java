package com.project.plan.entity.support;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;


@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AbstractEntity extends BaseEntity{

	private static final long serialVersionUID = 1234122421L;

	@Id
	@GeneratedValue
	protected Integer id;
	
	@NotNull(message="创建时间不能为空")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@CreatedDate
	@JsonIgnore
	protected Date createTime;

//	@NotNull(message="修改时间不能为空")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@LastModifiedDate
	@JsonIgnore
	protected Date updateTime;
	
	//创建描述
	@Column(name="create_comment",length=2000)
	protected String createComment;
	//修改描述
	@Column(name="update_comment",length=2000)
	protected String updateComment;
	
	/*
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getCreateComment() {
		return createComment;
	}
	public void setCreateComment(String createComment) {
		this.createComment = createComment;
	}
	public String getUpdateComment() {
		return updateComment;
	}
	public void setUpdateComment(String updateComment) {
		this.updateComment = updateComment;
	}*/
}
