package ru.vasilev.otus.hw06;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SampleApp {
    public static void main(String[] args) {
        Class<SampleTest> clazz = SampleTest.class;
        SampleTest sampleTest;

        try {
            sampleTest = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Method[] methods = clazz.getDeclaredMethods();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        Arrays.stream(methods).forEach(method -> {
            if (isBefore(method)) beforeMethods.add(method);
            else if (isTest(method)) testMethods.add(method);
            else if (isAfter(method)) afterMethods.add(method);
        });


        testMethods.forEach(m -> {
            beforeMethods.forEach(mBefore -> invokeMethod(mBefore, sampleTest));
            invokeMethod(m, sampleTest);
            afterMethods.forEach(mAfter -> invokeMethod(mAfter, sampleTest));
        });
    }

    private static void invokeMethod(Method m, Object o) {
        try {
            m.invoke(o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isBefore(Method m) {
        return m.getDeclaredAnnotation(MyBefore.class) != null;
    }

    private static boolean isAfter(Method m) {
        return m.getDeclaredAnnotation(MyAfter.class) != null;
    }

    private static boolean isTest(Method m) {
        return m.getDeclaredAnnotation(MyTest.class) != null;
    }
}
