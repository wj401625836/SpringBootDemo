package com.project.plan.entity;

import com.project.plan.entity.support.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 记录系统所有的请求与相应
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = false)
@Table(name = "tb_visit")
public class Visit extends AbstractEntity {

	
	private String url; // 访问的url地址
	
	@Size(max=5000)
	private String requestBody;  // 访问参数
	@Size(max=8000)
	private String responseBody; // 响应参数
	private String ip; // 访问ip地址
	private long duration; //访问处理时间
	
	@Size(min=14, max=20)
	@Column(name="user_id",length=32)
	private Integer userId; //主键uuid 使用客户端手机唯一标识号，如果没有登录 user_id为空
	
	@Size(min=0, max=10)
	@Column(name="is_enc",length=10)
	private String isEnc;//是否对请求加密处理 t:是,  f:否
}
