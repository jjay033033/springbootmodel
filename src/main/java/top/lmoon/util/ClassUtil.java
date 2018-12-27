package top.lmoon.util;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassUtil {
	
	/**
	 * 扫描父类是否被打上标签,或者父类中的这个方法是否被打伤标签
	 * @param invokeClass
	 * @param signatureName
	 * @param annotationClass
	 * @return
	 */
    public static boolean isTagged(Class invokeClass, String signatureName,Class annotationClass) {
        if (isTaggedInInterfaceOf(invokeClass, signatureName,annotationClass)) {
            return true;
        }
        if (!invokeClass.equals(Object.class)) {
            return isTaggedInClassOf(invokeClass, signatureName,annotationClass) ? true :
                    isTagged(invokeClass.getSuperclass(), signatureName,annotationClass);
        }
        return false;
    }

    /**
     * 扫描当前类的接口
     * @param invokeClass
     * @param signatureName
     * @param annotationClass
     * @return
     */
    public static boolean isTaggedInInterfaceOf(Class invokeClass, String signatureName,Class annotationClass) {
        Class[] interfaces = invokeClass.getInterfaces();
        for (Class cas : interfaces) {
            return isTaggedInClassOf(cas, signatureName,annotationClass) ? true :
                    isTaggedInInterfaceOf(cas, signatureName,annotationClass);
        }
        return false;
    }

    /**
     * 方法名为signatureName的方法tagged有两种情况:方法本身被taged或者方法所在的类被taged
     * @param cas
     * @param signatureName
     * @param annotationClass
     * @return
     */
    public static boolean isTaggedInClassOf(Class cas, String signatureName,Class annotationClass) {
//        return Lists.newArrayList(cas.getDeclaredMethods()).stream()
    	return Arrays.stream(cas.getDeclaredMethods())
                .anyMatch(method ->
                        isMethodWithName(method, signatureName) && isMethodTagged(method,annotationClass)
                                || isMethodWithName(method, signatureName) && isClassTagged(cas,annotationClass));
    }

    private static boolean isClassTagged(Class invokeClass,Class annotationClass) {
        return invokeClass.getAnnotation(annotationClass) != null;
    }

    private static boolean isMethodTagged(Method method,Class annotationClass) {
        return method.getAnnotation(annotationClass) != null;
    }

    private static boolean isMethodWithName(Method method, String name) {
        return method.getName().equals(name);
    }

}
