package top.lmoon.util;

import java.security.MessageDigest;

public class MD5Util 
{

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++)
			resultSb.append(byteToHexString(b[i]));

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname))
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes()));
			else
				resultString = byteArrayToHexString(md.digest(resultString
						.getBytes(charsetname)));
		} catch (Exception exception) {
		}
		return resultString;
	}
	
	/**
	 * 计算字符数组特定部分的MD5值.
	 * 
	 * @param info 字符数组
	 * @param start 字符数组开始计算的下标
	 * @param len 字符长度
	 * @return md5值
	 */
	public static String getMd5(byte[] info, int start, int len)
    {
    	try
    	{
    		byte []temp = new byte[len];
    		System.arraycopy(info,start, temp, 0, len);
    		MessageDigest md = MessageDigest.getInstance("MD5");
    		byte[] digest = md.digest(temp);
    		StringBuffer hexString = new StringBuffer();
    		for (int i = 0; i < digest.length; i++)
    		{
    			if ((0xff & digest[i]) < 0x10)
    			{
    				hexString.append("0" + Integer.toHexString((0xFF & digest[i])));
                }
    			else
    			{
    				hexString.append(Integer.toHexString(0xFF & digest[i]));
                }
            }
    		return hexString.toString();
    	}
    	catch(Exception ex)
    	{
    		return null;
    	}
    }

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

}
