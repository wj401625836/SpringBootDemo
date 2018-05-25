package com.project.plan.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	public static final String DEF_DATE_YMDH="yyMMddHH";
	public static final String DEF_DATE_PATTERN = "yyyy-MM-dd";
	public static final String DEF_DATE_MINUT = "yyyy-MM-dd HH:mm";
	public static final String DEF_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DEF_DATETIME_PATTERN_MILL ="yyyy-MM-dd HH:mm:ss:SSS";
	
	public static final void add(Date date, int year, int month, int day, int hour, int minute, int second, int millisecond) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DAY_OF_MONTH, day);
		cal.add(Calendar.HOUR_OF_DAY, hour);
		cal.add(Calendar.MINUTE, minute);
		cal.add(Calendar.SECOND, second);
		cal.add(Calendar.MILLISECOND, millisecond);
		date.setTime(cal.getTimeInMillis());
	}
	
	public static final void addYear(Date date, int year) {
		add(date, year, 0, 0, 0, 0, 0, 0);
	}
	
	public static final void addMonth(Date date, int month) {
		add(date, 0, month, 0, 0, 0, 0, 0);
	}
	
	public static final void addDay(Date date, int day) {
		add(date, 0, 0, day, 0, 0, 0, 0);
	}
	
	public static final void addHour(Date date, int hour) {
		add(date, 0, 0, 0, hour, 0, 0, 0);
	}
	
	public static final void addMinute(Date date, int minute) {
		add(date, 0, 0, 0, 0, minute, 0, 0);
	}
	
	public static final void addSecond(Date date, int second) {
		add(date, 0, 0, 0, 0, 0, second, 0);
	}
	
	public static final void addMillisecond(Date date, int millisecond) {
		add(date, 0, 0, 0, 0, 0, 0, millisecond);
	}
	
	public static String format(Date date, String pattern, int day) {
		addDay(date, day);
		return format(date,pattern);
	}

	/**
	 *
	 * 格式化时间,如果时间参数不合法返回默认值(defaultValue)
	 * @param date 时间
	 * @param pattern 格式
	 * @param defaultValue
     * @return
     */
	public static final String format(Date date, String pattern,String defaultValue ) {
		String value = format(date,pattern);
		return value !=null ?value : defaultValue ;
	}

	/**
	 * 格式化时间,如果时间参数不合法返回空
	 * @param date 时间
	 * @param pattern 格式
     * @return
     */
	public static final String format(Date date, String pattern) {
		if(date != null && pattern != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		}
		return null;
	}
	
	public static final String formatDate(Date date) {
		return format(date, DEF_DATE_PATTERN);
	}
	
	public static final String formatDatetime(Date date) {
		if(date==null)
			return null;
		return format(date, DEF_DATETIME_PATTERN);
	}
	public static final Date parse(String dateSource, String pattern) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			date = sdf.parse(dateSource);
		} catch (ParseException e) {
		}
		return date;
	}
	
	public static final Date parseDate(String dateSource) {
		return parse(dateSource, DEF_DATE_PATTERN);
	}
	
	public static final Date parseDatetime(String dateSource) {
		return parse(dateSource, DEF_DATETIME_PATTERN);
	}
	/**
	 * 转时间,如果格式从精确到不精确依次取,保证取到数据
	 */
	public static final Date parseTryDate(String dateSource) {
		Date d = null;
		d = parse(dateSource, DEF_DATETIME_PATTERN_MILL);	
		if(d==null) {
			d = parse(dateSource, DEF_DATETIME_PATTERN);
		}
		if(d==null) {
			d = parse(dateSource, DEF_DATE_MINUT);
		}
		if(d==null) {
			d = parse(dateSource, DEF_DATE_PATTERN);
		}
		if(d==null) {
			d = parse(dateSource, DEF_DATE_YMDH);
		}
		return d;
	}
	
	public static final Date copyDate(Date source) {
		Date target = null;
		if(source != null) {
			target = new Date(source.getTime());
		}
		return target;
	}
	/*得到哪天.格式 yyyy-MM-dd */
	public static final String getDayStr(Long currentMillis){
		return format(new Date(currentMillis), DEF_DATE_PATTERN);
	}
	
	/*得到哪天.格式 yyyy-MM-dd HH:mm:ss */
	public static final String getDayString(Long currentMillis){
		return format(new Date(currentMillis), DEF_DATE_PATTERN);
	}
	
	public static void main(String[] args) {
		/*Date sendTime = new Date();
		Date expiredTime = new Date();
		addDay(sendTime, 2);
		System.out.println(expiredTime.before(sendTime));
		System.out.println(getDayStr(System.currentTimeMillis()));
		
		System.out.println(format(sendTime, DEF_DATETIME_PATTERN));
		
		
		Date curDate = new Date();
		System.out.println(curDate);
		DateUtil.addMinute(curDate, -3);
		System.out.println(curDate.before(new Date()));
		*/
		/*Date date = new Date();
		System.out.println(date);
		date = getNextDayMoring(date);
		//System.out.println(date);
		//System.out.println("s"+parseDatetime("2017-02-08 22:33:00"));
		System.out.println(format(new Date(), "yyMMddHH"));*/
		
		String sendTime = "2017-06-20 10:08:00";
		Integer answerTime = 2; 
		Date send = DateUtil.parse(sendTime, DateUtil.DEF_DATETIME_PATTERN);//作业发布时间
		Date expd = new Date();
		expd.setTime(send.getTime()+answerTime*60*1000);//作业截止时间
		long mill = DateUtil.dateDiff(expd,new Date());
		Long second = (mill/1000);
		System.out.println(second);
	}
	/*根据时间计算它的openIm账号 */
	public static final String getImNameByDate(Long currentMillis){
		return format(new Date(currentMillis), "yyyyMMddHHmmss");
	}
	/**变成第二条凌晨6点*/
	public static Date getNextDayMoring(Date date) {
		if(date==null)
			date=new Date();
		//date.setHours(hours);
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		
		instance.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH),  instance.get(Calendar.DATE)+1, 6, 0);
		return instance.getTime();
	}
	/***
	 * 获取时间差: date1 比date2 小多少毫秒
	 * @param date1
	 * @param date2
	 * @return 毫秒
	 * @throws ParseException
	 */
	public static Long dateDiff(Date date1,Date date2) {
		if(date1==null||date2==null){
			return null;
		}
        long time1=date1.getTime();  
        long time2=date2.getTime();  
        long dateMill=Math.abs(time2-time1);  
        return dateMill;
	}  
	//根据现在日期求起始日期
	public static Map<String,String> getTime(){
		Map<String,String> map=new HashMap<String,String>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-M-dd");
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		if(month>=9){
			Calendar begin = new GregorianCalendar(year, 8, 1);
			String beginDate = sdf.format(begin.getTime());
			Calendar end = new GregorianCalendar(year+1, 8, 1);
			String endDate = sdf.format(end.getTime());
			map.put("begin", beginDate);
			map.put("end", endDate);
		}else{
			Calendar begin = new GregorianCalendar(year-1, 8, 1);
			String beginDate = sdf.format(begin.getTime());
			Calendar end = new GregorianCalendar(year, 8, 1);
			String endDate = sdf.format(end.getTime());
			map.put("begin", beginDate);
			map.put("end", endDate);			
		}
		return map;
	}

	//时间格式转换,转化为时间戳
	public static Long getTimestamp(Object object){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = sdf.parse(String.valueOf(object));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}
}
