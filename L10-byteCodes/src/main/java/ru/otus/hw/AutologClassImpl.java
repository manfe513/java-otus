package ru.otus.hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Должны быть проксированы 2 из 3 методов, аннотированных @Autologger
public class AutologClassImpl implements AutologInterface {
    private static final Logger logger = LoggerFactory.getLogger(AutologClassImpl.class);

    @Override
    public String toString() {
        return "AutologClassImpl {}";
    }

    @Autologger
    @Override
    public void calc(int param) {
        logger.info(">> calling method calc, param: {}", param);
    }

    @Autologger
    @Override
    public void calc(int param, String param2) {
        logger.info(">> calling method calc, params: {}, {}", param, param2);
    }

    @Override
    public void calc(int param, String param2, int param3) {
        logger.info(">> calling method calc, params: {}, {}, {}", param, param2, param3);
    }
}
