package com.project.plan.config.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CommonIntercepter implements HandlerInterceptor {
	private static Logger logger = LoggerFactory.getLogger(CommonIntercepter.class);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		 return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
			request.setAttribute("ctx", request.getContextPath());
		

		StringBuffer requestUrl = request.getRequestURL();
		String remoteAddr = request.getRemoteAddr();
		String method = request.getMethod();

		logger.info("-----------------\trequstUrl: "+requestUrl+"\tremoteAddr:"+remoteAddr+"\tmethod:"+method);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}


}
