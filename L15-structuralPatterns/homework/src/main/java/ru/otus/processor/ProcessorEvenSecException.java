package ru.otus.processor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import ru.otus.model.Message;

// todo: 3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с
// гарантированным результатом)
//         Секунда должна определяьться во время выполнения.
//         Тест - важная часть задания
// Обязательно посмотрите пример к паттерну Мементо!
public class ProcessorEvenSecException implements Processor {

    private SecondProdiver secondProdiver;

    ProcessorEvenSecException(SecondProdiver secondProdiver) {
        this.secondProdiver = secondProdiver;
    }

    private ProcessorEvenSecException() {
        this.secondProdiver = new SecondProdiver() {};
    }

    @Override
    public Message process(Message message) {

        if (secondProdiver.getCurrentSecond() % 2 == 0) {
            throw new EvenSecondException();

        } else return message;
    }

    interface SecondProdiver {
        default long getCurrentSecond() {
            return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        }
    }

    static class EvenSecondException extends IllegalStateException {

        EvenSecondException() {
            super("Выполнение в чётную секунду запрещено");
        }
    }
}
