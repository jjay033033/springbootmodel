package top.lmoon.util;

import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;

/**
 * 唯一id生成类
 * @author gzy
 * @date 2018年8月30日
 *
 */
public class IdGenUtil{
	
	/**
	 * 20位唯一ID
	 * @return
	 */
	public static String getId(){
		long curr = System.currentTimeMillis();
		String currStr = String.valueOf(curr).substring(0, 13);
		int nextInt = RandomUtils.nextInt(0, 9999999);
		String ran = String.format("%07d", nextInt);
		return currStr+ran;
	}
	
	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(getId());
	}

}
