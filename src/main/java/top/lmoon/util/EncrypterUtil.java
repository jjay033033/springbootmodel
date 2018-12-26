package top.lmoon.util;

/**
 * 数据加密/解密工具类（需调整）
 * @author gzy
 * @date 2018年7月19日
 *
 */
public class EncrypterUtil {
	
	private static final String key = "abc";
	
	/**
	 * 加密
	 * @param data
	 * @return
	 */
	public static String encrypt(String data){
//		return Encrypter.dataEncrypt(data, key);
		return data;
	}
	
	/**
	 * 解密
	 * @param data
	 * @return
	 */
	public static String decrypt(String data){
//		return Encrypter.dataDecrypt(data, key);
		return data;
	}

}
