package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import ru.otus.crm.model.Id;

/** "Разбирает" объект на составные части */
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private Class<T> cls;
    private List<Field> fields;

    public EntityClassMetaDataImpl(Class<T> cls) {
        this.cls = cls;
        this.fields = Arrays.asList(cls.getDeclaredFields());
    }

    @Override
    public String getName() {
        return cls.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {

        try {
            return cls.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        return fields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow();
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fields.stream()
                .filter(f -> !f.isAnnotationPresent(Id.class))
                .toList();
    }
}
