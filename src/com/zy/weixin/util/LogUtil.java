package com.zy.weixin.util;

import java.io.File;
import java.io.FileWriter;


public class LogUtil {

	public static void logResult(String path, String filePeffix, String message) {
        FileWriter writer = null;
        StringBuffer tmpBuffer = null;
        String currentDate = null;
        String currentTime = null;
        String fileName = null;
        String filePath = null;
        File file = null;
        
        try {
        	//参数效验
        	if (path == null || message == null)
        		return;
        	
    		//当前系统日期和时间
    		currentDate = ToolUtil.getCurrentDate("yyyyMMdd");
    		currentTime = ToolUtil.getOtherSysDateAll();

    		//日志文件全称
    		tmpBuffer = new StringBuffer();
    		if (filePeffix == null)
    			filePeffix = "";
    		fileName = tmpBuffer.append(filePeffix).append(currentTime).append(".txt").toString();
    		
    		//获取文件目录
    		filePath = tmpBuffer.delete(0, tmpBuffer.length()).append(path).append(File.separator).append(currentDate.substring(0, 6)).append(File.separator).append(currentDate.substring(6, 8)).toString();
    		
    		//若文件夹未创建，则要创建所有父目录
    		file = new File(filePath);
    		if (!file.exists())
    			file.mkdirs();
    		else if (!file.isDirectory())
    			file.mkdirs();
    		
    		//获取文件绝对路径
    		tmpBuffer.delete(0, tmpBuffer.length()).append(filePath).append(File.separator).append(fileName);
    		
    		//写入数据到文件中
            writer = new FileWriter(tmpBuffer.toString());
            writer.write(message);
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
        	//清空
        	tmpBuffer = null;
            currentDate = null;
            currentTime = null;
            fileName = null;
            filePath = null;
            file = null;
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                } finally{
                	writer = null;
                }
            }
        }
	}
	
}