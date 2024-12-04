package ru.vasilev.otus.hw06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class SampleTest {

    private static final Logger logger = LoggerFactory.getLogger(SampleTest.class);

    @MyBefore
    public void myBefore() {
        logger.info("myBefore");
    }

    @MyTest
    public void test1() {
        logger.info("test 1");
        if (2*2 != 4) throw new ArithmeticException("wrong result");
    }

    @MyTest
    public void test2() {
        logger.info("test 2");
        if( 2*2 != 5) throw new ArithmeticException("wrong result");
    }

    @MyAfter
    public void myAfter() {
        logger.info("myAfter");
    }
}
