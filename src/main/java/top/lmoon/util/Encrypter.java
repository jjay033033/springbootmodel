package top.lmoon.util;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encrypter {

	// 把3DES加密后的数据解密，acsii转换
	public static String dataDecrypt(String data, String key) {

		// 3des 解密
		String decryStr = JStringUtil.formatBytes(DESDecrypt(StrToBytes(key),
				Encrypter.StrToBytes(data)));
//		System.out.println("3DESDecryStr = " + decryStr);

		// ascii码转换
		String asciiStr = asciiToString(decryStr).trim();
//		System.out.println("asciiStr = " + asciiStr.trim());

		asciiStr = Base64.decode(asciiStr, "UTF-8");
		
		return asciiStr;
	}

	// acsii转换，3DES加密
	public static String dataEncrypt(String data, String key) {
		// System.out.println("key = " + key);
		
		data = Base64.encode(data, "UTF-8");
		
		// ascii码转换
		String asciiStr = stringToHexAsciiString(data);
//		System.out.println("asciiStr = " + asciiStr);

		// 3des 加密
		String encryStr = JStringUtil.formatBytes(DESEncrypt(StrToBytes(key),
				Encrypter.StrToBytes(asciiStr)));
//		System.out.println("encryStr = " + encryStr);

		return encryStr;
	}

	// string转成bytes
	public static byte[] StrToBytes(String str) {
		if (str == null) {
			return null;
		}
		if (str.length() / 2 < 1) {
			return null;
		}
		byte data[] = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			int intValue = Integer
					.parseInt(str.substring(2 * i, 2 * i + 2), 16);
			if (intValue > 127) {
				intValue -= 256;
			}
			data[i] = (byte) intValue;
		}
		return data;
	}

	// 将字符串转成ASCII十六进制的java方法
	public static String stringToHexAsciiString(String value) {
		StringBuffer sbu = new StringBuffer();
		char[] chars = value.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			sbu.append(Integer.toHexString(chars[i]));
		}
		return sbu.toString();
	}

	// 将ASCII转成字符串的java方法
	public static String asciiToString(String value) {
		StringBuffer sbu = new StringBuffer();
		// System.out.println("value.length()/2 = " + value.length()/2);
		byte[] chars = new byte[value.length() / 2];
		chars = JStringUtil.hexStringToByte(value);
		// System.out.println("chars.length = " + chars.length);
		for (int i = 0; i < chars.length; i++) {
			// sbu.append((char) Integer.parseInt(chars[i]));
			byte[] change = new byte[1];
			change[0] = chars[i];
			// System.out.println("change[0] = " + change[0]);
			sbu.append((char) chars[i]);
			// System.out.println("i = " + i + " " + sbu.toString());
		}
		return sbu.toString();
	}

	public static byte[] DESEncrypt(byte key[], byte Sour[]) {
		try {
			// 3DES ? ? ? ?8λ ? ?16λ 24λ ?
			byte[] key24 = new byte[24];
			System.arraycopy(key, 0, key24, 0, 16);
			System.arraycopy(key, 0, key24, 16, 8);
			SecretKey deskey = new SecretKeySpec(key24, "DESede");
			Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
			cipher.init(Cipher.ENCRYPT_MODE, deskey);
			int i = Sour.length % 8;
			byte[] tmp = null;
			if (i != 0) {
				tmp = new byte[Sour.length + 8 - i];
			} else {
				tmp = new byte[Sour.length];
			}
			System.arraycopy(Sour, 0, tmp, 0, Sour.length);
			byte[] TmpBuf = cipher.doFinal(tmp, 0, tmp.length);
			return TmpBuf;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @param key
	 *            byte[] 16λ ?
	 * @param Sour
	 *            byte[]
	 * @return byte[]
	 */
	public static byte[] DESDecrypt(byte key[], byte Sour[]) {
		try {
			byte[] key24 = new byte[24];
			System.arraycopy(key, 0, key24, 0, 16);
			System.arraycopy(key, 0, key24, 16, 8);

			SecretKey deskey = new SecretKeySpec(key24, "DESede");
			Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding"); //
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			return cipher.doFinal(Sour, 0, Sour.length);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String formatBytes(byte p_arrBytes[]) {
		if (p_arrBytes != null) {
			return formatBytes(p_arrBytes, p_arrBytes.length, "");
		} else {
			return null;
		}
	}

	public static String formatBytes(byte p_arrBytes[], int p_intLength,
			String p_strSeparator) {
		StringBuffer sbResult = new StringBuffer();
		for (int intIndex = 0; intIndex < p_intLength; intIndex++) {
			int intValue = p_arrBytes[intIndex];
			if (intValue < 0)
				intValue += 256;
			String strHexString = Integer.toHexString(intValue);
			if (strHexString.length() == 1) {
				sbResult.append("0");
				sbResult.append(p_strSeparator);
			}
			sbResult.append(strHexString);
		}

		return sbResult.toString();
	}

//	public static void main(String[] args ){
//		String str1 = "{\"platform\":1, \"userinfo\": \"{\"openid\":\"1234567890\"}\", \"type\":1}";
//		String key = "9F16FC5C138F102A3DD434ECFF8100DE";
//		dataEncrypt(str1,key);
		
		
//	}
	
}
