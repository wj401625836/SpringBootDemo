package com.project.plan.common.utils;


import com.project.plan.common.Constats;
import com.project.plan.entity.User;

import java.util.Date;

public class TUser  {

	/**
	 * ldap系统里面查询到的用户取部分属性转系统用户
	 * @param ldapUser
	 * @return
     */
	public static User parseLdapUserToUser(TUser ldapUser){
		if(ldapUser==null){
			return null;
		}
		User sysUser = new User();
		sysUser.setUserName(ldapUser.getLoginCount());

		sysUser.setNickName(ldapUser.getName());
//			sysUser.setPassword("3931MUEQD1939MQMLM4AISPVNE");//设置密码是 "332211"
		String pwd = MD5Utils.md5(Constats.DEFAULT_USER_PWD);
		sysUser.setPassword(pwd);

		sysUser.setSex(1);//不分男女
//			sysUser.setBirthday();ldUser.getEnterDate()
		sysUser.setEmail(ldapUser.getEmail());
		sysUser.setAddress(ldapUser.getDisplayName());
//			sysUser.setTelephone();
		sysUser.setDeleteStatus(ldapUser.getStat());
		sysUser.setLocked(0);//未锁定
		sysUser.setDescription(ldapUser.getDepart());
		sysUser.setCreateTime(new Date());

		return sysUser;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("id=");
		sb.append(id);
		sb.append(",loginCount=");
		sb.append(loginCount);
		sb.append(",loginPwd=");
		sb.append(loginPwd);
		sb.append(",name=");
		sb.append(name);
		sb.append(",depart=");
		sb.append(depart);
		sb.append(",lastLoginTime=");
		sb.append(lastLoginTime);
		sb.append(",number=");
		sb.append(number);
		sb.append(",stat=");
		sb.append(stat);
		sb.append(",displayName=");
		sb.append(displayName);
		sb.append(",userType=");
		sb.append(userType);
		sb.append(",enterDate=");
		sb.append(enterDate);
		sb.append(",email=");
		sb.append(email);
		sb.append(",leaveDate=");
		sb.append(leaveDate);
		sb.append(",dept1=");
		sb.append(dept1);
		sb.append(",deptName1=");
		sb.append(deptName1);
		sb.append(",dept2=");
		sb.append(dept2);
		sb.append(",deptName2=");
		sb.append(deptName2);
		return sb.toString();
	}
	private Long id;
	private String loginCount;
	private String loginPwd;
	private String name;
	private String depart;
	private java.sql.Timestamp lastLoginTime;
	private String number;
	private Integer stat;
	private String displayName;
	private Integer userType;
	private String enterDate;
	private String email;
	private String leaveDate;
	private Long dept1;
	private String deptName1;
	private Long dept2;
	private String deptName2;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(String loginCount) {
		this.loginCount = loginCount;
	}

	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public java.sql.Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(java.sql.Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getEnterDate() {
		return enterDate;
	}

	public void setEnterDate(String enterDate) {
		this.enterDate = enterDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public Long getDept1() {
		return dept1;
	}

	public void setDept1(Long dept1) {
		this.dept1 = dept1;
	}

	public String getDeptName1() {
		return deptName1;
	}

	public void setDeptName1(String deptName1) {
		this.deptName1 = deptName1;
	}

	public Long getDept2() {
		return dept2;
	}

	public void setDept2(Long dept2) {
		this.dept2 = dept2;
	}

	public String getDeptName2() {
		return deptName2;
	}

	public void setDeptName2(String deptName2) {
		this.deptName2 = deptName2;
	}
}
