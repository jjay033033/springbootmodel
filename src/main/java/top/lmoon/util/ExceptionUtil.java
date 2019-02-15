/**
 * 
 */
package top.lmoon.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author LMoon
 * @date 2017年10月9日
 * 
 */
public class ExceptionUtil {
	
	public static String getExceptionMessage(Exception e){
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw, true));
		return sw.toString();
	}


}
