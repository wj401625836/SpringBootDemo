//package com.project.plan.config.intercepter;
//
//import com.project.plan.common.Constats;
//import com.project.plan.common.utils.HttpUtil;
//import com.project.plan.entity.User;
//import com.project.plan.entity.Visit;
//import com.project.plan.service.impl.VisitServiceImpl;
//import org.apache.shiro.SecurityUtils;
//import org.apache.shiro.subject.Subject;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Date;
//import java.util.Queue;
//import java.util.concurrent.ConcurrentLinkedQueue;
//
///**
// * 拦截某些敏感操作记录到数据库
// * @author Barry
// */
//@Aspect
//@Component
//public class HttpAspect {
//	public static Logger logger = LoggerFactory.getLogger(HttpAspect.class);
//
//	@Autowired
//	private VisitServiceImpl visitService;
//
//
////	private static Queue<Visit> queueBatch = new ConcurrentLinkedQueue<>();//如数据太多别每次一条一条的插入吧
//	/**
//	 * 配置切面
//	 */
//	@Pointcut("execution (public * com.project.plan.controller.admin.system.*.*(..))")
//	public void log() {}
//
//
//	/**
//     * 可以在执行方法之前和之后改变参数和返回值
//     * @param joinPoint 用于获取目标方法相关信息的参数
//     * @return 最终的返回值
//     * @throws Throwable
//     */
//	@Around("log()")
//    public Object processTx(ProceedingJoinPoint joinPoint) throws Throwable {
//
//
//		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = attributes.getRequest();
//		String requestUrl = request.getRequestURL().toString();
//		if(requestUrl.endsWith("/example")||requestUrl.endsWith("/test")) {
//			//某些请求,转发的要忽略, @Pointcut 切的时候把所有的controller都切进去啦
//			  Object rvt = joinPoint.proceed();
//			  return rvt;
//		}else {
//
//
//			String requestBody = HttpUtil.getRequestBody(request);
//			String ip = HttpUtil.getClientIP(request);
//
//			System.out.println("Around ：执行方法之前 ");
//
//	       long startTime = System.currentTimeMillis();
//	        //执行 controller方法
//	        Object rvt = joinPoint.proceed();
//	        long duration = System.currentTimeMillis() - startTime;
//	        String responseBody = rvt.toString();
//	        if(responseBody.indexOf("forward:")>-1) { //内部转发不需要打log
//	        	return rvt;
//	        }
//
//	        System.out.println("Around ：执行方法之后");
//	        Integer userId = null;
//			//获取登录用户id
//			Subject subject = SecurityUtils.getSubject();
//			User user = (User) subject.getSession().getAttribute(Constats.CURRENTUSER);
//			if(user !=null){
//				userId = user.getId();
//			}
//
//
//	     // isEnc 是 RsaController解密后转发到其它Controller带的开关,如果有并且 equals("t"),返回也需要加密,   如果有加过密的就不用再加密啦
//	     	String isEnc = HttpUtil.getString(request, "isEnc","f");
//			saveVisit(userId,duration,ip,requestUrl,isEnc,requestBody,responseBody);
//
//			//String validateAndEncReturnMsg = RsaSimpleUtils.validateAndEncReturnMsg(requestUrl, responseBody, isEnc);
//	        return rvt;
//		}
//    }
//
//
//
//	/**
//     * 可以在执行方法之前对目标方法的参数进行判断
//     * 通过抛出一个异常来阻断目标方法的访问
//     * @param joinPoint
//     */
//	@Before("log()")
//	public void toLog(JoinPoint joinPoint) {
//	}
//
//	/**
//     * 可以在执行方法之后对目标方法的参数进行判断
//     * @param joinPoint
//     */
//	@After("log()")
//	public void afterLog(JoinPoint joinPoint) {
////		System.out.println("afterLog");
//	}
//
//	/**
//     * 与After的区别在于AfterReturning只有在方法执行成功的之后才会被织入，如果After和
//     * AfterReturning同时存在于一个文件中，谁写在前面谁先运行
//     * @param joinPoint
//     * @param object 方法的返回值
//     */
//	@AfterReturning(returning="object",pointcut="log()")
//	public void afterReturnLog(JoinPoint joinPoint,Object object) {
////		System.out.println("afterReturning");
//	}
//
//	private void saveVisit(Integer userId,long duration,String ip,String requestUrl,String isEnc,String requestBody,String responseBody) {
//		Visit visit = new Visit();
//		try {
//			visit.setUrl(requestUrl);
//			visit.setRequestBody(requestBody);
//			visit.setResponseBody(responseBody);
//			visit.setIp(ip);
//			visit.setIsEnc(isEnc);
//			visit.setUserId(userId);
//			visit.setDuration(duration);
//			visit.setCreateTime(new Date());
//			visitService.save(visit);
//
////			if(queueBatch.size()!=0 && queueBatch.size()%500==0) {//每有多少条记录执行存储一次,
////				visitService.saveAll(queueBatch);
////				queueBatch.clear();
////				queueBatch = new ConcurrentLinkedQueue<>();
////			}else {
////				queueBatch.add(visit);
////			}
//			logger.info("save visit succ："+visit.toString());
//		} catch (Exception e) {
//			logger.error("save visit error : "+visit.toString()+"" + e.getMessage());
//		}
//	}
//}
