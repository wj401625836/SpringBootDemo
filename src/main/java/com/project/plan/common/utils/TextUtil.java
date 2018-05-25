package com.project.plan.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.regex.Pattern;

public class TextUtil {
	private static Logger logger = LoggerFactory.getLogger(TextUtil.class);

	public static <T> boolean sqlValid(T t){
		if(t == null){
			return false;
		}
		
		String reg = "(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute)\\b)";
		Pattern sqlPattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
		if (sqlPattern.matcher(t.toString()).find()) {
	        logger.info("存在sql注入风险");
	        return false;  
	    }  
	    return true;
	}
	
	public static boolean isNullOrEmpty(String s) {
		return s == null || "".equals(s.trim());
	}
	public static boolean isNullOrEmpty(Long l){
		return l==null ;
	}
	public static boolean isNullOrEmpty(Integer i){
		return i==null ;
	}
	
	public static boolean isNullOrEmpty(Double d){
		return d==null ;
	}
	
	public static boolean isStrNullOrEmpty(String s) {
		if(StringUtils.isBlank(s) || "null".equals(s)){
			return true;
		}
		return false;  
	}
	
	/**
	 * MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String EncoderByMd5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			BASE64Encoder base64en = new BASE64Encoder();
			str = base64en.encode(md5.digest(str.getBytes("utf-8")));
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return str;
	}

	/**
	 * 获取单个文件的MD5值！
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileMD5(File file) {
		if (!file.isFile()) {
			return null;
		}
		MessageDigest digest = null;
		FileInputStream in = null;
		byte buffer[] = new byte[1024];
		int len;
		try {
			digest = MessageDigest.getInstance("MD5");
			in = new FileInputStream(file);
			while ((len = in.read(buffer, 0, 1024)) != -1) {
				digest.update(buffer, 0, len);
			}
			in.close();
		} catch (Exception e) {
			logger.error(e.toString(), e);
			return null;
		}
		BigInteger bigInt = new BigInteger(1, digest.digest());
		return bigInt.toString(16);
	}

	public static String getJsonFromRequest(HttpServletRequest request) {
		String json = "";
		try {
			BufferedReader br = request.getReader();
			String line="";
			while((line=br.readLine())!=null){
				json +=line;
			}
			br.close();
			if(TextUtil.isNullOrEmpty(json)) {
				json = request.getParameter("json");
			}
		} catch (Exception e) {
			logger.error(e.toString());
			if(json==null){
				json=request.getParameter("json");
				logger.info("getRequestJson from param: json=" + json);
			}
		}
		// json = request.getParameter("jsonObject");
		// logger.info(json);
		return json;
	}


}
