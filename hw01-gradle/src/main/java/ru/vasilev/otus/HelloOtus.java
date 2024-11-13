package ru.vasilev.otus;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HelloOtus {

    private HelloOtus() {}

    private static final Logger logger = LoggerFactory.getLogger(HelloOtus.class);

    static void callSomeMethodFromGuava() {
        List<Integer> nums = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            nums.add(i);
        }

        print("Nums before reversing", nums);
        print("After reversing with Guava", Lists.reverse(nums));
    }

    private static void print(String title, List<Integer> nums) {
        logger.info(title);
        logger.info("{}", nums);
    }
}
