package top.lmoon.util;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.MethodInvoker;

@Component
@Lazy(value = false)
public class SpringUtils implements ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(SpringUtils.class);
	private static ApplicationContext applicationContext;

	private SpringUtils() {
	}

	@Override
	public void setApplicationContext(ApplicationContext _applicationContext) throws BeansException {
		logger.debug("set ApplicationContext :  " + _applicationContext);
		applicationContext = _applicationContext;
	}

	public static Environment getEnvironment() {
		return applicationContext.getEnvironment();
	}

	public static String getProperty(String key) {
		return getEnvironment().getProperty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return getEnvironment().getProperty(key, defaultValue);
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	public static <T> T getBean(Class<T> requiredType) {
		return applicationContext.getBean(requiredType);
	}

	public static Object invokeBeanMethod(String beanName, String methodName, Object[] arguments) {
		try {
			MethodInvoker methodInvoker = new MethodInvoker();
			methodInvoker.setTargetObject(SpringUtils.getBean(beanName));
			methodInvoker.setTargetMethod(methodName);

			if (arguments != null && arguments.length > 0) {
				methodInvoker.setArguments(arguments);
			}

			methodInvoker.prepare();

			return methodInvoker.invoke();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}