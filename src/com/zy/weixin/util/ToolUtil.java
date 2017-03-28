package com.zy.weixin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {
	
	/**
	 * 时间格式 yyyy-MM-dd
	 */
	public final static String TIME_FORMAT_TEN = "yyyy-MM-dd";
	
	/**
	 * 时间格式 yyyy-MM
	 */
	public final static String TIME_FORMAT_SEVEN = "yyyy-MM";
	
	/**
	 * 时间格式 yyyyMMdd
	 */
	public final static String TIME_FORMAT_EIGHT = "yyyyMMdd";
	
	/**
	 * 时间格式 yyyy
	 */
	public final static String TIME_FORMAT_FOUR = "yyyy";
	
	/**
	 * 时间格式 HH:mm:ss
	 */
	public final static String TIME_FORMAT_SIX = "HH:mm:ss";
	
	/**
	 * 时间格式 yyyy-MM-dd HH:mm:ss
	 */
	public final static String TIME_FORMAT_THIRTEEN = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 时间格式 yyyy/MM/dd HH:mm:ss
	 */
	public final static String TIME_FORMAT_THIRTEEN_OTHER = "yyyy/MM/dd HH:mm:ss";
	
	/**
	 * 时间格式 yyyyMMddHHmmss
	 */
	public final static String TIME_FORMAT_FOURTEEN = "yyyyMMddHHmmss";
	
	/**
	 * 空字符串 ""
	 */
	public final static String STR_EMPTY = "";
	
	/**
	 * 英文标点符号 ","
	 */
	public final static String COMMA = ",";
	
	/**
	 * 英文标点符号 "-"
	 */
	public final static String RAIL = "-";
	
	/**
	 * 英文标点符号 "*"
	 */
	public final static String ASTERISK = "*";
	
	/**
	 * （大小字母和数字的）字符集合
	 */
	private final static String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	 /**
	 * 判断指定字符串是否为空(null、""、" "都为空)
	 * @param s
	 * @return true表示 为空
	 */
	public static boolean isStrEmpty(String s){
		if (s == null || "".equals(s) || "".equals(s.trim()))
			return true;
		return false;
	}
	
	/**
	 * 验证输入的字母或数字格式是否正确
	 * @param s 待验证的字符串
	 * @return 是否合法 true合法
	 */
	public static boolean numberOrLetterValidate(String s){
		String strPattern = "^[0-9a-zA-Z]+$";
	    return validate(s,strPattern);
	}
	
	/**
	 * 将字节转换为十六进制字符串
	 * @param ib --byte*-- 字节
	 * @return 十六进制字符串
	 */
	public static String byteToHexStr(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        return new String(ob);
    }

	/**
	 * 将字节数组转换为十六进制字符串
	 * @param bytearray --byte[]*-- 字节数组
	 * @return 十六进制字符串
	 */
    public static String byteToStr(byte[] bytearray) {
        StringBuffer strDigest = new StringBuffer();
        for (int i = 0; i < bytearray.length; i++) {
            strDigest.append(byteToHexStr(bytearray[i]));
        }
        return strDigest.toString();
    }
    
    /**
     * 【计算字符串位数】
     * 一个中文汉字算两位，一个英文字母算一位，计算中文和英文混合的字符串长度
     * @param str
     * @return
     */
    public static int calculatePlaces(String str) {
    	int m = 0;
    	char arr[] = str.toCharArray();
    	for(int i=0;i<arr.length;i++){
    		char c = arr[i];
    		if((c >= 0x0391 && c <= 0xFFE5)){	//中文字符
    			m = m + 2;  
    		}
    		else if((c>=0x0000 && c<=0x00FF)){	//英文字符
    			m = m + 1;  
    		}  
        }
    	return m;
    }
    
	/**
	 * 通用效验方法
	 * @param s
	 * @param strPattern
	 * @return
	 */
	private static boolean validate(String s,String strPattern){
		if (s == null)
			return false;
		
		Pattern pattern = null;
		Matcher matcher = null;
		
		try {
			pattern = Pattern.compile(strPattern);
			matcher = pattern.matcher(s);
			if (!matcher.find())
				return false;
		} finally {
			pattern = null;
			matcher = null;
		}
		
	    return true;
	}
	
	/**
	 * 获取指定位数的随机数字串
	 * @param bit --int-- 指定位数
	 * @return 随机数字串
	 */
	public static String getRandomNumber(int bit){
		if (bit == 0)
			return "";
		
		StringBuffer tmpBuffer = new StringBuffer();
		Random random = new Random();
		for (int i = 0;i < bit;i++){
			tmpBuffer.append(random.nextInt(10));
		}
		String result = tmpBuffer.toString();
		
		tmpBuffer = null;
		random = null;
		
		return result;
	}
	
	/**
	 * 获取退款批次号（长度18位）【格式为：退款日期（8位当天日期）+流水号（10位0-9的随机数）】
	 */
	public static String getRefundBatchNo(){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(getCurrentDate(TIME_FORMAT_EIGHT));//+8位当天日期
		
		Random random = new Random();
		int size = 5;
		for (int i = 0;i < size;i++) {
			tmpBuffer.append(random.nextInt(10)).append(random.nextInt(9) + 1);//+10
		}
		
		String returnStr = tmpBuffer.toString();
		
		random = null;
		tmpBuffer = null;
		
		return returnStr;
	}
	
	
	/**
	 * 格式化日期时间 （格式：yyyyMMdd）
	 * @param date --String*-- 日期时间字符串（格式：yyyy-MM-dd）
	 * @return
	 */
	public static String formatTimeFrom10bitTo8bit_A(String date){
		if (isStrEmpty(date))
			return null;
		if (date.length() != 10)
			return null;
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpStr = tmpBuffer.append(date.substring(0, 4)).append(date.substring(5,7)).append(date.substring(8,10)).toString();
		
		tmpBuffer = null;
		
		return tmpStr;
	}
	
	/**
	 * 格式化日期时间 （格式：yyyyMMdd）
	 * @param date --String*-- 日期时间字符串（格式：yyyy年MM月dd日）
	 * @return
	 */
	public static String formatTimeFrom11bitTo8bit_B(String date){
		if (isStrEmpty(date))
			return null;
		if (date.length() != 11)
			return null;
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpStr = tmpBuffer.append(date.substring(0, 4)).append(date.substring(5,7)).append(date.substring(8,10)).toString();
		
		tmpBuffer = null;
		
		return tmpStr;
	}
	
	/**
	 * 格式化日期时间 （格式：yyyy-MM-dd）
	 * @param date --String*-- 日期时间字符串（格式：yyyyMMdd）
	 * @return 新的日期时间（格式：yyyy-MM-dd）
	 */
	public static String formatTimeFrom8bitTo10bit_A(String date){
		if (isStrEmpty(date))
			return null;
		if (date.length() != 8)
			return null;
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpStr = tmpBuffer.append(date.substring(0, 4)).append("-").append(date.substring(4, 6)).append("-").append(date.substring(6, 8)).toString();
		
		tmpBuffer = null;
		
		return tmpStr;
	}
	
	/**
	 * 格式化日期时间 （格式：yyyy年MM月dd日）
	 * @param date --String*-- 日期时间字符串（格式：yyyyMMdd）
	 * @return 新的日期时间（格式：yyyy年MM月dd日）
	 */
	public static String formatTimeFrom8bitTo10bit_B(String date){
		if (isStrEmpty(date))
			return null;
		if (date.length() != 8)
			return null;
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpStr = tmpBuffer.append(date.substring(0, 4)).append("年").append(Integer.parseInt(date.substring(4, 6))).append("月").append(Integer.parseInt(date.substring(6, 8))).append("日").toString();
		
		tmpBuffer = null;
		
		return tmpStr;
	}
	
	/**
	 * 格式化日期时间 （格式：yyyy/MM/dd）
	 * @param date --String*-- 日期时间字符串（格式：yyyyMMdd）
	 * @return 新的日期时间（格式：yyyy/MM/dd）
	 */
	public static String formatTimeFrom8bitTo10bit_C(String date){
		if (isStrEmpty(date))
			return null;
		if (date.length() != 8)
			return null;
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpStr = tmpBuffer.append(date.substring(0, 4)).append("/").append(date.substring(4, 6)).append("/").append(date.substring(6, 8)).toString();
		
		tmpBuffer = null;
		
		return tmpStr;
	}
	
	/**
	 * 格式化日期时间字符串
	 * @param dateTime --String*-- 日期时间字符串
	 * @param format --String*-- 日期时间格式（默认格式：yyyy-MM-dd HH:mm:ss）
	 * @return 日期时间对象
	 */
	public static Date formatTimeFromStringToDate(String dateTime, String format){
		Date date = null;
		if (isStrEmpty(dateTime))
			return date;
		
		if (isStrEmpty(format))
			format = TIME_FORMAT_THIRTEEN;
		
		SimpleDateFormat simpleDateFormat = null;
		try {
			simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(dateTime);
			
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			//清空
			simpleDateFormat = null;
		}
		
		return date;
	}
	
	
	/**
	 * 格式化日期时间字符串
	 * @param dateTime --Date*-- 日期时间对象
	 * @param format --String*-- 日期时间格式（默认格式：yyyy-MM-dd HH:mm:ss）
	 * @return 日期时间字符串
	 */
	public static String formatTimeFromDateToString(Date dateTime, String format){
		String date = null;
		if (dateTime == null)
			return date;
		
		if (isStrEmpty(format))
			format = TIME_FORMAT_THIRTEEN;
		
		SimpleDateFormat simpleDateFormat = null;
		try {
			simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.format(dateTime);
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			//清空
			simpleDateFormat = null;
		}
		
		return date;
	}
	
	/**
	 * 将秒数形式的时间转换成java.uti.Data对象
	 * @param secondsNumber --long*-- 时间（以从历元至现在所经过的 UTC 秒数形式）
	 * @return java.uti.Data对象
	 */
	public static Date formatTimeFromSecondsNumber(long secondsNumber){
		return formatTimeFromMilliseSecondsNumber(secondsNumber * 1000);
	}
	
	/**
	 * 将毫秒数形式的时间转换成java.uti.Data对象
	 * @param millisecondsNumber --long*-- 时间（以从历元至现在所经过的 UTC 毫秒数形式）
	 * @return java.uti.Data对象
	 */
	public static Date formatTimeFromMilliseSecondsNumber(long millisecondsNumber){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millisecondsNumber);
		Date date = calendar.getTime();
		
		calendar = null;
		
		return date;
	}
	
	/**
	 * 将日期字符串转换成秒数形式的时间
	 * @param date --String*-- 日期字符串（格式yyyy-MM-dd）
	 * @return 秒数形式的时间
	 */
	public static long formatMilliseSecondsNumberFromDate(String date){
		return formatMilliseSecondsNumberFromDateTime(date, TIME_FORMAT_TEN);
	}
	
	/**
	 * 将日期时间字符串转换成秒数形式的时间
	 * @param dateTime --String*-- 日期时间字符串
	 * @param format --String*-- 日期时间格式
	 * @return 秒数形式的时间
	 */
	public static long formatMilliseSecondsNumberFromDateTime(String dateTime, String format){
		long milliseSeconds = -1;
		if (isStrEmpty(dateTime))
			return milliseSeconds;
		if (isStrEmpty(format))
			return milliseSeconds;
		
		SimpleDateFormat simpleDateFormat = null;
		Date date = null;
		Calendar calendar = null;
		try {
			simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(dateTime);
			calendar = Calendar.getInstance();
			calendar.setTime(date);
			milliseSeconds = calendar.getTimeInMillis();
			milliseSeconds = milliseSeconds / 1000;
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			//清空
			simpleDateFormat = null;
			date = null;
			calendar = null;
		}

		return milliseSeconds;
	}
	
	/**
	 * 获取指定时间（格式为：yyyy-MM-dd HH:mm:ss）的N小时后的时间
	 * @param specialTimeOf19bit --String*-- 指定时间（格式为：yyyy-MM-dd HH:mm:ss）
	 * @param hours --int*-- 小时数（大于0表示N小时后，小于0则表示N小时前）
	 * @return null 或 java.uti.Data对象 
	 */
	public static Date getDateAfterHoursFromSpecialTimeOf19bit(String specialTimeOf19bit, int hours){
		Date date = null;
		
		if (isStrEmpty(specialTimeOf19bit))
			return date;
		
		SimpleDateFormat simpleDateFormat = null;
		Calendar calendar = null;
		try {
			simpleDateFormat = new SimpleDateFormat(TIME_FORMAT_THIRTEEN);
			date = simpleDateFormat.parse(specialTimeOf19bit);
			
			calendar = Calendar.getInstance();
			calendar.setTime(date);
	        calendar.add(Calendar.HOUR_OF_DAY, hours);
	        date = calendar.getTime();
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			//清空
			simpleDateFormat = null;
			calendar = null;
		}
		
		return date;
	}
	
	/**
	 * 获取指定时间（格式为：yyyy-MM-dd HH:mm:ss）的N分钟后的时间
	 * @param date --Date-- 指定时间（默认值为当前时间）
	 * @param days --int*-- 天数（大于0表示N天后，小于0则表示N天前）
	 * @param hours --int*-- 小时数（大于0表示N小时后，小于0则表示N小时前）
	 * @param mins --int*-- 分钟数（大于0表示N分钟后，小于0则表示N分钟前）
	 * @return null 或 java.uti.Data对象 
	 */
	public static Date getDateAfterDaysOrHoursOrMinutesFromDate(Date date, int days, int hours, int mins){
		Date newDate = null;
		
		Calendar calendar = null;
		try {
			calendar = Calendar.getInstance();
			if (date != null)
				calendar.setTime(date);
	        calendar.add(Calendar.DAY_OF_YEAR, days);
	        calendar.add(Calendar.HOUR_OF_DAY, hours);
	        calendar.add(Calendar.MINUTE, mins);
	        newDate = calendar.getTime();
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			//清空
			calendar = null;
		}
		
		return newDate;
	}
	
	/**
	 * 获取指定时间（格式为：yyyy-MM-dd HH:mm:ss）的N分钟后的时间
	 * @param specialTimeOf19bit --String-- 指定时间（格式为：yyyy-MM-dd HH:mm:ss）（默认值为当前时间）
	 * @param days --int*-- 天数（大于0表示N天后，小于0则表示N天前）
	 * @param hours --int*-- 小时数（大于0表示N小时后，小于0则表示N小时前）
	 * @param mins --int*-- 分钟数（大于0表示N分钟后，小于0则表示N分钟前）
	 * @return null 或 java.uti.Data对象 
	 */
	public static Date getDateAfterDaysOrHoursOrMinutesFromSpecialTimeOf19bit(String specialTimeOf19bit, int days, int hours, int mins){
		Date date = null;
		
		if (isStrEmpty(specialTimeOf19bit))
			return date;
		
		SimpleDateFormat simpleDateFormat = null;
		Calendar calendar = null;
		try {
			calendar = Calendar.getInstance();
			
			if (!ToolUtil.isStrEmpty(specialTimeOf19bit)){
				simpleDateFormat = new SimpleDateFormat(TIME_FORMAT_THIRTEEN);
				date = simpleDateFormat.parse(specialTimeOf19bit);
				calendar.setTime(date);
			}
			
	        calendar.add(Calendar.DAY_OF_YEAR, days);
	        calendar.add(Calendar.HOUR_OF_DAY, hours);
	        calendar.add(Calendar.MINUTE, mins);
	        date = calendar.getTime();
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			//清空
			simpleDateFormat = null;
			calendar = null;
		}
		
		return date;
	}
	
	/**
	 * 将时间字符串（格式yyyy-MM-dd HH:mm:ss）转换成java.uti.Data对象
	 * @param strTime --String*-- 时间字符串（格式yyyy-MM-dd HH:mm:ss）
	 * @return null 或 java.uti.Data对象
	 */
	public static Date formatTimeFromStringOf19bit(String strTime){
		return formatTimeFromString(strTime, TIME_FORMAT_THIRTEEN);
	}
	
	/**
	 * 将时间字符串转换成java.uti.Data对象
	 * @param strTime --String*-- 时间字符串
	 * @param format --String*-- 时间格式 （如：yyyy-MM-dd HH:mm:ss）
	 * @return null 或 java.uti.Data对象
	 */
	public static Date formatTimeFromString(String strTime, String format){
		Date date = null;
		
		if (isStrEmpty(strTime) || isStrEmpty(format))
			return date;
		
		SimpleDateFormat simpleDateFormat = null;
		try {
			simpleDateFormat = new SimpleDateFormat(format);
			date = simpleDateFormat.parse(strTime);
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			//清空
			simpleDateFormat = null;
		}
		
		return date;
	}
	
	/**
	 * （另一个）字符串转义（若参数为null，则返回""）
	 * @param s --String*-- 指定字符串
	 * @return "" 或 转义后的字符串
	 */
	public static String quoteReplacementOfMatcher_(String s){
		if (isStrEmpty(s))
			return STR_EMPTY;
		return Matcher.quoteReplacement(s);
	}
	
	public static String getNewStringByIntercept(String s, int maxlength){
		if (s == null || maxlength <= 3)
			return s;
		if (s.length() <= maxlength)
			return s;
		return s.substring(0, maxlength - 3).concat("...");
	}
	
	/**
	 * 获得当前时间
	 * @return 时间格式为yyyyMMddHHmmss
	 */
	public static String getOtherSysDateAll(){
		return getCurrentDate(TIME_FORMAT_FOURTEEN);
	}
	
	/**
	 * 获得当前时间
	 * @return 时间格式为yyyy-MM-dd HH:mm:ss
	 */
	public static String getSysDateAll(){
		return getCurrentDate(TIME_FORMAT_THIRTEEN);
	}
	
	/**
	 * 获得当前时间
	 * @return Date对象
	 */
	public static Date getSysDateAsDate() {
		return Calendar.getInstance().getTime();
	}
	
	/**
	 * 根据格式，获取当前时间
	 * @param format 时间格式，默认为"yyyy-MM-dd"
	 * 	"2003-02-01"标示为"yyyy-MM-dd",
	 *  24小时："23:12:21"-->"HH:mm:ss" 
	 *  12小时：设为"hh:mm:ss"
	 * @return 当前时间字符串
	 */
	public static String getCurrentDate(String format){
		return formatDate(Calendar.getInstance().getTime(),format);
	}
	
	/**
	 * 获取当天的前n天（n小于0）或后n天(n大于0)的日期
	 * @param n
	 * @return 日期（格式：yyyy-MM-dd）
	 */
	public static String getPreOrNextDayFromCurrentDate(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, n);
		return formatDate(calendar.getTime(), null);
	}
	
	/**
	 * 格式化时间
	 * @param date 时间对象
	 * @param format 时间格式，默认为"yyyy-MM-dd"
	 * @return 格式化后的时间
	 */
	public static String formatDate(Date date,String format){
		if (isStrEmpty(format))
			format = TIME_FORMAT_TEN;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		return dateFormat.format(date);
	}
	
	/**
	 * 根据格式化时间得到毫秒数
	 * @param date 格式化时间对象
	 * @param format 时间格式，默认为"yyyy-MM-dd HH:mm:ss"
	 * @return 格式化后的时间
	 */
	public static long formatDateMills(String date, String format) throws Exception{
		if (isStrEmpty(format))
			format = TIME_FORMAT_THIRTEEN;
		
		SimpleDateFormat dateFormat = null;
		Date formatDate = null;
		long dateMills = 0;
		
		try {
			dateFormat = new SimpleDateFormat(format);
			formatDate = dateFormat.parse(date);
			dateMills = formatDate.getTime();
		} catch (Exception e) {
			throw e;
		} finally {
			dateFormat = null;
			formatDate = null;
		}
		
		return dateMills;
	}
	
	/**
	 * 字符串转义（若参数为null，则返回null）
	 * @param s --String*-- 指定字符串
	 * @return null 或 转义后的字符串
	 */
	public static String quoteReplacementOfMatcher(String s){
		if (isStrEmpty(s))
			return s;
		return Matcher.quoteReplacement(s);
	}
	
	/**
	 * 获取指定位数的随机字符串
	 * @param bit --int-- 指定位数
	 * @return 随机字符串
	 */
	public static String getRandomCharacter(int bit){
		if (bit == 0)
			return "";
		
		StringBuffer tmpBuffer = new StringBuffer();
		Random random = new Random();
		int length = BASE62_CHARS.length();
		int index = 0;
		for (int i = 0;i < bit;i++){
			index = random.nextInt(length);
			tmpBuffer.append(BASE62_CHARS.substring(index, index + 1));
		}
		String result = tmpBuffer.toString();
		
		tmpBuffer = null;
		random = null;
		
		return result;
	}
	
	/**
	 * List类型对象转换成String型,多个元素以,相隔
	 * @param list
	 * @return String型
	 */
	@SuppressWarnings("rawtypes")
	public static String formatListToString(List list){
		if (list == null)
			return null;
		if (list.size() == 0)
			return STR_EMPTY;
		StringBuilder buffer = new StringBuilder();
		for (Object obj : list){
			if (obj == null)
				continue;
			buffer.append(COMMA).append(obj.toString().trim());
		}
		if (buffer.length() > 0)
			buffer.deleteCharAt(0);
		return buffer.toString();
	}
	
	/**
	 * 字符串转换成List类型
	 * @param ids --String*-- 多个ID以间隔符相连
	 * @param blankCharacter --String-- 间隔符号（默认,）
	 * @return List类型
	 */
	public static List<String> formatStringToList(String ids, String blankCharacter){
		if (isStrEmpty(ids))
			return null;
		
		if (isStrEmpty(blankCharacter))
			blankCharacter = ",";
		
		List<String> rstList = new ArrayList<String>();
		
		String[] array = ids.split(blankCharacter);
		for (String s : array){
			rstList.add(s);
		}
		
		array = null;
		
		return rstList;
	}
	
	/**
	 * 字符串转换成List类型（去重复）
	 * @param ids --String*-- 多个ID以间隔符相连
	 * @param blankCharacter --String-- 间隔符号（默认,）
	 * @return List类型
	 */
	public static List<String> formatStringToListWithClearRepeat(String ids, String blankCharacter){
		if (isStrEmpty(ids))
			return null;
		
		if (isStrEmpty(blankCharacter))
			blankCharacter = ",";
		
		List<String> rstList = new ArrayList<String>();
		
		String[] array = ids.split(blankCharacter);
		for (String s : array){
			if (rstList.contains(s))
				continue;
			rstList.add(s);
		}
		
		array = null;
		
		return rstList;
	}
	
	/**
	 * 判断字符数组中是否包含指定字符串
	 * @param array
	 * @param value
	 * @return true表示包含
	 */
	public static boolean checkContainValueFromArray(String[] array, String value){
		if (array == null || value == null)
			return false;
		for (String s : array){
			if (value.equals(s))
				return true;
		}
		return false;
	}
	
	/**
	 * 获取当月某天是星期几
	 * @param year 年
	 * @param month 月份
	 * @param day 日期
	 * @return 星期几 (1-星期日,2-星期一,...,7-星期六)
	 */
	public static int getWeek(int year,int month,int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 得到八位数字型的当前日期
	 * @return (Integer) yyyyMMdd型当前日期
	 */
	public static Integer getSysDateInInteger() {
		return Integer.valueOf(getCurrentDate(TIME_FORMAT_EIGHT));
	}
	
	/**
	 * 得到八位数字型的当前日期
	 * @return (Long) yyyyMMdd型当前日期
	 */
	public static Long getSysDateInLong() {
		return Long.valueOf(getCurrentDate(TIME_FORMAT_EIGHT));
	}
	
	/**
	 * 根据完整的日期，获取月日信息
	 * @param date 日期（格式为yyyyMMdd）
	 * @return 月日信息（格式m月d日）
	 */
	public static String getMonthAndDayFromWholeDate(Long date){
		if (date == null)
			return null;
		int month = Integer.parseInt(date.toString().substring(4, 6));
		int day = Integer.parseInt(date.toString().substring(6, 8));
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpDate = tmpBuffer.append(month).append("月").append(day).append("日").toString();
		
		tmpBuffer = null;
		
		return tmpDate;
	}
	
	/**
	 * 将20090321型日期转换成"yyyy-MM-dd"
	 * @param date 8位数字型日期
	 * @return "yyyy-MM-dd"日期
	 */
	public static String getSysDateInString(Long date) {
		String sdate = String.valueOf(date);
		StringBuffer buffer = new StringBuffer();
		buffer.append(sdate.substring(0,4)).append(RAIL).append(sdate.substring(4,6)).append(RAIL).append(sdate.substring(6));
		return buffer.toString();		
	}
	
	/**
	 * 根据完整的日期，获取年月日信息
	 * @param date 日期（格式为yyyyMMdd）
	 * @return 年月日信息（格式yyyy年m月d日）
	 */
	public static String getYearAndMonthAndDayFromWholeDate(Long date){
		if (date == null)
			return null;
		
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(4, 6));
		int day = Integer.parseInt(date.toString().substring(6, 8));
		
		StringBuffer tmpBuffer = new StringBuffer();
		String tmpDate = tmpBuffer.append(year).append("年").append(month).append("月").append(day).append("日").toString();
		
		tmpBuffer = null;
		
		return tmpDate;
	}
	
	/**
	 * 根据格式，获取当月第一天
	 * @param format 时间格式，默认为"yyyy-MM-dd"
	 * @return 当月第一天时间字符串
	 */
	public static String getFirstDayOfMonth(String format){
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE,1);//设为当前月的1号
		return formatDate(lastDate.getTime(),format);
	}
	
	/**
	 * 格式化日期
	 * @param date --String*-- 待格式化的日期（格式为"yyyy-MM-dd"或"yyyy-m-d"）
	 * @return 日期（格式为"yyyy-MM-dd"）
	 */
	public static String getStingDate(String date) {
		if (date == null)
			return null;
		
		String[] array = date.split("-");
		if (array == null || array.length != 3)
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(array[0]);
		tmpBuffer.append("-");
		if (array[1].length() == 1)
			tmpBuffer.append("0");
		tmpBuffer.append(array[1]);
		tmpBuffer.append("-");
		if (array[2].length() == 1)
			tmpBuffer.append("0");
		tmpBuffer.append(array[2]);
		String newdate = tmpBuffer.toString();
		
		array = null;
		tmpBuffer = null;

		return newdate;
	}
	
	/**
	 * 格式化日期
	 * @param strDate --String*-- 待格式化的日期（格式为"yyyy-MM-dd"或"yyyy-m-d"）
	 * @return 日期（格式为yyyyMMdd）
	 */
	public static Long formatDateFromString(String strDate){
		if (strDate == null)
			return null;
		
		String[] array = strDate.split("-");
		if (array == null || array.length != 3)
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(array[0]);	//4
		if (array[1].length() == 1)
			tmpBuffer.append("0");
		tmpBuffer.append(array[1]);
		if (array[2].length() == 1)
			tmpBuffer.append("0");
		tmpBuffer.append(array[2]);
		Long newdate = Long.valueOf(tmpBuffer.toString());
		
		array = null;
		tmpBuffer = null;

		return newdate;
	}
	
	/**
	 * 得到某天之后N天日期
	 * @param someDay yyyyMMdd数字型日期
	 * @param days N天
	 * @return  yyyyMMdd数字型日期
	 */
	@SuppressWarnings("deprecation")
	public static Integer getSomeDayInIntegerAfterDays(Integer someDay,int days) {
		Date d = getSysDateInDate(String.valueOf(someDay),TIME_FORMAT_EIGHT);
		d.setDate(d.getDate() + days);
		return Integer.valueOf(formatDate(d,TIME_FORMAT_EIGHT));
	}
	
	/**
	 * 得到某天之后N天日期
	 * @param someDay yyyyMMdd数字型日期
	 * @param days N天
	 * @return  yyyyMMdd数字型日期
	 */
	@SuppressWarnings("deprecation")
	public static Long getSomeDayInLongAfterDays(Long someDay, int days) {
		Date d = getSysDateInDate(String.valueOf(someDay),TIME_FORMAT_EIGHT);
		d.setDate(d.getDate() + days);
		return Long.valueOf(formatDate(d,TIME_FORMAT_EIGHT));
	}
	
	/**
	 * 比较指定日期到今天的年份差值（当前年份为减数，指定日期对应的年份为被减数）
	 * @param date --String*-- 指定日期（格式：yyyyMMdd）
	 * @return 年份差值
	 */
	public static Integer getDifferenceForYearFromSpecialYearToNow(String date){
		String specialYear = date.substring(0, 4);
		String currentYear = getCurrentDate(TIME_FORMAT_EIGHT).substring(0, 4);
		
		int diff = Integer.parseInt(currentYear) - Integer.parseInt(specialYear);
		
		specialYear = null;
		currentYear = null;
		
		return diff;
	}
	
	/**
	 * 得到某天之前N天日期
	 * @param someDay yyyyMMdd数字型日期
	 * @param days N天
	 * @return  yyyyMMdd数字型日期
	 */
	@SuppressWarnings("deprecation")
	public static Integer getSomeDayInIntegerBeforeDays(Integer someDay,int days) {
		Date d = getSysDateInDate(String.valueOf(someDay),TIME_FORMAT_EIGHT);
		d.setDate(d.getDate()-days);
		return Integer.valueOf(formatDate(d,TIME_FORMAT_EIGHT));
	}
	
	/**
	 * 获取当前时间的N小时之前的时间
	 * @param hours N小时
	 * @return 目标时间（格式为yyyy-MM-dd HH:mm:ss）
	 */
	public static String getSomeHoursBeforeCurrentDate(int hours){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -1 * hours);
		Date date = calendar.getTime();
		
		String time = formatDate(date, TIME_FORMAT_THIRTEEN);
		
		//清空
		calendar = null;
		date = null;
		
		return time;
	}
	
	/**
	 * 获取当前时间的N小时之后的时间
	 * @param hours N小时
	 * @return 目标时间（格式为yyyy-MM-dd HH:mm:ss）
	 */
	public static String getSomeHoursAfterCurrentDate(int hours){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, 1 * hours);
		Date date = calendar.getTime();
		
		String time = formatDate(date, TIME_FORMAT_THIRTEEN);
		
		//清空
		calendar = null;
		date = null;
		
		return time;
	}
	
	/**
	 * 日期的字符串形式转成Date
	 * @param date String 默认格式为yyyy-MM-dd
	 * @param String format 格式，null则表示默认
	 * @return Date 日期
	 */
	public static Date getSysDateInDate(String date,String format) {
		if (format == null)
			format = TIME_FORMAT_TEN;
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date sysdate = null;
		if (date == null) {
			return null;
		}
		try {
			sysdate = dateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sysdate;
	}
	
	/**
	 * 获取指定日期是星期几
	 * @param date --Integer*-- 日期（格式：yyyyMMdd）
	 * @return 星期几 (1-星期日,2-星期一,...,7-星期六)
	 */
	public static int getWeek(Integer date){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(date.toString().substring(0, 4)), Integer.parseInt(date.toString().substring(4,6)) - 1, Integer.parseInt(date.toString().substring(6,8)));
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 获取指定日期是星期几
	 * @param date --String*-- 日期（格式：yyyyMMdd）
	 * @return 星期几 (1-星期日,2-星期一,...,7-星期六)
	 */
	public static int getOtherWeek(String date){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(4,6)) - 1, Integer.parseInt(date.substring(6,8)));
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * 获取从指定日期开始的N天的所有日期的集合
	 * @param date --Integer*-- 指定日期（格式：yyyyMMdd）
	 * @param n --int*-- N天
	 * @return 日期集合（格式：yyyyMMdd）
	 */
	public static List<Integer> getDateListBetweenTwoDates(Integer date, int n){
		List<Integer> tmpList = new ArrayList<Integer>();
		tmpList.add(date);
		for (int i = 1;i < n;i++){
			tmpList.add(getSomeDayInIntegerAfterDays(date, i));
		}
		return tmpList;
	}
	
	/**
	 * 获取两个日期之间的天数差值
	 * @param startDate --Long*-- 开始日期（格式：yyyyMMdd）
	 * @param endDate --Long*-- 截止日期（格式：yyyyMMdd）
	 * @return 天数差值
	 */
	public static int getDaysBetweenTwoDates(Long startDate, Long endDate) {
		int days = 0;
		
		SimpleDateFormat simpleDateFormat = null;
		Calendar calendar = null;
		try {
			simpleDateFormat = new SimpleDateFormat(TIME_FORMAT_EIGHT);
			calendar = Calendar.getInstance();
			calendar.setTime(simpleDateFormat.parse(startDate.toString()));
			long time1 = calendar.getTimeInMillis();
			calendar.setTime(simpleDateFormat.parse(endDate.toString()));
			long time2 = calendar.getTimeInMillis();
			days = Integer.parseInt(String.valueOf((time2 - time1) / (1000 * 3600 * 24)));
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			simpleDateFormat = null;
			calendar = null;
		}
		
		return days;
	}
	
	/**
	 * 获取一个时间区间里所有日期的列表
	 * @param startDate --Long*-- 开始日期（格式：yyyyMMdd）
	 * @param endDate --Long*-- 截止日期（格式：yyyyMMdd）
	 * @return 日期列表
	 */
	public static List<Long> getDateListBetweenTwoDatesForDay(Long startDate, Long endDate){
		List<Long> tmpList = new ArrayList<Long>();
		
		Long tmpDate = null;
		try {
			if (startDate == null || endDate == null)
				return tmpList;

			int days = getDaysBetweenTwoDates(startDate, endDate);
			for (int i = 0;i <= days;i++){
				if (i == 0)
					tmpDate = startDate;
				else
					tmpDate = getSomeDayInLongAfterDays(startDate, i);
				
				tmpList.add(tmpDate);
			}
			
		} catch (Exception e){
			//e.printStackTrace();
		} finally {
			tmpDate = null;
		}

		return tmpList;
	}
	
    /**
     * 给某个时间增加（减少）天数，增加（减少）小时
     * @param date +或-
     * @param hour +或-
     * @return
     */
    public static Date addDayAndHour(Date date, int day, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, day);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }
    
    /**
	 * 获取指定位数的（由数字组成的）随机字符串
	 * @param length --int-- 指定位数
	 * @return 随机字符串
	 */
	public static String getRandomLengthNum(int length){
		StringBuilder tmpBuilder = new StringBuilder();
		Random random = new Random(); 
		for(int i=0;i<length;i++) {
			tmpBuilder.append(Math.abs(random.nextInt(10)));
		}
		String tmpStr = tmpBuilder.toString();
		
		tmpBuilder = null;
		random = null;
		
		return tmpStr;
	}
	
	/**
	 * 用指定符号连接两个指定字符串
	 * @param s1 --String-- 指定字符串1
	 * @param s2 --String-- 指定字符串2
	 * @param symbol --String-- 符号（默认,）
	 * @return 合并后的字符串
	 */
	public static String mergeStringByCoverCharacter(String s1, String s2, String symbol){
		if (isStrEmpty(symbol))
			symbol = COMMA;
		
		StringBuilder tmpBuilder = new StringBuilder();
		
		if (!isStrEmpty(s1))
			tmpBuilder.append(symbol).append(s1);
		
		if (!isStrEmpty(s2))
			tmpBuilder.append(symbol).append(s2);
		
		if (tmpBuilder.length() > 0)
			tmpBuilder.deleteCharAt(0);
		
		String s = tmpBuilder.toString();
		
		tmpBuilder = null;
		
		return s;
	}
	
	
	/**
	 * 用指定符号连接两个指定字符串
	 * @param s1 --String-- 指定字符串1
	 * @param s2 --String-- 指定字符串2
	 * @param s3 --String-- 指定字符串3
	 * @param symbol --String-- 符号（默认,）
	 * @return 合并后的字符串
	 */
	public static String mergeStringByCoverCharacter(String s1, String s2, String s3, String symbol){
		if (isStrEmpty(symbol))
			symbol = COMMA;
		
		StringBuilder tmpBuilder = new StringBuilder();
		
		if (!isStrEmpty(s1))
			tmpBuilder.append(symbol).append(s1);
		
		if (!isStrEmpty(s2))
			tmpBuilder.append(symbol).append(s2);
		
		if (!isStrEmpty(s3))
			tmpBuilder.append(symbol).append(s3);
		
		if (tmpBuilder.length() > 0)
			tmpBuilder.deleteCharAt(0);
		
		String s = tmpBuilder.toString();
		
		tmpBuilder = null;
		
		return s;
	}
	
	/**
	 * 用指定符号替换目标字符串上的指定位置上的字符
	 * @param s --String*-- 目标字符串
	 * @param index --int*-- 下标
	 * @param length --int*-- 长度
	 * @param symbol --String*-- 指定符号
	 * @return 转换后的字符串
	 */
	public static String formatStringByCoverCharacter(String s, int index, int length, String symbol){
		if (isStrEmpty(s))
			return s;
		
		if (index < 0 || length <= 0)
			return s;
		
		int size = s.length();
		if (size <= index)
			return s;
		
		if (isStrEmpty(symbol))
			symbol = ASTERISK;
		
		String s1 = "";//前
		if (index > 0)
			s1 = s.substring(0, index);
		
		String s2 = "";//后
		if (size > index + length)
			s2 = s.substring(index + length);
		
		StringBuilder tmpBuilder = new StringBuilder();
		tmpBuilder.append(s1);
		for (int i = 0;i < length;i++){
			tmpBuilder.append(symbol);
		}
		tmpBuilder.append(s2);
		String result = tmpBuilder.toString();
		
		if (result.length() > size)
			result = result.substring(0, size);
		
		//清空
		s1 = null;
		s2 = null;
		tmpBuilder = null;
		
		return result;
	}
	
	/**
	 * 获取指定位数的随机数
	 * @param counts --int*-- 指定位数
	 * @return 随机数
	 */
	public static String getRandomNumberString(int counts){
		String returnStr = "";
		if (counts <= 0)
			return returnStr;
		
		StringBuffer tmpBuffer = new StringBuffer();
		Random random = new Random();
		for (int i = 0;i < counts;i++)
			tmpBuffer.append(random.nextInt(10));
		returnStr = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;
		random = null;
		
		return returnStr;
	}
	
	/**
	 * 获取特殊的指定位数的随机数
	 * @param counts --int*-- 指定位数
	 * @return 随机数
	 */
	public static String getSpecialRandomNumberString(int counts){
		String returnStr = "";
		if (counts <= 0)
			return returnStr;
		
		StringBuffer tmpBuffer = new StringBuffer();
		Random random = new Random();
		for (int i = 0;i < counts;i++)
			tmpBuffer.append(random.nextInt(10));
		returnStr = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;
		random = null;
		
		return returnStr;
	}
	
	/**
	 * 比较两个字符串是否相等
	 * @param s1 --String-- 指定字符串1
	 * @param s2 --String-- 指定字符串2
	 * @return true表示相同
	 */
	public static boolean equalsWithString(String s1, String s2){
		if (null == s1)
			return (null == s2);
		else
			return s1.equals(s2);
	}
	
}