package com.project.plan.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;


public class HttpUtil {



	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		 String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			InputStream is = conn.getInputStream();
			System.out.println(is);
			
			in = new BufferedReader(new InputStreamReader(is));
			String line;
 			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	 /**
     * 发送Post请求
     *
     * @param url  请求地址
     * @param list 请求参数
     *
     * @return 请求结果
     *
     * @throws IOException
     */
    public static String sendPost(String url, List<HTTPParam> list) throws IOException {
        StringBuffer buffer = new StringBuffer(); //用来拼接参数
        StringBuffer result = new StringBuffer(); //用来接受返回值
        URL httpUrl = null; //HTTP URL类 用这个类来创建连接
        URLConnection connection = null; //创建的http连接
        PrintWriter printWriter = null;
        BufferedReader bufferedReader; //接受连接受的参数
        //创建URL
        httpUrl = new URL(url);
        //建立连接
        connection = httpUrl.openConnection();
        connection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connection.setRequestProperty("connection", "keep-alive");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        printWriter = new PrintWriter(connection.getOutputStream());
        
        
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                buffer.append(list.get(i).getKey()).append("=").append(URLEncoder.encode(list.get(i).getValue(), "utf-8"));
                //如果不是最后一个参数，不需要添加&
                if ((i + 1) < list.size()) {
                    buffer.append("&");
                }
            }
        }
        printWriter.print(buffer.toString());
        printWriter.flush();
        connection.connect();
        //接受连接返回参数
        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            result.append(line);
        }
        bufferedReader.close();
        return result.toString();
    }
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }    
    
    /**
     * 获取服务器IP(用于记录openim消息发送来源)
     * @author junting.chen
     * @date 2018年1月22日
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static String localIp(){
    	String localIp="";
    	try {
			Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			int temp=1;
			while (allNetInterfaces.hasMoreElements() && temp<=2) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				Enumeration addresses = netInterface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					ip = (InetAddress) addresses.nextElement();
					if (ip != null && ip instanceof Inet4Address) {
						if(temp==2){
							localIp=ip.getHostAddress();
						}
						temp++;
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
    	return localIp;
    }
    
	public static void main(String[] args) throws IOException, InterruptedException {
		 
		//发送 POST 请求 ios 请求更新  http://itunes.apple.com/lookup?id=1195792578
		//发送 GET 请求
		// String sr=HttpUtil.sendGet("http://61.129.57.40:8083/HomeworkManagement/server/req.action", "id=1195792578");
//        String sr=HttpUtil.sendGet("http://itunes.apple.com/lookup", "id=1195792578");
		//发送 POST 请求
// 		  String sr=HttpUtil.sendPost("http://itunes.apple.com/lookup", "id=1195792578");
		
		String testGetHomeworks="{\"name\":\"c_get_homeworks\",\"details\":{\"classId\":\"99\",\"deviceType\":\"Android\",\"pageIndex\":1,\"pageSize\":20,\"protocol\":\"1.0\",\"sessionId\":\"fb1ec40c-8811-48c9-80fe-8c11f1609d30\",\"status\":1,\"userId\":\"20110\",\"userType\":2,\"versionId\":1,\"versionName\":\"1.0\"}}";
		String testGetTeaClassDetail="{\"name\":\"c_get_tea_class_details\",\"details\":{\"classId\":\"109\",\"deviceType\":\"Android\",\"protocol\":\"1.0\",\"sessionId\":\"a9d54dee-3d92-4fb0-99fb-614f531c985a\",\"userId\":\"20191\",\"userType\":2,\"versionId\":1,\"versionName\":\"1.0\"}}";
		String testGetHomeworkStat="{\"name\":\"c_get_homework_stat\",\"details\":{\"deviceType\":\"Android\",\"homeworkId\":\"690\",\"orderType\":0,\"protocol\":\"1.0\",\"sessionId\":\"b0943509-2e9b-4f49-abcf-42283e1f5c57\",\"sortType\":1,\"topNum\":1,\"userId\":\"20849\",\"userType\":2,\"versionId\":1,\"versionName\":\"1.0\"}}";
		String testToken = "{'name':'c_get_oss_permission','details':{'protocol':'1.0','userType':'-1','userId':'-1','sessionId':'-1','deviceType':'4','token':'123'}}";
		long start = System.currentTimeMillis();
		for(int i=0 ; i<1 ;i++){
			//0.5秒发送一个请求
			Thread.sleep(100l);
			String sr=HttpUtil.sendPost("http://www.stubook.com.cn:8080/server/req.action", "json="+testToken);
	        System.out.println("结果"+sr+"\n 次数"+(i+1));
		}
		long end = System.currentTimeMillis();
		System.out.println("\n\n\n\n一共耗时："+(end-start));
		
	/*	String sr=HttpUtil.sendPost("http://192.168.2.6:8081/HomeworkManagement/server/req.action", "json="+testGetHomeworks);
        System.out.println("结果"+sr);*/
		
		
		/*List<HTTPParam> list = new ArrayList<>();
		list.add(new HTTPParam("id", "1195792578"));
		System.out.println("sendPost: "+sendPost("http://itunes.apple.com/lookup", list));*/
	}


	public static String getClientIP(HttpServletRequest request) {
		if (request == null)
			return null;
		String s = request.getHeader("X-Forwarded-For");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("Proxy-Client-IP");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("WL-Proxy-Client-IP");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("HTTP_CLIENT_IP");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getHeader("X-Real-IP");
		if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s))
			s = request.getRemoteAddr();

		if ("127.0.0.1".equals(s) || "0:0:0:0:0:0:0:1".equals(s)) {
			try {
				s = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException unknownhostexception) {
			}
		}
		return s;
	}

	public static String getString(HttpServletRequest request, String paraname) {
		return request.getParameter(paraname) == null ? "" : request.getParameter(paraname);
	}

	public static String getString(HttpServletRequest request, String paraname, String defaultVal) {
		return request.getParameter(paraname) == null ? defaultVal : request.getParameter(paraname);
	}

	public static String getRequestBody(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		Enumeration<String> enu = request.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = request.getParameter(name);
			if(sb.length()<=0) {
				sb.append(name + "=" + value);
			}else {
				sb.append("&" + name + "=" + value);
			}
		}
		return sb.toString();
	}
}

class HTTPParam {
    //请求参数
    private String key;
    //参数值
    private String value;

    public HTTPParam() {

    }

    public HTTPParam(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
