package ru.otus.hw;

import ru.otus.aop.proxy.MyClassInterface;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class AutologApp {
    public static void main(String[] args) {
        AutologInterface myClass = createMyClass();
        myClass.calc(2);
        myClass.calc(2, "Some string paraaam");
        myClass.calc(2, "Some string paraaam", 456);
    }

    static AutologInterface createMyClass() {

        InvocationHandler handler = new AutologInvocationHandler(
                new AutologClassImpl()
        );

        return (AutologInterface) Proxy.newProxyInstance(
                AutologApp.class.getClassLoader(),
                new Class<?>[] {AutologInterface.class},
                handler
        );
    }
}
