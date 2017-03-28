package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 业务层工具类
 * @author zy20022630
 */
public class ToolUtil {
	
	/**
	 * 提前自动发布场地的天数：14
	 */
	public final static int AUTO_RELEASE_FIELD_DAY = 14;

	/**
	 * 分页相关的Map的Key：FIRSTROW (页面起始记录数，从1开始)
	 */
	public final static  String PAGE_FIRSTROW = "FIRSTROW";
	
	/**
	 * 分页相关的Map的Key：MAXPAGE (页面允许展示的最大记录数)
	 */
	public final static  String PAGE_MAXPAGE = "MAXPAGE";
	
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
	 * DES加密解密的秘钥：ydb@1604
	 */
	public final static String DES_KEY = "ydb@1604";
	
	/**
	 * （大小字母和数字的）字符集合
	 */
	private final static String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	/**
	 * 判断str是否为空；
	 * @return true--空；false--非空
	 */
	public static boolean isStrEmpty(String str) {
		return ((str == null) || (str.trim().equals(STR_EMPTY)));
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
	 * 判断指定时间是否已过时
	 * @param appointedTime --String*-- 指定时间（格式：yyyy-MM-dd HH:mm:ss）
	 * @return true表示过时
	 */
	public static boolean overtimeWithCompareCurrenttime(String appointedTime){
		boolean isOvertime = false;
		if (isStrEmpty(appointedTime))
			return isOvertime;
		
		String currentTime = getOtherSysDateAll();	//时间格式为yyyyMMddHHmmss
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(appointedTime.substring(0, 4));//yyyy
		tmpBuffer.append(appointedTime.substring(5, 7));//MM
		tmpBuffer.append(appointedTime.substring(8, 10));//MM
		tmpBuffer.append(appointedTime.substring(11, 13));//HH
		tmpBuffer.append(appointedTime.substring(14, 16));//mm
		tmpBuffer.append(appointedTime.substring(17, 19));//ss
		String tmpTime = tmpBuffer.toString();
		isOvertime = Long.parseLong(tmpTime) <= Long.parseLong(currentTime);
		
		//清空
		currentTime = null;
		tmpBuffer = null;
		tmpTime = null;
		
		return isOvertime;
	}
	
	/**
	 * 合成时间字符串
	 * @param date --String*-- 日期（格式：yyyyMMdd）
	 * @param timePoint --Integer*-- 时间点（24h制，整点，从0开始）
	 * @return 格式为yyyy-MM-dd HH:mm:ss的时间字符串
	 */
	public static String getComplexTimeByDateAndTime(String date, Integer timePoint) {
		String complexTime = null;
		
		if (isStrEmpty(date) || timePoint == null)
			return complexTime;
		else if (date.length() != 8 || timePoint.intValue()< 0 || timePoint.intValue() > 23)
			return complexTime;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(date.substring(0, 4)).append("-").append(date.substring(4, 6)).append("-").append(date.substring(6, 8)).append(" ");
		if (timePoint.intValue() < 10)
			tmpBuffer.append("0");
		tmpBuffer.append(timePoint);
		tmpBuffer.append(":00:00");
		
		complexTime = tmpBuffer.toString();
		
		tmpBuffer = null;
		
		return complexTime;
	}
	
	/**
	 * 合成日期时间区间字符串
	 * @param date --String*-- 日期时间字符串（格式：yyyyMMdd）
	 * @param starttimepoint 开始时间点
	 * @param endtimepoint 截止时间点
	 * @return 新的日期时间区间字符串（格式：yyyy-mm-dd mm:ss-mm:ss）
	 */
	public static String getComplexTimeIntervalByDateAndTwoTimePoint_A(String date, int starttimepoint, int endtimepoint){
		if (isStrEmpty(date))
			return null;
		if (date.length() != 8)
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(date.substring(0, 4)).append("-").append(date.substring(4, 6)).append("-").append(date.substring(6, 8)).append(" ");
		if (starttimepoint < 10)
			tmpBuffer.append("0");
		tmpBuffer.append(starttimepoint).append(":00-");
		if (endtimepoint < 10)
			tmpBuffer.append("0");
		tmpBuffer.append(endtimepoint).append(":00");
		String tmpStr = tmpBuffer.toString();
		
		tmpBuffer = null;
		
		return tmpStr;
	}
	
	/**
	 * 合成日期时间区间字符串
	 * @param date --String*-- 日期时间字符串（格式：yyyyMMdd）
	 * @param starttimepoint 开始时间点
	 * @param endtimepoint 截止时间点
	 * @return 新的日期时间区间字符串（格式：yyyy年mm月dd日mm:ss-mm:ss）
	 */
	public static String getComplexTimeIntervalByDateAndTwoTimePoint_B(String date, int starttimepoint, int endtimepoint){
		if (isStrEmpty(date))
			return null;
		if (date.length() != 8)
			return null;
		
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(date.substring(0, 4)).append("年").append(Integer.parseInt(date.substring(4, 6))).append("月").append(Integer.parseInt(date.substring(6, 8))).append("日");
		if (starttimepoint < 10)
			tmpBuffer.append("0");
		tmpBuffer.append(starttimepoint).append(":00-");
		if (endtimepoint < 10)
			tmpBuffer.append("0");
		tmpBuffer.append(endtimepoint).append(":00");
		String tmpStr = tmpBuffer.toString();
		
		tmpBuffer = null;
		
		return tmpStr;
	}
	
	/**
	 * 获取APP用户的用户名（即韵动吧号）（长度16位）【格式为：当前时间（12位yyMMddHHmmss格式）+流水号（4位0-9的随机数）】
	 */
	public static String getNewLogonname(){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(getOtherSysDateAll());//+14
		
		int size = 18 - tmpBuffer.length();
		Random random = new Random();
		for (int i = 0;i < size;i++)
			tmpBuffer.append(random.nextInt(10));//+4
		
		String returnStr = tmpBuffer.substring(2);//-2

		random = null;
		tmpBuffer = null;
		
		return returnStr;
	}
	
	public static String getRandomNumber(int n){
		String returnStr = "";
		if (n <= 0)
			return returnStr;
		
		StringBuffer tmpBuffer = new StringBuffer();
		Random random = new Random();
		for (int i = 0;i < n;i++){
			tmpBuffer.append(random.nextInt(10));//+4
		}
		returnStr = tmpBuffer.toString();

		random = null;
		tmpBuffer = null;
		
		return returnStr;
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
	 * 获取订单UID（长度18位）【格式为：U/T（1位） + 当前时间（12位yyMMddHHmmss格式）+流水号（5位0-9的随机数）】
	 * @param type --int*-- 类型：0-充值订单、1-客户订单、2-场地订单、3-其他订单
	 * @return 
	 */
	public static String getOrderUID(int type){
		StringBuffer tmpBuffer = new StringBuffer();
		tmpBuffer.append(getOtherSysDateAll());//+14
		
		int size = 19 - tmpBuffer.length();
		Random random = new Random();
		for (int i = 0;i < size;i++)
			tmpBuffer.append(random.nextInt(10));//+5
		
		if (type == 0)
			tmpBuffer.insert(2, "C");//+1
		else if (type == 1)
			tmpBuffer.insert(2, "U");//+1
		else if (type == 2)
			tmpBuffer.insert(2, "T");//+1
		else 
			tmpBuffer.insert(2, "O");//+1
		
		String returnStr = tmpBuffer.substring(2);//-2

		random = null;
		tmpBuffer = null;
		
		return returnStr;
	}
	
	/**
	 * 获取订单确认码（长度6位）【格式为：流水号（6位0-9的随机数）】
	 */
	public static String getOrderConfirmCode(){
		StringBuffer tmpBuffer = new StringBuffer();
		int size = 6;
		Random random = new Random();
		for (int i = 0;i < size;i++)
			tmpBuffer.append(random.nextInt(10));//+6
		
		String returnStr = tmpBuffer.toString();
		
		random = null;
		tmpBuffer = null;
		
		return returnStr;
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
	 * 获取指定位数的随机字符串
	 * @param bit --int-- 指定位数
	 * @return 随机字符串
	 */
	public static String getRandomCharacter(int bit){
		if (bit == 0)
			return null;
		
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
		StringBuffer buffer = new StringBuffer();
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
	 * 根据完整的日期，获取"周几"字符
	 * @param date 日期（格式为yyyyMMdd）
	 * @return "周几"字符
	 */
	public static String getWeekStringFromWholeDate(Long date){
		if (date == null)
			return null;
		
		int year = Integer.parseInt(date.toString().substring(0, 4));
		int month = Integer.parseInt(date.toString().substring(4, 6));
		int day = Integer.parseInt(date.toString().substring(6, 8));
		
		int week = getWeek(year, month, day);	//星期几 (1-星期日,2-星期一,...,7-星期六)
		
		String strWeek = null;
		if (week == 1)
			strWeek = "周日";
		else if (week == 2)
			strWeek = "周一";
		else if (week == 3)
			strWeek = "周二";
		else if (week == 4)
			strWeek = "周三";
		else if (week == 5)
			strWeek = "周四";
		else if (week == 6)
			strWeek = "周五";
		else if (week == 7)
			strWeek = "周六";
		
		return strWeek;
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
	 * 获取一个时间区间里指定星期的所有日期的列表
	 * @param startDate --Long*-- 开始日期（格式：yyyyMMdd）
	 * @param endDate --Long*-- 截止日期（格式：yyyyMMdd）
	 * @param weekList --weekList*-- 星期几列表（1-星期日，2-星期一，7-星期六）
	 * @return 日期列表（格式：yyyyMMdd）
	 */
	public static List<Long> getDateListBetweenTwoDatesForWeeks(Long startDate, Long endDate, List<Integer> weekList){
		List<Long> tmpList = new ArrayList<Long>();
		
		Long tmpDate = null;
		Integer week = null;
		try {
			if (startDate == null || endDate == null || weekList == null || weekList.isEmpty())
				return tmpList;

			int days = getDaysBetweenTwoDates(startDate, endDate);
			for (int i = 0;i <= days;i++){
				if (i == 0)
					tmpDate = startDate;
				else
					tmpDate = getSomeDayInLongAfterDays(startDate, i);
				
				week = Integer.valueOf(getOtherWeek(tmpDate.toString()));
				if (!weekList.contains(week))
					continue;
				
				tmpList.add(tmpDate);
			}
			
		} catch (Exception e){
			//e.printStackTrace();
		} finally {
			tmpDate = null;
			week = null;
		}

		return tmpList;
	}
	
	/**
	 * 获取一个时间区间里指定星期的所有日期的列表
	 * @param startDate --Long*-- 开始日期（格式：yyyyMMdd）
	 * @param endDate --Long*-- 截止日期（格式：yyyyMMdd）
	 * @param week --Integer*-- 星期几（1-星期日，2-星期一，7-星期六）
	 * @return 日期列表（格式：yyyyMMdd）
	 */
	public static List<Long> getDateListBetweenTwoDatesForWeek(Long startDate, Long endDate, Integer week){
		List<Long> tmpList = new ArrayList<Long>();
		
		Long tmpDate = null;
		Integer tmpWeek = null;
		try {
			if (startDate == null || endDate == null || week == null)
				return tmpList;

			int days = getDaysBetweenTwoDates(startDate, endDate);
			for (int i = 0;i <= days;i++){
				if (i == 0)
					tmpDate = startDate;
				else
					tmpDate = getSomeDayInLongAfterDays(startDate, i);
				
				tmpWeek = Integer.valueOf(getOtherWeek(tmpDate.toString()));
				if (tmpWeek.intValue() != week.intValue())
					continue;
				
				tmpList.add(tmpDate);
			}
			
		} catch (Exception e){
			//e.printStackTrace();
		} finally {
			tmpDate = null;
			week = null;
		}

		return tmpList;
	}
	
}