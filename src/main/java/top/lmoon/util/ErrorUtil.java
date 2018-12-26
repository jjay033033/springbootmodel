package top.lmoon.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorUtil {

	private final static Logger logger = LoggerFactory.getLogger(ErrorUtil.class);

	public static void sendError(String msg) {
		logger.error(msg);
		handleError(msg, null);
	}

	public static void sendError(String msg, Exception e) {
		if (e == null) {
			sendError(msg);
			return;
		}
		logger.error(msg, e);
		handleError(msg, e);
	}

	private static void handleError(String msg, Exception e) {
		// TODO 错误处理
	}

}
