package ru.otus.dataprocessor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private String filename;
    private ObjectMapper objectMapper = new ObjectMapper();

    public ResourcesFileLoader(String fileName) {
        this.filename = fileName;
    }

    @Override
    public List<Measurement> load() {

        // читает файл, парсит и возвращает результат
        try {

            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(filename).getFile());

            return Arrays.stream(objectMapper.readValue(file, Measurement[].class)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
