package top.lmoon.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import top.lmoon.annotation.ExceptionHandle;
import top.lmoon.util.ClassUtil;

@Component
@Aspect
public class ExceptionHandleAop {

	private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandleAop.class);

	// 切面应用范围是在com.gzyct.modules.spark包下面所有函数
	@Around("execution(* top.lmoon..*.*(..))")
	public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		String signatureName = joinPoint.getSignature().getName();
		Class<? extends Object> invokeClass = joinPoint.getTarget().getClass();
		if (ClassUtil.isTagged(invokeClass, signatureName, ExceptionHandle.class)) {
			LOGGER.info(invokeClass.getName() + ":" + signatureName + "方法执行.");
			Object res = null;
			try {
				res = joinPoint.proceed();
			} catch (Exception e) {
				LOGGER.error(invokeClass.getName() + ":" + signatureName + "方法执行出错:", e);
			}
			return res;
		}
		return joinPoint.proceed();

	}

}
