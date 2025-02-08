package ru.otus.hw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

class AutologInvocationHandler implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(AutologInvocationHandler.class);
    private final AutologInterface myClass;
    private final Set<String> loggableMethodSignatures;

    private StringBuilder sb = new StringBuilder();

    AutologInvocationHandler(AutologInterface myClass) {
        this.myClass = myClass;
        loggableMethodSignatures = Arrays.stream(this.myClass.getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Autologger.class))
                .map(this::methodSignature)
                .collect(Collectors.toSet());
    }

    private String methodSignature(Method m) {
        sb.setLength(0);

        sb.append(m.getReturnType().getSimpleName());
        sb.append('-');
        sb.append(m.getName());
        sb.append(':');

        for (Class<?> arg : m.getParameterTypes()) {
            sb.append(arg.getName());
            sb.append(',');
        }

        return sb.toString();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logIfNeeded(method, args);

        return method.invoke(myClass, args);
    }

    private void logIfNeeded(Method method, Object[] args) {
        String methodSignature = methodSignature(method);
        if (!loggableMethodSignatures.contains(methodSignature)) return;

        logger.info("autologging method: {}", methodSignature);
        logger.info("params:");

        for (Object arg : args) {
            logger.info("- {} {}", arg.getClass().getSimpleName(), arg);
        }
    }

    @Override
    public String toString() {
        return "AutologInvocationHandler {" + "myClass=" + myClass + '}';
    }
}