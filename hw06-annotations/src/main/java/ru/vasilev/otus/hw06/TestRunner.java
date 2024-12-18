package ru.vasilev.otus.hw06;


import ru.vasilev.otus.hw06.annotation.MyAfter;
import ru.vasilev.otus.hw06.annotation.MyBefore;
import ru.vasilev.otus.hw06.annotation.MyTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TestRunner {

    @SuppressWarnings("ReassignedVariable")
    public TestResult runTestsInClass(Class<?> tClass) {

        Method[] methods = tClass.getDeclaredMethods();
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();

        Arrays.stream(methods).forEach(method -> {

            if (isBefore(method)) {
                beforeMethods.add(method);

            } else if (isTest(method)) {
                testMethods.add(method);

            } else if (isAfter(method)) {
                afterMethods.add(method);
            }
        });

        AtomicInteger failedTests = new AtomicInteger();
        AtomicReference<Object> obj = new AtomicReference<>();

        testMethods.forEach(m -> {
            boolean beforeMethodSucceed = true;

            obj.set(createTestClassInstance(tClass));

            for (Method mBefore : beforeMethods) {
                if (!runMethod(mBefore, obj.get())) {
                    beforeMethodSucceed = false;
                    break;
                }
            }

            if (beforeMethodSucceed) {
                if (!runMethod(m, obj.get())) {
                    failedTests.incrementAndGet();
                }
            }

            for (Method mAfter : afterMethods) {
                runMethod(mAfter, obj.get());
            }
        });

        return new TestResult(testMethods.size(), failedTests.get(), obj.toString());
    }

    private Object createTestClassInstance(Class<?> tClass) {
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("unable to instantiate class" + tClass.getName());
        }
    }

    private boolean runMethod(Method m, Object o) {
        try {
            m.invoke(o);

        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
        return true;
    }

    private boolean isBefore(Method m) {
        return m.getDeclaredAnnotation(MyBefore.class) != null;
    }

    private boolean isAfter(Method m) {
        return m.getDeclaredAnnotation(MyAfter.class) != null;
    }

    private boolean isTest(Method m) {
        return m.getDeclaredAnnotation(MyTest.class) != null;
    }
}
