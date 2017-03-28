package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

/**
 * 文件处理的工具类
 */
public class FileUtils {
	
	/**
	 * 英文符号 "."
	 */
	private final static String SPOT = ".";
	
	/**
	 * 高级缓冲区大小：20 * 1024(Bit)
	 */
	private final static int FILEUPLOAD_SIZETHRESHOLD = 20 * 1024;
	
	/**
	 * 英文符号 "/"
	 */
	private final static String SLASH = "/";

	/**
	 * 创建一个新的文件名（不含后缀名）
	 * @param length --int*-- 新文件名的长度（至少大于13）
	 * @return 新的文件名
	 */
	public static String gainNewFileName(int length){
		StringBuffer tmpBuffer = new StringBuffer();
		
		//部分一：系统时间
		tmpBuffer.append(System.currentTimeMillis());
		
		//部分二：随机数
		int size = length - tmpBuffer.length();
		if (size > 0){
			Random random = new Random();
			for (int i = 0;i < size;i++){
				tmpBuffer.append(random.nextInt(10));
			}
			random = null;
		}
		
		return tmpBuffer.toString();
	}
	
	/**
	 * 创建一个新的文件名（包含后缀名）
	 * @param fileName --String*-- 带后缀的文件名
	 * @param length --int*-- 新文件名的长度（至少大于13）
	 * @return 新的文件名
	 */
	public static String gainNewFileName(String fileName, int length){
		StringBuffer tmpBuffer = new StringBuffer();
		
		//部分一：系统时间
		tmpBuffer.append(System.currentTimeMillis());
		
		//部分二：随机数
		int size = length - tmpBuffer.length();
		if (size > 0){
			Random random = new Random();
			for (int i = 0;i < size;i++){
				tmpBuffer.append(random.nextInt(10));
			}
			random = null;
		}
		
		//部分三：后缀名
		if (fileName.indexOf(SPOT) != -1)
			tmpBuffer.append(fileName.substring(fileName.lastIndexOf(SPOT)));
		
		return tmpBuffer.toString();
	}
	
	/**
	 * 创建一个文件相对路径（不含文件名和后缀名）
	 * @param type --String*-- 文件类型名（如：CUST、STDM、PRDT、MESG、OQMK）
	 * @return 文件相对路径
	 */
	public static String gainFileRelativePath(String type){
		StringBuffer tmpBuffer = new StringBuffer();
		
		//当前系统日期
		String currentDate = ToolUtil.getCurrentDate("yyyyMMdd");
		
		//相对目录
		tmpBuffer.append(SLASH).append(type).append(currentDate.substring(0, 6))
				 .append(SLASH).append(currentDate.substring(6))
				 .append(SLASH);

		String relativePath = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;
		currentDate = null;
		
		//返回
		return relativePath;
	}
	
	/**
	 * 创建一个文件相对路径（包含文件名和后缀名）
	 * @param type --String*-- 文件类型名（如：CUST、STDM、PRDT、MESG、OQMK）
	 * @param fileName --String*-- 带后缀的文件名
	 * @param length --int*-- 新文件名的长度（至少大于13）
	 * @return 文件相对路径
	 */
	public static String gainFileRelativePath(String type, String fileName, int length){
		StringBuffer tmpBuffer = new StringBuffer();
		
		//当前系统日期
		String currentDate = ToolUtil.getCurrentDate("yyyyMMdd");
		
		//相对目录
		tmpBuffer.append(SLASH).append(type).append(currentDate.substring(0, 6))
				 .append(SLASH).append(currentDate.substring(6))
				 .append(SLASH);
		
		int size = tmpBuffer.length();
		
		//文件名部分一：系统时间
		tmpBuffer.append(System.currentTimeMillis());
		
		//文件名部分二：随机数
		size = length - (tmpBuffer.length() - size);
		if (size > 0){
			Random random = new Random();
			for (int i = 0;i < size;i++){
				tmpBuffer.append(random.nextInt(10));
			}
			random = null;
		}
		
		//文件名部分三：后缀名
		if (fileName.indexOf(SPOT) != -1)
			tmpBuffer.append(fileName.substring(fileName.lastIndexOf(SPOT)));
		
		String relativePath = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;
		currentDate = null;
		
		//返回
		return relativePath;
	}
	
	/**
	 * 生成(绝对路径的)指定目录，只生成当前目录文件夹，不生成上级文件夹，若指定目录存在则不生成
	 * @param absolutePath  --String*-- 指定目录（即文件夹）的绝对路径
	 * @return true表示生成成功 或 已存在
	 */
	public static boolean createFolder(String absolutePath){
		if (ToolUtil.isStrEmpty(absolutePath))
			return false;
		
		File file = new File(absolutePath);
		if (!file.exists()){
			file.mkdir();
		}else if (!file.isDirectory()){
			file.mkdir();
		}
		
		file = null;
		
		return true;
	}
	
