package ru.otus.appcontainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();

    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {

        try {
            processConfig(initialConfigClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processConfig(Class<?> configClass) throws Exception {

        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(
                    "Конфигурация %s не поддерживается, требуется @AppComponentsContainerConfig"
                            .formatted(configClass.getName()));
        }

        List<Method> appComponentMethods = getAppComponentMethods(configClass);

        Object configObject = configClass.getDeclaredConstructor().newInstance();

        for (Method method : appComponentMethods) {
            String componentName = method.getAnnotation(AppComponent.class).name();
            Object component = createComponent(method, configObject);

            appComponents.add(component);

            if (!appComponentsByName.containsKey(componentName)) {
                appComponentsByName.put(componentName, component);
            } else {
                throw new IllegalStateException("Дубликат компонента %s".formatted(componentName));
            }
        }
    }

    private Object createComponent(final Method creationMethod, final Object configObject)
            throws InvocationTargetException, IllegalAccessException {

        Class<?>[] parameterTypes = creationMethod.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        Object param;

        for (int i = 0; i < parameterTypes.length; i++) {
            param = getAppComponent(parameterTypes[i]);

            if (param == null) {
                throw new RuntimeException("Dependency not found for type: " + parameterTypes[i].getName());
            } else {
                parameters[i] = param;
            }
        }

        return creationMethod.invoke(configObject, parameters);
    }

    private List<Method> getAppComponentMethods(final Class<?> configClass) {

        List<Method> methods = new ArrayList<>();

        for (Method method : configClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                methods.add(method);
            }
        }

        methods.sort(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()));

        return methods;
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {

        List<Object> foundComponents = new ArrayList<>();
        for (Object component : appComponents) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                foundComponents.add(component);
            }
        }

        if (foundComponents.isEmpty()) {
            throw new IllegalStateException("Компонент %s отсутствует".formatted(componentClass.getName()));
        }

        if (foundComponents.size() > 1) {
            throw new IllegalStateException("Компонент %s имеет более одной реализации".formatted(componentClass.getName()));
        }

        return (C) foundComponents.getFirst();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {

        var component = appComponentsByName.get(componentName);

        if (component == null) {
            throw new IllegalStateException("Компонент %s отсутствует".formatted(componentName));
        }

        return (C) appComponentsByName.get(componentName);
    }
}
