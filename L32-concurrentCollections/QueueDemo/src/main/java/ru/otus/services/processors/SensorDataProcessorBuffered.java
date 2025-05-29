package ru.otus.services.processors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {

    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;

    private final SensorDataBufferedWriter writer;

    private final BlockingQueue<SensorData> buffer;

    private List<SensorData> drainBucket = new ArrayList<>();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {

        this.bufferSize = bufferSize;
        this.writer = writer;
        this.buffer = new PriorityBlockingQueue<>(bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {

        buffer.add(data);

        if (buffer.size() >= bufferSize) {
            flush();
        }
    }

    public void flush() {

        try {

            drainBucket = new ArrayList<>(bufferSize);

            if (buffer.drainTo(drainBucket) > 0) {
                writer.writeBufferedData(drainBucket);
            }

        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {

        flush();
    }
}
