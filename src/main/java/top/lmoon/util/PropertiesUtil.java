package top.lmoon.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static Properties configProperties = new Properties();
	static {
//		 InputStream is =
//		 PropertiesUtil.class.getClassLoader().getResourceAsStream("config/config.properties");
		try {
			InputStream is = new FileInputStream("config/config.properties");
			configProperties.load(is);
		} catch (IOException e) {
			ErrorUtil.sendError("载入config.properties配置出错！", e);
		}
	}

	public static Properties getConfigProperties(){
		return configProperties;
	}
	
	public static String getB2bicIp(){
		return getConfigProperties().getProperty("b2bic_ip");
	}
	
	/**
	 * b2bic socket通讯端口，默认7072
	 * @return
	 */
	public static int getB2bicPort(){
		String v = getConfigProperties().getProperty("b2bic_port");
		return StringUtil.toInt(v, 7072);
	}
	
	/**
	 * b2bic socket超时时间（秒），默认90秒
	 * @return
	 */
	public static int getB2bicSoTimeout(){
		String v = getConfigProperties().getProperty("b2bic_soTimeout");
		return StringUtil.toInt(v, 90);
	}
	
	//b2bic_businessCode
	public static String getB2bicBusinessCode(){
		return getConfigProperties().getProperty("b2bic_businessCode");
	}
	
	/**
	 * 数据库分页查询每页大小，默认100
	 * @return
	 */
	public static int getSelectPageSize(){
		String v = getConfigProperties().getProperty("select_page_size");
		return StringUtil.toInt(v, 100);
	}
	
	public static int getSendPayTimes(){
		String p = getConfigProperties().getProperty("send_pay_times");
		int pInt = Integer.parseInt(p);
		return pInt;
	}
	
	public static String getCreateBy(){
		return configProperties.getProperty("create_by");
	}
	
	/**
	 * 单笔付款结果查询重试限定时长（秒），默认3600秒
	 * @return
	 */
	public static long getPayResultRetryDuration(){
		String v = getConfigProperties().getProperty("pay_result_retry_duration");
		return StringUtil.toLong(v, 3600);
	}
	
	/**
	 * 付款结果查询间隔（毫秒），默认10000毫秒
	 * @return
	 */
	public static long getPayResultRequestInterval(){
		String v = getConfigProperties().getProperty("pay_result_request_interval");
		return StringUtil.toLong(v, 10000);
	}
	
	/**
	 * 银行编码
	 * @return
	 */
	public static String getBankCode(){
		return configProperties.getProperty("bank_code");
	}
	
}
