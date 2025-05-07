package ru.otus.dataprocessor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileSerializer implements Serializer {

    private String filename;
    private ObjectMapper objectMapper = new ObjectMapper();

    public FileSerializer(String fileName) {
        this.filename = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try {
            objectMapper.writeValue(new File(filename), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
