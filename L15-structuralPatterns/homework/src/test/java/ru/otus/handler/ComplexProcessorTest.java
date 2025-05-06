package ru.otus.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.ProcessorEvenSecond;
import ru.otus.processor.ProcessorExchangeFields;

class ComplexProcessorTest {

    @Test
    @DisplayName("Тестируем вызовы процессоров")
    void handleProcessorsTest() {
        // given
        var message = new Message.Builder(1L).field7("field7").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenReturn(message);

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, ex -> {});

        // when
        var result = complexProcessor.handle(message);

        // then
        verify(processor1).process(message);
        verify(processor2).process(message);
        assertThat(result).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем обработку исключения")
    void handleExceptionTest() {
        // given
        var message = new Message.Builder(1L).field8("field8").build();

        var processor1 = mock(Processor.class);
        when(processor1.process(message)).thenThrow(new RuntimeException("Test Exception"));

        var processor2 = mock(Processor.class);
        when(processor2.process(message)).thenReturn(message);

        var processors = List.of(processor1, processor2);

        var complexProcessor = new ComplexProcessor(processors, ex -> {
            throw new TestException(ex.getMessage());
        });

        // when
        assertThatExceptionOfType(TestException.class).isThrownBy(() -> complexProcessor.handle(message));

        // then
        verify(processor1, times(1)).process(message);
        verify(processor2, never()).process(message);
    }

    @Test
    @DisplayName("Тестируем уведомления")
    void notifyTest() {
        // given
        var message = new Message.Builder(1L).field9("field9").build();

        var listener = mock(Listener.class);

        var complexProcessor = new ComplexProcessor(new ArrayList<>(), ex -> {});

        complexProcessor.addListener(listener);

        // when
        complexProcessor.handle(message);
        complexProcessor.removeListener(listener);
        complexProcessor.handle(message);

        // then
        verify(listener, times(1)).onUpdated(message);
    }

    @Test
    @DisplayName("Проверка обработчика: поменять местами поля 11 и 12")
    void testFieldsExchange() {
        // given
        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .build();

        var processorExchangeFields = new ProcessorExchangeFields();
        var complexProcessor = new ComplexProcessor(List.of(processorExchangeFields), ex -> {});

        // when
        Message handledMsg = complexProcessor.handle(message);

        // then
        assertThat(handledMsg.getField11()).isEqualTo("field12");
        assertThat(handledMsg.getField12()).isEqualTo("field11");
    }

    @ParameterizedTest
    @CsvSource({
            "4, true",
            "3, false"
    })
    @DisplayName("Проверка обработчика: бросить исключение в чётную секунду")
    void throwsExceptionOnEvenSecond(int second, Boolean exceptionExpected) {
        // given
        var msg = new Message.Builder(1L).build();

        var secondProvider = new ProcessorEvenSecond.TimeProvider() {
            @Override
            public long getCurrentSecond() {

                return second;
            }
        };

        var processorEvenSecEx = new ProcessorEvenSecond(secondProvider);
        var complexProcessor = new ComplexProcessor(
                List.of(processorEvenSecEx),
                ex -> { throw new TestException(ex.getMessage()); }
        );

        // when
        if (exceptionExpected) {
            assertThatThrownBy(() -> complexProcessor.handle(msg))
                    .isInstanceOf(TestException.class)
                    .hasMessage("Выполнение в чётную секунду запрещено");
        } else {
            assertDoesNotThrow(() -> complexProcessor.handle(msg));
        }
    }

    private static class TestException extends RuntimeException {
        public TestException(String message) {
            super(message);
        }
    }
}
