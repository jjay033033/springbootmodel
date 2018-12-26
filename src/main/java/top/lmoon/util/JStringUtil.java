/*
 * JStringUtil.java
 *
 * Created on 2005年3月14日, 上午9:45
 */

package top.lmoon.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.gzyct.yctterm.comm.protocol.reader.Reader79Response;
//import com.gzyct.yctterm.comm.protocol.reader.ReaderB9Response;

/**
 * 
 * @author dengzhi
 */
// Decompiled by Jad v1.5.7g. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi
// Source File Name: JStringUtil.java

public class JStringUtil {
	public static final boolean DEBUG = false;
	public static final String ENCODE_GB2312 = "gb2312";

	//private static final Logger LOG = Logger.getLogger(JStringUtil.class.getName());
	private static final Logger logger = LoggerFactory.getLogger(JStringUtil.class);

	public JStringUtil() {
	}

	public static String get8SizeBinaryString(int i) {
		String str = Integer.toBinaryString(i);
		if (i < 0 && str.length() > 8) {
			str = str.substring(str.length() - 8, str.length());
		} else {
			while (str.length() < 8) {
				str = "0" + str;
			}
		}

		return str;
	}

	public static String get14SizeString(String tmp) {
		while (tmp.length() < 14) {
			tmp = " " + tmp;
		}
		return tmp;
	}

	public static long getBalanceByte(byte[] arrBalance) {
		String strPrevBalance = JStringUtil.formatBytes(arrBalance);
		long intPrevBalance = Long.parseLong(strPrevBalance, 16); // 充值前票卡余额
		return intPrevBalance;
	}

	public static String getBalanceByteYuan(byte[] arrBalance) {
		return JStringUtil.getYuan((int) getBalanceByte(arrBalance));
	}

	public static String getDisplayBalanceByteYuan(byte[] arrBalanceIn,
			byte[] arrBalanceDown) {
		byte arrPrevBalance[] = new byte[4]; // 充值前票卡余额
		arrPrevBalance = arrBalanceIn;
		String strPrevBalance = JStringUtil.formatBytes(arrPrevBalance);
		long intPrevBalanceIn = Long.parseLong(strPrevBalance, 16); // 充值前票卡余额

		int intAmtDown = arrBalanceDown[0];

		// double d = point/100d;
		DecimalFormat df = new DecimalFormat("#0.00");
		double d = ((intPrevBalanceIn - intAmtDown * 100)) / 100D;
		return df.format(d);

	}

	public static boolean isDelimiter(char p_cCharacter) {
		return (p_cCharacter < 'A' || p_cCharacter > 'Z')
				&& (p_cCharacter < 'a' || p_cCharacter > 'z')
				&& (p_cCharacter < '0' || p_cCharacter > '9')
				&& p_cCharacter != '$';
	}

	public static List breakIntoWords(String p_strContent) {
		List listWords = new ArrayList();
		int intLength = p_strContent.length();
		StringBuffer sbBuffer = new StringBuffer();
		for (int intIndex = 0; intIndex < intLength; intIndex++) {
			char cCharacter = p_strContent.charAt(intIndex);
			char cNextCharacter;
			if (intIndex >= intLength - 1)
				cNextCharacter = ' ';
			else
				cNextCharacter = p_strContent.charAt(intIndex + 1);
			if (cCharacter == '<') {
				int intEndIndex = p_strContent.indexOf(">", intIndex);
				if (intEndIndex != -1) {
					String strTag = p_strContent.substring(intIndex,
							intEndIndex + 1);
					listWords.add(strTag);
					intIndex = intEndIndex;
				}
				continue;
			}
			if (cCharacter <= '\377') {
				if (isDelimiter(cCharacter)) {
					listWords.add(String.valueOf(cCharacter));
					continue;
				}
				if (cNextCharacter <= '\377') {
					if (isDelimiter(cNextCharacter)) {
						sbBuffer.append(cCharacter);
						listWords.add(sbBuffer.toString());
						sbBuffer = new StringBuffer();
					} else {
						sbBuffer.append(cCharacter);
					}
				} else {
					sbBuffer.append(cCharacter);
					listWords.add(sbBuffer.toString());
					sbBuffer = new StringBuffer();
				}
			} else {
				listWords.add(String.valueOf(cCharacter));
			}
		}

		return listWords;
	}

