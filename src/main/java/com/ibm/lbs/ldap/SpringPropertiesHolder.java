package com.ibm.lbs.ldap;

import java.io.IOException;
import java.util.Properties;

/**
 * <p>
 * �ļ�����: SpringPropertiesHolder.java
 * </p>
 * <p>
 * �ļ�����: ����Spring�����Ա�
 * </p>
 * <p>
 * ��Ȩ����: ��Ȩ����(C)2012
 * </p>
 * <p>
 * ����ժҪ: ��Ҫ�������ļ������ݣ�������Ҫģ�顢�������ܵ�˵��
 * </p>
 * <p>
 * ����˵��: �������ݵ�˵��
 * </p>
 * <p>
 * �������: 2012-4-10
 * </p>
 * <p>
 * �޸ļ�¼1:
 * </p>
 * 
 * <pre>
 *    �޸�����:
 *    �� �� ��:
 *    �޸�����:
 * </pre>
 * <p>
 * �޸ļ�¼2����
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
	 * @author��Wuqingming 	        
	 * @date��2012-4-10
	 * @Description����ȡ����ֵ
	 * @param key ��
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
	 * @author��Wuqingming 	        
	 * @date��2012-4-10
	 * @Description����ȡ����ֵ
	 * @param key ��
	 * @param defaultValue Ĭ��ֵ����δ�ҵ����Ե�ʱ�򷵻ظ�Ĭ��ֵ
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
	 * @author��Wuqingming 	        
	 * @date��2012-4-10
	 * @Description����������ֵ
	 * @param key ��
	 * @param value ֵ
	 */
	public static void setProperty (String key, String value) {
		if (properties == null) {
			properties = new Properties();
		}
		properties.setProperty(key, value);
	}
}
