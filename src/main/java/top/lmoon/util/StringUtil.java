package top.lmoon.util;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	
	/**
	 * String转为int
	 * @param src
	 * 源
	 * @param defaultVal
	 * 默认值
	 * @return
	 */
	public static int toInt(String src,int defaultVal){
		if(src==null){
			return defaultVal;
		}
		try{
			return Integer.parseInt(src);
		}catch(Exception e){
			return defaultVal;
		}
	}
	
	/**
	 * String转为long
	 * @param src
	 * 源
	 * @param defaultVal
	 * 默认值
	 * @return
	 */
	public static long toLong(String src,long defaultVal){
		if(src==null){
			return defaultVal;
		}
		try{
			return Long.parseLong(src);
		}catch(Exception e){
			return defaultVal;
		}
	}
	
	/**
	 * 如果源字符串为null则返回""（空字符串），否则返回源字符串
	 * @param src
	 * @return
	 */
	public static String nullToEmpty(String src){
		return src==null?"":src;
	}
	
	/**
	 * 根据字符编码、限制字节数长度截取字符串，源字符串不超过长度则返回源字符串
	 * @param src
	 * 源字符串
	 * @param charset
	 * 字符编码
	 * @param length
	 * 限制字节数长度
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String subString(String src, String charset,int length) throws UnsupportedEncodingException{
		byte[] bytes = src.getBytes(charset);
		String ret = src;
		if (bytes.length > length) {
			ret = new String(bytes, 0, length, charset);
		}
		return ret;
	}
	
	/**
	 * null或空字符串转为"0"
	 * @param src
	 * @return
	 */
	public static String emptyToZero(String src){
		return StringUtils.isBlank(src)?"0":src;
	}
	
	public static boolean isNullOrBlank(String src){
		return src==null||src.isEmpty();
	}

}
