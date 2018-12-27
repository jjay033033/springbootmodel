package top.lmoon.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import top.lmoon.annotation.TimeStat;

@Component
@Aspect
public class TimeStatAop {
	
	 private final static Logger LOGGER = LoggerFactory.getLogger(TimeStatAop.class);

	    //切面应用范围是在com.gzyct.modules.spark包下面所有函数
	    @Around("execution(* top.lmoon.test..*.*(..))")
	    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
	        String signatureName = joinPoint.getSignature().getName();
	        Class<? extends Object> invokeClass = joinPoint.getTarget().getClass();
	        if (isTagged(invokeClass, signatureName)) {
	        	long t1 = System.currentTimeMillis();
	            Object res = joinPoint.proceed();
	            long t2 = System.currentTimeMillis();
	            LOGGER.info(invokeClass.getName()+":"+signatureName + "方法执行耗时(ms):"+(t2-t1));
	            return res;
	        }
	        return joinPoint.proceed();

	    }

	    //扫描父类是否被打上标签,或者父类中的这个方法是否被打伤标签
	    private boolean isTagged(Class invokeClass, String signatureName) {
	        if (isTaggedInInterfaceOf(invokeClass, signatureName)) {
	            return true;
	        }
	        if (!invokeClass.equals(Object.class)) {
	            return isTaggedInClassOf(invokeClass, signatureName) ? true :
	                    isTagged(invokeClass.getSuperclass(), signatureName);
	        }
	        return false;
	    }

	    //扫描当前类的接口
	    private boolean isTaggedInInterfaceOf(Class invokeClass, String signatureName) {
	        Class[] interfaces = invokeClass.getInterfaces();
	        for (Class cas : interfaces) {
	            return isTaggedInClassOf(cas, signatureName) ? true :
	                    isTaggedInInterfaceOf(cas, signatureName);
	        }
	        return false;
	    }

	    //方法名为signatureName的方法tagged有两种情况:方法本身被taged或者方法所在的类被taged
	    private boolean isTaggedInClassOf(Class cas, String signatureName) {
//	        return Lists.newArrayList(cas.getDeclaredMethods()).stream()
	    	return Arrays.stream(cas.getDeclaredMethods())
	                .anyMatch(method ->
	                        isMethodWithName(method, signatureName) && isMethodTagged(method)
	                                || isMethodWithName(method, signatureName) && isClassTagged(cas));
	    }

	    private boolean isClassTagged(Class invokeClass) {
	        return invokeClass.getAnnotation(TimeStat.class) != null;
	    }

	    private boolean isMethodTagged(Method method) {
	        return method.getAnnotation(TimeStat.class) != null;
	    }

	    private boolean isMethodWithName(Method method, String name) {
	        return method.getName().equals(name);
	    }

}
