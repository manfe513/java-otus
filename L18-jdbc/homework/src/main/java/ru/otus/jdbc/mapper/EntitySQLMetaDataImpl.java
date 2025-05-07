package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;

/** Создает SQL - запросы */
public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private EntityClassMetaData<T> meta;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> meta) {
        this.meta = meta;
    }

    @Override
    public String getSelectAllSql() {

        return "select * from %s".formatted(meta.getName());
    }

    @Override
    public String getSelectByIdSql() {

        return "select * from %s where %s=?".formatted(
                meta.getName(),
                meta.getIdField().getName()
        );
    }

    @Override
    public String getInsertSql() {

        var fields = meta.getAllFields().stream()
                .map(Field::getName)
                .toList();

        String commaSeparatedFieldNames = String.join(",", fields);
        String placeholders = fields.stream().map(f -> "?").collect(Collectors.joining(","));

        return "insert into %s (%s) values(%s)".formatted(
                meta.getName(),
                commaSeparatedFieldNames,
                placeholders
        );
    }

    @Override
    public String getUpdateSql() {

        String setExpr = meta.getFieldsWithoutId().stream()
                .map(f -> f.getName() + "=?")
                .collect(Collectors.joining(","));

        return "update %s set %s where %s=?".formatted(
                meta.getName(),
                setExpr,
                meta.getIdField().getName()
        );
    }
}
