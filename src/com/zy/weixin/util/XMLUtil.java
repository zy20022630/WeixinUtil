package com.zy.weixin.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class XMLUtil {
	
	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static SortedMap<String, String> doXMLParse(String strxml) throws JDOMException, IOException {
		if (null == strxml || "".equals(strxml))
			return null;
		
		SortedMap<String, String> dataMap = null;
		InputStream inputStream = null;
		SAXBuilder saxBuilder = null;
		Document document = null;
		Element rootElement = null;
		List list = null;
		List children = null;
		Iterator iterator = null;
		Element element = null;
		String key = null;
		String value = null;

		try {
			strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
			
			dataMap = new TreeMap<String, String>();
			inputStream = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
			saxBuilder = new SAXBuilder();
			document = saxBuilder.build(inputStream);
			rootElement = document.getRootElement();
			list = rootElement.getChildren();
			iterator = list.iterator();
			
			while (iterator.hasNext()) {
				element = (Element)iterator.next();
				key = element.getName();
				value = "";
				
				children = element.getChildren();
				if (children.isEmpty())
					value = element.getTextNormalize();
				else 
					value = XMLUtil.getChildrenText(children);
				
				if (value != null)
					value = value.trim();
				
				dataMap.put(key, value);
			}
			
			return dataMap;
		} catch (Exception e) {
			return null;
		} finally {
			//清空
			if (inputStream != null){
				try {
					inputStream.close();
				} catch (Exception e) {
				} finally {
					inputStream = null;
				}
			}
			saxBuilder = null;
			document = null;
			rootElement = null;
			if (list != null)
				list.clear();
			list = null;
			if (children != null)
				children.clear();
			children = null;
			iterator = null;
			element = null;
			key = null;
			value = null;
		}
	}
	
	/**
	 * 获取子结点的xml
	 * @param children
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String getChildrenText(List children) {
		String rtnStr = "";
		if (children == null || children.isEmpty())
			return rtnStr;
		
		StringBuffer tmpBuffer = new StringBuffer();
		List list = null;
		Element element = null;
		String name = null;
		String value = null; 
		for (Object obj : children){
			element = (Element)obj;
			name = element.getName();
			value = element.getTextNormalize();
			list = element.getChildren();
			
			tmpBuffer.append("<").append(name).append(">");
			if(list != null && !list.isEmpty())
				tmpBuffer.append(XMLUtil.getChildrenText(list));
			tmpBuffer.append(value);
			tmpBuffer.append("</").append(name).append(">");
		}
		rtnStr = tmpBuffer.toString();
		
		//清空
		tmpBuffer = null;
		if (list != null)
			list.clear();
		list = null;
		element = null;
		name = null;
		value = null;
		
		return rtnStr;
	}
	
}