package ru.vasilev.otus.hw06;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vasilev.otus.hw06.annotation.MyAfter;
import ru.vasilev.otus.hw06.annotation.MyBefore;
import ru.vasilev.otus.hw06.annotation.MyTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestRunner {

    private static final Logger logger = LoggerFactory.getLogger(TestRunner.class);

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

        testMethods.forEach(m -> {
            boolean beforeMethodSucceed = true;

            Object obj = createTestClassInstance(tClass);
            logger.info("Class instance - {}", obj);

            for (Method mBefore : beforeMethods) {
                if (!runMethod(mBefore, obj)) {
                    beforeMethodSucceed = false;
                    break;
                }
            }

            if (beforeMethodSucceed) {
                if (!runMethod(m, obj)) {
                    failedTests.incrementAndGet();
                }
            }

            for (Method mAfter : afterMethods) {
                runMethod(mAfter, obj);
            }
        });

        return new TestResult(testMethods.size(), failedTests.get());
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
