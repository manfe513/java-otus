package ru.vasilev.otus.hw06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SampleApp {
    private static final Logger logger = LoggerFactory.getLogger(SampleApp.class);

    public static void main(String[] args) {
        List<TestResult> results = new ArrayList<>();

        for(String arg : args) {
            Class<?> clazz;
            try {
                clazz = Class.forName(arg);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            TestResult result = new TestRunner().runTestsInClass(clazz);
            logger.info("Test results for class " + clazz.getName() + ":");
            logger.info("\t total: " + result.totalTests());
            logger.info("\t failed: " + result.failedTests());

            results.add(result);
        }

        int overallTests = results.stream().mapToInt(TestResult::totalTests).sum();
        int failedTests = results.stream().mapToInt(TestResult::failedTests).sum();

        logger.info("Overall results:");
        logger.info("\t total: " + overallTests);
        logger.info("\t failed: " + failedTests);
    }
}
