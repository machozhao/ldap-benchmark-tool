package com.ibm.lbs.ldap;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>
 * 文件名称: SpringPropertiesHolder.java
 * </p>
 * <p>
 * 文件描述: 基于Spring的属性柄
 * </p>
 * <p>
 * 版权所有: 版权所有(C)2012
 * </p>
 * <p>
 * 内容摘要: 简要描述本文件的内容，包括主要模块、函数及能的说明
 * </p>
 * <p>
 * 其他说明: 其它内容的说明
 * </p>
 * <p>
 * 完成日期: 2012-4-10
 * </p>
 * <p>
 * 修改记录1:
 * </p>
 * 
 * <pre>
 *    修改日期:
 *    修 改 人:
 *    修改内容:
 * </pre>
 * <p>
 * 修改记录2：…
 * </p>
 * 
 * @author Wuqingming
 */
public class SpringPropertiesHolder {
	
	private static Properties properties = null;

	protected SpringPropertiesHolder() {
		super();
	}

	/**
	 * @param defaults
	 * @throws IOException
	 */
	protected SpringPropertiesHolder(
			SpringPropertyPlaceholderConfigurer propertyPlaceholderConfigurer)
			throws IOException {
		properties = new Properties (propertyPlaceholderConfigurer.getProperties());
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2012-4-10
	 * @Description：获取属性值
	 * @param key 键
	 * @return
	 */
	public static String getProperty (String key) {
		if (properties == null) {
			return null;
		}
		return properties.getProperty(key);
	}
	
	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2012-4-10
	 * @Description：获取属性值
	 * @param key 键
	 * @param defaultValue 默认值，当未找到属性的时候返回该默认值
	 * @return
	 */
	public static String getProperty (String key, String defaultValue) {
		if (properties == null) {
			return defaultValue;
		}
		return properties.getProperty(key, defaultValue);
	}

	/**
	 * 
	 * @author：Wuqingming 	        
	 * @date：2012-4-10
	 * @Description：设置属性值
	 * @param key 键
	 * @param value 值
	 */
	public static void setProperty (String key, String value) {
		if (properties == null) {
			properties = new Properties();
		}
		properties.setProperty(key, value);
	}
}
