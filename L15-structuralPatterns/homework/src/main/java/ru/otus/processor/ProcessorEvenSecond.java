package ru.otus.processor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import ru.otus.model.Message;

// todo: 3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с
// гарантированным результатом)
//         Секунда должна определяьться во время выполнения.
//         Тест - важная часть задания
// Обязательно посмотрите пример к паттерну Мементо!
public class ProcessorEvenSecond implements Processor {

    private final SecondProvider secondProdiver;

    public ProcessorEvenSecond(SecondProvider secondProdiver) {
        this.secondProdiver = secondProdiver;
    }

    private ProcessorEvenSecond() {
        this.secondProdiver = new SecondProvider() {};
    }

    @Override
    public Message process(Message message) {

        if (secondProdiver.getCurrentSecond() % 2 == 0) {
            throw new EvenSecondException();

        } else return message;
    }

    public interface SecondProvider {
        default long getCurrentSecond() {
            return LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        }
    }

    public static class EvenSecondException extends IllegalStateException {

        EvenSecondException() {
            super("Выполнение в чётную секунду запрещено");
        }
    }
}
