package top.lmoon.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import top.lmoon.annotation.TimeStat;
import top.lmoon.util.ClassUtil;

@Component
@Aspect
public class TimeStatAop {
	
	 private final static Logger LOGGER = LoggerFactory.getLogger(TimeStatAop.class);

	    //切面应用范围是在com.gzyct.modules.spark包下面所有函数
	    @Around("execution(* top.lmoon.test..*.*(..))")
	    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
	        String signatureName = joinPoint.getSignature().getName();
	        Class<? extends Object> invokeClass = joinPoint.getTarget().getClass();
	        if (ClassUtil.isTagged(invokeClass, signatureName,TimeStat.class)) {
	        	long t1 = System.currentTimeMillis();
	            Object res = joinPoint.proceed();
	            long t2 = System.currentTimeMillis();
	            LOGGER.info(invokeClass.getName()+":"+signatureName + "方法执行耗时(ms):"+(t2-t1));
	            return res;
	        }
	        return joinPoint.proceed();

	    }

	    

}
