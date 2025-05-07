package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;

    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor,
            EntitySQLMetaData entitySQLMetaData,
            EntityClassMetaData<T> entityClassMetaData) {

        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), this::mapEntity);
    }

    private T mapEntity(ResultSet rs) {
        try {
            T obj = entityClassMetaData.getConstructor().newInstance();

            entityClassMetaData.getAllFields()
                    .forEach(f -> {
                        f.setAccessible(true);

                        try {
                            f.set(obj, rs.getObject(f.getName()));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<T> findAll(Connection connection) {

        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), List.of(), rs -> {

            List<T> items = new ArrayList<>();

            try {
                do {
                    mapEntity(rs);
                } while (rs.next());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return items;
        }).orElse(new ArrayList<>());
    }

    @Override
    public long insert(Connection connection, T client) {
        List<Object> values = fieldsToValues(entityClassMetaData.getAllFields(), client);

        return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), values);
    }

    private List<Object> fieldsToValues(List<Field> fields, T client) {
        return fields.stream()
                .map(f -> {
                    f.setAccessible(true);
                    try {
                        return f.get(client);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> values = fieldsToValues(entityClassMetaData.getFieldsWithoutId(), client);

        try {
            values.add(entityClassMetaData.getIdField().get(client));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), values);
    }
}
