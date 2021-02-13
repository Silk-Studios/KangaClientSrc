package io.github.si1kn.KangarooClient.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    public static void genericInvokeMethod(Object obj, String methodName, Object... params) {
        int paramCount = params.length;
        Method method;
        Object requiredObj = null;
        Class<?>[] classArray = new Class<?>[paramCount];
        for (int i = 0; i < paramCount; i++) {
            classArray[i] = params[i].getClass();
        }
        try {
            method = obj.getClass().getDeclaredMethod(methodName, classArray);
            method.setAccessible(true);
            try {
                requiredObj = method.invoke(obj, params);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static <E> Method findMethod(Class<? super E> clazz, E instance, String[] methodNames, Class<?>... methodTypes)
    {
        Exception failed = null;
        for (String methodName : methodNames)
        {
            try
            {
                Method m = clazz.getDeclaredMethod(methodName, methodTypes);
                m.setAccessible(true);
                return m;
            }
            catch (Exception e)
            {
                failed = e;
            }
        }
        throw new UnableToFindMethodException(methodNames, failed);
    }


    public static class UnableToFindMethodException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;

        public UnableToFindMethodException(String[] methodNames, Exception failed)
        {
            super(failed);
        }

    }
}
