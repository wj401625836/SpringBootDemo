package com.project.plan.common;

import java.util.*;

public class Constats {

	public static String CURRENTUSER = "_currentUser";//登录用户session保存key

	public static final String DEFAULT_USER_PWD = "332211";//ldap用户默认统一设置密码, 现在登录更具ldap域登录,登录成功表示账号是对的, 系统再以这个这个登录密码登录, 如ldap密码登录不成功,系统密码成功也能登录
	public static final String DEFAULT_USER_ADD_PWD = "111111";

	//各个环节名称下所属分类
	public static final String[] TACHE_TYPE_NAMES=new String[]{"一、产品经理","二、UI效果图","三、研发阶段(服务器)","三、研发阶段(客户端)","四、测试","五、验收上线"
			,"归档"};
	//各个环节名称
    public static final String[] TACHE_INDEX_NAMES=new String[]{"线框图评审","原型交互稿评审","原型交互稿归档","UI效果图","软件概要设计归档","软件Case归档"
            ,"Server借口提供","软件首版交付","UI / 产品检查","测试","产品验收","上线评审"};
	public static Map<String,Map<String,String>> map = new LinkedHashMap<>();
//	public static Map<String,String> map = new LinkedHashMap<>();
	static {
		Map<String,String> item1 = new LinkedHashMap<>();
		Map<String,String> item2 = new LinkedHashMap<>();
		Map<String,String> item3 = new LinkedHashMap<>();
		Map<String,String> item4 = new LinkedHashMap<>();
		Map<String,String> item5 = new LinkedHashMap<>();
		Map<String,String> item6 = new LinkedHashMap<>();

		item1.put("1","线框图评审");
		item1.put("2","原型交互稿评审");
		item1.put("3","原型交互稿归档");
		item2.put("4","UI效果图");
		item3.put("5","软件概要设计归档");
		item5.put("6","软件Case归档");
		item3.put("7","Server借口提供");
		item4.put("8","软件首版交付");
		item1.put("9","UI / 产品检查");
		item5.put("10","测试");
		item6.put("11","产品验收");
		item6.put("12","上线评审");

//		map.putAll(item1);
//		map.putAll(item2);
//		map.putAll(item3);

		map.put(Constats.TACHE_TYPE_NAMES[0],item1);//"一、产品经理"
		map.put(Constats.TACHE_TYPE_NAMES[1],item2);//"二、UI效果图"
		map.put(Constats.TACHE_TYPE_NAMES[2],item3);//"三、研发阶段(服务器)"
		map.put(Constats.TACHE_TYPE_NAMES[3],item4);//"三、研发阶段(客户端)"
		map.put(Constats.TACHE_TYPE_NAMES[4],item5);//"四、测试"
		map.put(Constats.TACHE_TYPE_NAMES[5],item6);//"五、验收上线"

		Map<String,String> itemSucess= new LinkedHashMap<>();
		map.put(Constats.TACHE_TYPE_NAMES[6],itemSucess);//"归档" 已经归档不在环节内做搜索查询

	}

	/**
	 * 获取这个类型下面的所有key的下标
	 * @param typeName eg: 一、产品经理
	 * @return	 eg： 	[1,2,3,9]
     */
	public static Integer[] getTacheIndexByType(String typeName){
		if(typeName==null){
			return null;
		}
		List<Integer> indexList = new ArrayList<>();
		Map<String, String> indexMap = map.get(typeName);
		if(indexMap!=null&&!Constats.TACHE_TYPE_NAMES[6].equals(typeName)){
			Set<String> keys = indexMap.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()){
				String key = it.next();
				indexList.add(Integer.valueOf(key));
			}
		}else if(Constats.TACHE_TYPE_NAMES[6].equals(typeName)){//归档,所有类型都可能归档,返回全部
			return new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
		}
		return indexList.toArray(new Integer[indexList.size()]);
	}

}