	public static List breakIntoLines(String p_strContent, int p_intWidth,
			String p_strEncoding) {
		List listResult = new ArrayList();
		List listWords = breakIntoWords(p_strContent);
		try {
			int intCurrentLength = 0;
			StringBuffer sbBuffer = new StringBuffer();
			for (int intIndex = 0; intIndex < listWords.size(); intIndex++) {
				String strWord = (String) listWords.get(intIndex);
				if (strWord.startsWith("<")) {
					sbBuffer.append(strWord);
					continue;
				}
				int intLength = strWord.getBytes(p_strEncoding).length;
				int intRemainingSpace = p_intWidth - intCurrentLength;
				if (intLength > intRemainingSpace) {
					if (intLength > p_intWidth) {
						String strPart1 = strWord.substring(0,
								intRemainingSpace - 1) + "-";
						String strPart2 = strWord
								.substring(intRemainingSpace - 1);
						sbBuffer.append(strPart1);
						strWord = strPart2;
						intLength = strWord.getBytes(p_strEncoding).length;
					}
					listResult.add(sbBuffer.toString());
					intCurrentLength = 0;
					sbBuffer = new StringBuffer();
				}
				intCurrentLength += intLength;
				sbBuffer.append(strWord);
			}

			String strRemaining = sbBuffer.toString();
			if (strRemaining.length() > 0)
				listResult.add(strRemaining);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return listResult;
	}

	public static List getAllTokens(String p_strSource, String p_strDelimiter) {
		List listResult = new ArrayList();
		if (p_strSource != null && p_strSource.length() > 0) {
			int intLastIndex = 0;
			for (int intCurrentIndex = p_strSource.indexOf(p_strDelimiter); intCurrentIndex != -1; intCurrentIndex = p_strSource
					.indexOf(p_strDelimiter, intLastIndex)) {
				listResult.add(p_strSource.substring(intLastIndex,
						intCurrentIndex));
				intLastIndex = intCurrentIndex + p_strDelimiter.length();
			}

			listResult.add(p_strSource.substring(intLastIndex));
		}
		return listResult;
	}

	public static String getStringForList(List p_listValues,
			String p_strDelimiter) {
		StringBuffer sbResult = new StringBuffer();
		for (int intIndex = 0; intIndex < p_listValues.size(); intIndex++) {
			sbResult.append(p_listValues.get(intIndex));
			if (intIndex != p_listValues.size() - 1)
				sbResult.append(p_strDelimiter);
		}

		return sbResult.toString();
	}

	public static String getTrimmedString(String p_strValue,
			int p_intMaximumLength) {
		if (p_strValue.length() > p_intMaximumLength)
			p_strValue = p_strValue.substring(0, p_intMaximumLength);
		return p_strValue;
	}

	public static String getFiller(int p_intLength, String p_strFiller) {
		StringBuffer sbResult = new StringBuffer();
		for (int intIndex = 0; intIndex < p_intLength; intIndex++)
			sbResult.append(p_strFiller);

		return sbResult.toString();
	}

	public static String getFiller(String p_strText, int p_intLength,
			String p_strFiller) {
		int intSpaceLen = 0;
		try {
			intSpaceLen = p_intLength - p_strText.getBytes("ISO8859_1").length;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return getFiller(intSpaceLen, p_strFiller);
	}

	public static String getFiller(byte p_arrBytes[], int p_intLength,
			String p_strFiller) {
		int intSpaceLen = p_intLength - p_arrBytes.length;
		return getFiller(intSpaceLen, p_strFiller);
	}

	public static String getFiller(int p_intNumber, int p_intLength,
			String p_strFiller) {
		int intSpaceLen = p_intLength - String.valueOf(p_intNumber).length();
		return getFiller(intSpaceLen, p_strFiller);
	}

	public static String getFiller(long p_lNumber, int p_intLength,
			String p_strFiller) {
		int intSpaceLen = p_intLength - Long.toString(p_lNumber).length();
		return getFiller(intSpaceLen, p_strFiller);
	}

	public static String substituteVariable(String p_strText,
			Map p_mapVariableMap) {
		String strResult = p_strText;
		int intIndex = 0;
		int intIndex1 = 0;
		String strVariableName = "";
		String strReplacement = "";
		do {
			if ((intIndex = strResult.indexOf("$(")) == -1)
				break;
			intIndex1 = strResult.indexOf(")", intIndex);
			strVariableName = strResult.substring(intIndex + 2, intIndex1);
			strReplacement = (String) p_mapVariableMap.get(strVariableName);
			if (strReplacement != null)
				strResult = strResult.replaceAll("\\$\\(" + strVariableName
						+ "\\)", strReplacement);
		} while (true);
		return strResult;
	}

	public static String getStringFromBytes(byte p_arrBytes[]) {
		return new String(p_arrBytes);
	}

	public static String getStringFromBytes(byte p_arrBytes[],
			String p_strEncoding) {
		String strResult = "";
		try {
			strResult = new String(p_arrBytes, p_strEncoding);
		} catch (Exception ex) {
		}
		return strResult;
	}

	public static String getStringFromBytes(byte p_arrBytes[], int p_intOffset,
			int p_intLength, String p_strEncoding) {
		String strResult = "";
		try {
			strResult = new String(p_arrBytes, p_intOffset, p_intLength,
					p_strEncoding);
		} catch (Exception ex) {
		}
		return strResult;
	}

	public static String getStringFromMap(Map p_mapValues) {
		StringBuffer sbResult = new StringBuffer();
		if (p_mapValues != null) {
			for (Iterator itKeys = p_mapValues.keySet().iterator(); itKeys
					.hasNext(); sbResult.append(";")) {
				Object objKey = itKeys.next();
				Object objValue = p_mapValues.get(objKey);
				sbResult.append(objKey);
				sbResult.append("=");
				sbResult.append(objValue);
			}

		}
		return sbResult.toString();
	}

	public static Map getMapFromString(String p_strValues) {
		Map mapResult = new HashMap();
		List listPairs = getAllTokens(p_strValues, ";");
		for (int intIndex = 0; intIndex < listPairs.size(); intIndex++) {
			String strPair = (String) listPairs.get(intIndex);
			int intSeparatorIndex = strPair.indexOf("=");
			if (intSeparatorIndex != -1)
				mapResult.put(strPair.substring(0, intSeparatorIndex),
						strPair.substring(intSeparatorIndex + 1));
		}

		return mapResult;
	}

	public static String formatBytes(byte p_arrBytes[]) {
		if (p_arrBytes != null) {
			return formatBytes(p_arrBytes, p_arrBytes.length, "");
		} else {
			return null;
		}
	}

	public static String formatBytes(byte p_Bytes) {
		StringBuffer sbResult = new StringBuffer();

		int intValue = p_Bytes;
		if (intValue < 0)
			intValue += 256;
		String strHexString = Integer.toHexString(intValue);
		if (strHexString.length() == 1) {
			sbResult.append("0");

		}
		sbResult.append(strHexString);
		return sbResult.toString();
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

	public static byte[] compress2To1InBytes(String p_strValue)
			throws Exception {
		int intLength = p_strValue.length();
		int intCounter = 0;
		int intResultLength = intLength / 2 + (intLength % 2 != 0 ? 1 : 0);
		byte arrResult[] = new byte[intResultLength];
		for (int intIndex = intLength; intIndex >= 1; intIndex -= 2) {
			int intValue = 0;
			if (intIndex == 1)
				intValue = Integer.parseInt(p_strValue.substring(intIndex - 1,
						intIndex));
			else
				intValue = Integer.parseInt(p_strValue.substring(intIndex - 2,
						intIndex));
			arrResult[intResultLength - intCounter - 1] = (byte) (intValue + 33);
			intCounter++;
		}

		return arrResult;
	}

	public static String compress2To1(String p_strValue) throws Exception {
		return compress2To1(p_strValue, "iso8859_1");
	}

	public static String compress2To1(String p_strValue, String p_strEncoding)
			throws Exception {
		return new String(compress2To1InBytes(p_strValue), p_strEncoding);
	}

	public static String getSQLSelectGroupStatement(String p_strTableName,
			String p_strFieldName, List p_listValues) {
		StringBuffer sbStatement = new StringBuffer();
		sbStatement.append("SELECT * FROM ");
		sbStatement.append(p_strTableName);
		sbStatement.append(" WHERE ");
		sbStatement.append(p_strFieldName);
		sbStatement.append(" IN (");
		for (int intIndex = 0; intIndex < p_listValues.size(); intIndex++) {
			sbStatement.append("'");
			sbStatement.append(p_listValues.get(intIndex));
			sbStatement.append("'");
			if (intIndex != p_listValues.size() - 1)
				sbStatement.append(", ");
		}

		sbStatement.append(")");
		return sbStatement.toString();
	}

	public static String getSQLInsertStatement(String p_strTableName,
			Map p_mapFields) {
		StringBuffer sbStatement = new StringBuffer();
		sbStatement.append("INSERT INTO ");
		sbStatement.append(p_strTableName);
		sbStatement.append(" (");
		Iterator itFields = p_mapFields.keySet().iterator();
		do {
			if (!itFields.hasNext())
				break;
			String strFieldName = (String) itFields.next();
			sbStatement.append(strFieldName);
			if (itFields.hasNext())
				sbStatement.append(", ");
		} while (true);
		sbStatement.append(") VALUES (");
		itFields = p_mapFields.keySet().iterator();
		do {
			if (!itFields.hasNext())
				break;
			String strFieldValue = (String) p_mapFields.get(itFields.next());
			sbStatement.append(strFieldValue);
			if (itFields.hasNext())
				sbStatement.append(",");
		} while (true);
		sbStatement.append(")");
		return sbStatement.toString();
	}

	public static String getSQLUpdateStatement(String p_strTableName,
			Map p_mapIds, Map p_mapFields) {
		StringBuffer sbStatement = new StringBuffer();
		sbStatement.append("UPDATE ");
		sbStatement.append(p_strTableName);
		sbStatement.append(" SET ");
		Iterator itFields = p_mapFields.keySet().iterator();
		do {
			if (!itFields.hasNext())
				break;
			String strFieldName = (String) itFields.next();
			String strFieldValue = (String) p_mapFields.get(strFieldName);
			sbStatement.append(strFieldName);
			sbStatement.append(" = ");
			sbStatement.append(strFieldValue);
			if (itFields.hasNext())
				sbStatement.append(", ");
		} while (true);
		itFields = p_mapIds.keySet().iterator();
		if (itFields.hasNext()) {
			sbStatement.append(" WHERE ");
			do {
				if (!itFields.hasNext())
					break;
				String strFieldName = (String) itFields.next();
				String strFieldValue = (String) p_mapIds.get(strFieldName);
				sbStatement.append(strFieldName);
				sbStatement.append(" = ");
				sbStatement.append(strFieldValue);
				if (itFields.hasNext())
					sbStatement.append(" AND ");
			} while (true);
		}
		return sbStatement.toString();
	}

	public static byte[] StrToBytes(String str) {
		if (str == null)
			return null;
		if (str.length() / 2 < 1)
			return null;
		byte data[] = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			int intValue = Integer
					.parseInt(str.substring(2 * i, 2 * i + 2), 16);
			if (intValue > 127)
				intValue -= 256;
			data[i] = (byte) intValue;
		}
		return data;
	}

	public static byte[] LongTo4Byte(long s) {
		byte o[] = new byte[4];
		o[0] = (byte) (s / 0x1000000);
		o[1] = (byte) (s % 0x1000000 / 0x10000);
		o[2] = (byte) (s % 0x10000 / 0x100);
		o[3] = (byte) (s % 0x100);
		return o;
	}

	public static long Byte4ToLong(byte s[]) {
		if (s == null)
			return -1;
		int slen = s.length;
		long r = 0;
		long t = 1;
		for (int i = 0; i < slen; i++) {
			int b = s[slen - 1 - i];
			if (b < 0)
				b += 256;
			r = r + (b * t);
			t = t * 256;
		}
		return r;
	}

	public static String HexStrToAmountStr(String s) {
		long Amount = Long.parseLong(s, 16);
		String d = String.valueOf(Amount / 100);
		if (Amount % 100 < 10) {
			d = d + ".0" + String.valueOf(Amount % 100);
		} else
			d = d + "." + String.valueOf(Amount % 100);
		return d;
	}

	public static String LongToAmountStr(long s) {
		String d = String.valueOf(s / 100);
		if (s % 100 < 10) {
			d = d + ".0" + String.valueOf(s % 100);
		} else
			d = d + "." + String.valueOf(s % 100);
		return d;
	}

	public static byte[] PreNewString(byte[] byteArray) {
		byte[] TmpBuff = new byte[byteArray.length];
		for (int i = 0; i < TmpBuff.length; i++) {
			if (0x00 != byteArray[i]) {
				TmpBuff[i] = byteArray[i];
			} else {
				TmpBuff[i] = (byte) 0x20;
			}
		}
		return TmpBuff;
	}

	public static int BytesToInt(byte s[]) {
		if (s == null) {
			return -1;
		}
		int slen = s.length;
		int r = 0;
		int t = 1;
		for (int i = 0; i < slen; i++) {
			int b = s[slen - 1 - i];
			if (b < 0) {
				b += 256;
			}
			r = r + (b * t);
			t = t * 256;
		}
		return r;
	}

	// weipd
	public static byte[] hexStringToByte(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	public static byte[] intToHEXAmtByte(int c) {
		byte[] tt = intToByte(c);
		byte[] tmp = new byte[4];
		tmp[0] = tt[3];
		tmp[1] = tt[2];
		tmp[2] = tt[1];
		tmp[3] = tt[0];
		return tmp;
	}

	// weipd
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static boolean isIp(String ipAddress) {
		String test = "([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pattern = Pattern.compile(test);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();
	}

	public static String getFixLengthHex(String hex, int length) {
		String temp = hex;
		if (temp != null) {
			while (temp.length() < length) {
				temp = "0" + temp;
			}

		}
		return temp;
	}

	/**
	 * 检查输出的端口是否是数字
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isPort(String input) {
		boolean boolReturn = true;
		try {
			if (input.startsWith("0")) {
				return false;
			}
			int i = Integer.valueOf(input);
			if (i > 655360) {
				return false;
			}
		} catch (Exception e) {
			boolReturn = false;
		}
		return boolReturn;
	}

	/**
	 * 检查输出的端口是否是数字
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isInteger(String input) {
		boolean boolReturn = true;
		try {
			if (input.startsWith("0")) {
				return false;
			}
			int i = Integer.valueOf(input);

		} catch (Exception e) {
			boolReturn = false;
		}
		return boolReturn;
	}

	/**
	 * convert object to string. if it is null, return ""
	 * 
	 * @param obj
	 *            Object
	 * @return String
	 */
	public static String nvl(Object obj) {
		if (obj == null) {
			return "";
		} else {
			if (obj instanceof String) {
				return (String) obj;
			} else {
				return obj.toString();
			}
		}
	}

	/**
	 * Check a string is empty or not
	 * 
	 * @param str
	 *            a string to check
	 * @return true if the string is empty
	 */
	public static boolean isBlank(String str) {
		return !isNotBlank(str);
	}

	/**
	 * Check a string is empty or not
	 * 
	 * @param str
	 *            a string to check
	 * @return true if the string is not empty
	 */
	public static boolean isNotBlank(String str) {
		if (!("".equals(nvl(str).trim()))) {
			return true;
		}
		return false;
	}

	public static String getTrimPreZone(String source) {
		if (source != null) {
			while (source.startsWith("0")) {
				source = source.substring(1);
			}
		}
		return source;
	}

	public static String getTrim(Object source) {
		if (source != null) {
			if (source instanceof String) {
				return (String) source.toString().trim();
			} else if (source instanceof Integer) {
				return source.toString();
			} else {
				return source.toString();
			}
		} else {
			return "";
		}
	}

	public static int getPoint(int t) {
		return getPoint(Double.valueOf(t));
	}

	public static int getPoint(String t) {
		return getPoint(Double.valueOf(t));
	}

	public static int getPoint(double t) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String ii = df.format(t);
		ii = ii.replaceAll("\\.", "");
		return Integer.valueOf(ii);
	}

	public static String getYuan(int point) {
		double d = point / 100d;
		DecimalFormat df = new DecimalFormat("#0.00");
		return df.format(d);
	}

	public static double addDouble(double t1, double t2) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String strT1 = df.format(t1);
		int intt1 = Integer.valueOf(strT1.replaceAll("\\.", ""));

		String strT2 = df.format(t2);
		int intt2 = Integer.valueOf(strT2.replaceAll("\\.", ""));

		double result = (double) (intt1 + intt2) / 100;
		return result;
	}

	public static double subDouble(double t1, double t2) {
		DecimalFormat df = new DecimalFormat("#0.00");
		String strT1 = df.format(t1);
		int intt1 = Integer.valueOf(strT1.replaceAll("\\.", ""));

		String strT2 = df.format(t2);
		int intt2 = Integer.valueOf(strT2.replaceAll("\\.", ""));

		double result = (double) (intt1 - intt2) / 100;
		return result;
	}

	/*
	 * public static String getRealPath() { File file = new File(new
	 * RealPath().getClass().getProtectionDomain()
	 * .getCodeSource().getLocation().getFile());
	 * 
	 * String path = file.getPath(); int i= path.indexOf("classes");
	 * 
	 * if(i>0){ path= path.substring(0,i); }else{ path =
	 * file.getParentFile().getPath(); LOG.info("path "+path); } return path ; }
	 */

	public static String getDirLibraryPath() {
		String libpath = System.getProperty("java.library.path");
		System.out.println("libpath " + libpath);
		if (libpath == null || libpath.length() == 0) {
			throw new RuntimeException("java.library.path is null");
		}

		String path = null;
		StringTokenizer st = new StringTokenizer(libpath,
				System.getProperty("path.separator"));
		if (st.hasMoreElements()) {
			path = st.nextToken();
			File dir = new File(path + "/interface");
			if (!dir.exists()) {
				dir.mkdir();
			}
		} else {
			throw new RuntimeException("can not split library path:" + libpath);
		}

		System.out.println("path " + path);
		return path;
	}

	public static boolean isAppControl() {
		boolean flag = false;

		return flag;
	}

	public static boolean isAppLetControl() {
		boolean flag = false;

		return flag;
	}

	/*
	 * public byte[] intToByte(int i) {
	 * 
	 * byte[] bt = new byte[4];
	 * 
	 * bt[0] = (byte) (0xff & i);
	 * 
	 * bt[1] = (byte) ((0xff00 & i) >> 8);
	 * 
	 * bt[2] = (byte) ((0xff0000 & i) >> 16);
	 * 
	 * bt[3] = (byte) ((0xff000000 & i) >> 24);
	 * 
	 * return bt;
	 * 
	 * }
	 * 
	 * public static int bytesToInt(byte[] bytes) {
	 * 
	 * int num = bytes[0] & 0xFF;
	 * 
	 * num |= ((bytes[1] << 8) & 0xFF00);
	 * 
	 * num |= ((bytes[2] << 16) & 0xFF0000);
	 * 
	 * num |= ((bytes[3] << 24) & 0xFF000000);
	 * 
	 * return num; }
	 */

	// long类型转成byte数组
	public static byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();
			// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	// byte数组转成long
	public static long byteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;

		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	/**
	 * 注释：int到字节数组的转换！
	 * 
	 * @param number
	 * @return
	 */
	public static byte[] intToByte(int number) {
		int temp = number;
		byte[] b = new byte[4];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();
			// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	/**
	 * 注释：字节数组到int的转换！
	 * 
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte[] b) {
		int s = 0;
		int s0 = b[0] & 0xff;// 最低位
		int s1 = b[1] & 0xff;
		int s2 = b[2] & 0xff;
		int s3 = b[3] & 0xff;
		s3 <<= 24;
		s2 <<= 16;
		s1 <<= 8;
		s = s0 | s1 | s2 | s3;
		return s;
	}

	/**
	 * 注释：short到字节数组的转换！
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] shortToByte(short number) {
		int temp = number;
		byte[] b = new byte[2];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Integer(temp & 0xff).byteValue();
			// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	/**
	 * 注释：字节数组到short的转换！
	 * 
	 * @param b
	 * @return
	 */
	public static short byteToShort(byte[] b) {
		short s = 0;
		short s0 = (short) (b[0] & 0xff);// 最低位
		short s1 = (short) (b[1] & 0xff);
		s1 <<= 8;
		s = (short) (s0 | s1);
		return s;
	}

	/*
	public static void main(String p_arrArgs[]) {
		int k = -30000;
		byte[] tt = JStringUtil.intToByte(k);
		String hex = JStringUtil.formatBytes(tt);
		System.out.println(hex);

		byte[] bb = JStringUtil.StrToBytes(hex);
		int kk = JStringUtil.byteToInt(bb);
		System.out.println(kk);

		String str = "00000064";
		byte[] b = JStringUtil.StrToBytes(str);
		byte[] size = new byte[8];
		System.arraycopy(b, 0, size, 4, 4);
		LOG.info(JStringUtil.byteToLong(size));
		b[0] = 100;
		b[1] = 0;
		b[2] = 0;
		b[3] = 0;
		LOG.info(JStringUtil.byteToInt(b));


		try {
			LOG.info(new String(JStringUtil
					.StrToBytes("B1A8CEC4D0A3D1E9C2EBB4EDCEF3"),
					JStringUtil.ENCODE_GB2312).trim());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	/*
	 * List listResult = breakIntoLines(p_arrArgs[0],
	 * Integer.parseInt(p_arrArgs[1]), p_arrArgs[2]); for(int intIndex = 0;
	 * intIndex < listResult.size(); intIndex++)
	 * LOG.info(listResult.get(intIndex));
	 */
	// JYCTUtil.removeRecoveryData();
	/*
	 * String str ="11223344";
	 * JYCTUtil.createRecoveryData3(JStringUtil.StrToBytes(str)
	 * ,JYCTUtil.OC3M1_RECOVERY_DAT) ;
	 * 
	 * LOG.info(getPoint(80.03)); LOG.info(getYuan(8003));
	 * 
	 * LOG.info(addDouble(80.03, 50.12));
	 * 
	 * LOG.info(subDouble(80.03, 50.12));
	 * 
	 * String tt = "158183"; byte[] tstr = tt.getBytes();
	 * System.out.println(JStringUtil.formatBytes(tstr));
	 */

	public static int getAESRUFLength(int bodyLength) {
		//
		int result = 0;
		int iLength = ((bodyLength) % 16);
		if (iLength < (16)) {
			result = 16 - iLength;
		}
		return result;
	}

	/**
	 * 只能接受0~9和小数点
	 * 
	 * @param original
	 *            String
	 * @param input
	 *            String
	 * @return String
	 */
	public static String handleNumber(String original, String input) {
		String result = original;
		if (original.indexOf(".") < 0) {// 无小数点
			result += input;
		} else {// 有小数点
			if (!".".equals(input)
					&& (original.length() - original.indexOf(".") < 3)) {// 小数点后只能有2位
				result += input;
			}
		}

		// 除掉最前面的0
		while (result.length() > 1 && "0".equals(result.substring(0, 1))) {
			result = result.substring(1);
		}

		// 如果第一个字符是小数点，则在前面加0
		if (".".equals(result.substring(0, 1))) {
			result = "0" + result;
		}

		// 不能大于1000
		double d = Double.valueOf(result).doubleValue();
		if (d > 1000d) {
			return original;
		}

		return result;
	}

	/*
	 * public static double getBalance(ReaderB9Response responseBefor) { double
	 * dafter; byte arrPrevBalance[] = new byte[4]; //充值前票卡余额 arrPrevBalance[0]
	 * = responseBefor.getArrBalance()[3]; arrPrevBalance[1] =
	 * responseBefor.getArrBalance()[2]; arrPrevBalance[2] =
	 * responseBefor.getArrBalance()[1]; arrPrevBalance[3] =
	 * responseBefor.getArrBalance()[0];
	 * 
	 * String strPrevBalance = JStringUtil.formatBytes(arrPrevBalance); long
	 * intPrevBalance = Long.parseLong(strPrevBalance, 16); //充值前票卡余额 dafter
	 * =intPrevBalance/100D; return dafter ; }
	 * 
	 * public static double getBalance(Reader79Response responseBefor) { double
	 * dafter; byte arrPrevBalance[] = new byte[4]; //充值前票卡余额 arrPrevBalance =
	 * responseBefor.getArrBalance(); String strPrevBalance =
	 * JStringUtil.formatBytes(arrPrevBalance); long intPrevBalance =
	 * Long.parseLong(strPrevBalance, 16); //充值前票卡余额 dafter
	 * =intPrevBalance/100D; return dafter ; }
	 */
}