	/**
	 * 生成指定文件（绝对路径的）所有必需目录，若指定目录已存在则不生成
	 * @param fileAbsolutePath  --String*-- 指定文件的绝对路径
	 * @return true表示生成成功 或 已存在
	 */
	public static boolean createDirectory(String fileAbsolutePath){
		if (ToolUtil.isStrEmpty(fileAbsolutePath))
			return false;
		
		File file = new File(fileAbsolutePath.substring(0, fileAbsolutePath.lastIndexOf(SLASH)));
		if (!file.exists())
			file.mkdirs();
		else if (!file.isDirectory())
			file.mkdirs();
		
		file = null;
		
		return true;
	}

	/**
	 * 根据文本类文件的绝对路径，读取文件内容
	 * @param absoluatePath --String*-- 文件的绝对路径
	 * @return 文件内容
	 */
	public static StringBuffer readFile(String absoluatePath){
		StringBuffer buffer = new StringBuffer();
		
		//判断路径是否为空
		if (absoluatePath == null || absoluatePath.equals("") || absoluatePath.trim().equals(""))
			return buffer;
		
		//判断路径对应的是否是文件，是否可读
		File file = new File(absoluatePath);
		if (file == null || !file.exists() || !file.isFile() || !file.canRead())
			return buffer;
		
		//开始读取文件内容
		FileInputStream fileInputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		String temp = null;
		
		try {
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			temp = bufferedReader.readLine();
			while (temp != null){
				buffer.append(temp);
				temp = bufferedReader.readLine();
			}
		} catch (IOException e) {
			//e.printStackTrace();
		} finally{
			file = null;
			if (fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (IOException e) {
				} finally{
					fileInputStream = null;
				}
			}
			if (inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
				} finally{
					inputStreamReader = null;
				}
			}
			if (bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
				} finally{
					bufferedReader = null;
				}
			}
			temp = null;
		}

		return buffer;
	}
	
	/**
	 * 读取文件，返回字节数组
	 * @param file --File*-- 文件对象
	 * @return null或字节数组
	 */
	public static byte[] readFile(File file) throws Exception {
		//定义返回值
		byte[] returnByteArray = null;
		
		//效验文件对象是否为null
		if (file == null)
			return returnByteArray;
		
		FileInputStream fileInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		byte[] tmpByteArray = null;
		int position = 0, bufferSize = 1024;
		try {
			fileInputStream = new FileInputStream(file);
			byteArrayOutputStream = new ByteArrayOutputStream(bufferSize);
			tmpByteArray = new byte[bufferSize];
			while ((position = fileInputStream.read(tmpByteArray)) != -1){
				byteArrayOutputStream.write(tmpByteArray, 0, position);
			}
			byteArrayOutputStream.flush();
			returnByteArray = byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileInputStream != null){
				try {
					fileInputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					fileInputStream = null;
				}
			}
			if (byteArrayOutputStream != null){
				try {
					byteArrayOutputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					byteArrayOutputStream = null;
				}
			}
			tmpByteArray = null;
			
		}
		
		return returnByteArray;
	}
	
	/**
	 * 根据字节数组数据，生成文件
	 * @param file --File*-- 文件对象
	 * @param dataByteArray --byte[]*-- 数据的字节数组
	 * @return true表示成功
	 */
	public static boolean createFile(File file, byte[] dataByteArray) throws Exception {
		//效验文件对象是否为null
		if (file == null)
			return false;
		
		//效验文件数据是否为null
		if (dataByteArray == null)
			return false;
		
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(dataByteArray);
			bufferedOutputStream.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			if (fileOutputStream != null){
				try {
					fileOutputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					fileOutputStream = null;
				}
			}
			if (bufferedOutputStream != null){
				try {
					bufferedOutputStream.close();
				} catch (Exception e) {
					//e.printStackTrace();
				} finally {
					bufferedOutputStream = null;
				}
			}
		}

		return true;
	}
	
	/**
	 * 复制文件
	 * @param fromFile --File*--源文件对象
	 * @param toFile --File*--目标文件对象
	 * @return true表示成功，false则表示失败
	 */
	public static boolean copyFile(File fromFile, File toFile){
		InputStream in = null;				//(缓冲)输入流
        OutputStream out = null;			//(缓冲)输出流
        FileInputStream fileIn = null;		//文件输入流
        FileOutputStream fileOut = null;	//文件输出流
        byte[] data = null;
        
        if (fromFile.length() <= 0)
        	return false;

        try {
        	fileIn = new FileInputStream(fromFile);
			fileOut = new FileOutputStream(toFile);
			in = new BufferedInputStream(fileIn,FILEUPLOAD_SIZETHRESHOLD);
			out = new BufferedOutputStream(fileOut,FILEUPLOAD_SIZETHRESHOLD);
			data = new byte[1];
			while(in.read(data)!=-1){   
				out.write(data);   
            }
			out.flush();//将缓冲区中的数据全部写出
			
			return true;
		} catch (FileNotFoundException e) {
			//return false;
		} catch (IOException e) {
			//return false;
		} finally{
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
				}
				in = null;
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
				}
				out = null;
			}
			if (fileIn != null){
				try {
					fileIn.close();
				} catch (IOException e) {
				}
				fileIn = null;
			}
			if (fileOut != null){
				try {
					fileOut.close();
				} catch (IOException e) {
				}
				fileOut = null;
			}
			data = null;
		}
		return false;
	}

	/**
	 * 复制文件
	 * @param fromFile --String*--源文件绝对目录
	 * @param toFile --String*--目标文件绝对目录
	 * @return true表示成功，false则表示失败
	 */
	public static boolean copyFile(String fromFile, String toFile){
		if (ToolUtil.isStrEmpty(fromFile))
			return false;
		
		if (ToolUtil.isStrEmpty(toFile))
			return false;
		
		File fromFileObj = new File(fromFile);
		File toFileObj = new File(toFile);
		if (fromFileObj == null || toFileObj == null)
			return false;
		
		if (!fromFileObj.isFile() || !fromFileObj.exists() || !fromFileObj.canRead())
			return false;
		
		if (toFileObj.exists())
			return false;
		
		return copyFile(fromFileObj, toFileObj);
	}
	
	/**
	 * 复制文件
	 * @param fromURL --URL*-- 源文件URL地址
	 * @param toFile --File*--目标文件对象
	 * @return true表示成功，false则表示失败
	 */
	public static boolean copyFileByURL(URL fromURL, File toFile){
		InputStream in = null;				//(缓冲)输入流
        OutputStream out = null;			//(缓冲)输出流
        InputStream urlIn = null;		//文件输入流
        FileOutputStream fileOut = null;	//文件输出流
        byte[] data = null;
        
        if (fromURL == null)
        	return false;

        try {
        	urlIn = fromURL.openConnection().getInputStream();
			fileOut = new FileOutputStream(toFile);
			in = new BufferedInputStream(urlIn, FILEUPLOAD_SIZETHRESHOLD);
			out = new BufferedOutputStream(fileOut,FILEUPLOAD_SIZETHRESHOLD);
			data = new byte[1];
			while(in.read(data)!=-1){   
				out.write(data);   
            }
			out.flush();//将缓冲区中的数据全部写出
			
			return true;
		} catch (FileNotFoundException e) {
			//return false;
		} catch (IOException e) {
			//return false;
		} finally{
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
				}
				in = null;
			}
			if (out != null){
				try {
					out.close();
				} catch (IOException e) {
				}
				out = null;
			}
			if (urlIn != null){
				try {
					urlIn.close();
				} catch (IOException e) {
				}
				urlIn = null;
			}
			if (fileOut != null){
				try {
					fileOut.close();
				} catch (IOException e) {
				}
				fileOut = null;
			}
			data = null;
		}
		return false;
	}
	
	/**
	 * 复制文件
	 * @param fromURL --String*--源文件URL地址
	 * @param toFile --String*--目标文件绝对目录
	 * @return true表示成功，false则表示失败
	 */
	public static boolean copyFileByURL(String fromURL, String toFile){
		boolean isOK = false;
		
		URL fromURLObj = null;
		File toFileObj = null;
		
		try {
			if (ToolUtil.isStrEmpty(fromURL))
				return false;
			
			if (ToolUtil.isStrEmpty(toFile))
				return false;
			
			fromURLObj = new URL(fromURL);
			toFileObj = new File(toFile);
			
			if (fromURLObj == null || toFileObj == null)
				return false;
			
			if (toFileObj.exists())
				return false;
			
			isOK = copyFileByURL(fromURLObj, toFileObj);
		} catch (MalformedURLException e) {
			//e.printStackTrace();
		} finally {
			fromURLObj = null;
			toFileObj = null;
		}
		
		return isOK;
	}
	
	/**
	 * 获取指定长度的随机数字字符串
	 * @param length --int*-- 字符串长度
	 * @return 随机数字字符串
	 */
	public static String getRandomLengthNum(int length){
    	StringBuffer tmpBuffer = new StringBuffer();
    	Random random = new Random();
    	for(int i = 0;i < length;i++) {
    		tmpBuffer.append(random.nextInt(10));
    	}
    	String tmpStr = tmpBuffer.toString();
    	
    	//清空
    	tmpBuffer = null;
    	random = null;
    	
    	return tmpStr;
    }
	
	/**
	 * 从指定文件夹里随机抽出一个文件
	 * @param folderPath --String*-- 指定文件夹
	 * @return 随机文件对象
	 */
	public static File getRandomFileFromSpecifiedFolder(String folderPath) {
		File rstFile = null;
		
		//定义临时变量
		File folderFile = null;
		File[] fileArray = null;
		Random random = null;
		try {
			if (folderPath == null || folderPath.trim().length() == 0)
				return null;
			
			folderFile = new File(folderPath);
			if (!folderFile.exists() || !folderFile.isDirectory())
				return null;
			
			fileArray = folderFile.listFiles();
			if (fileArray.length == 0)
				return null;
			
			if (fileArray.length == 1){
				rstFile = fileArray[0];
			} else {
				random = new Random();
				rstFile = fileArray[random.nextInt(fileArray.length)];
			}
			
			return rstFile;
		} catch (Exception e){
			return null;
		} finally {
			//清空
			folderFile = null;
			fileArray = null;
			random = null;
		}
	}
	
}