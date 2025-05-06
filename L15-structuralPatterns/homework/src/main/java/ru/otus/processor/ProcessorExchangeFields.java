package ru.otus.processor;

import ru.otus.model.Message;

// todo: 2.
//  Сделать процессор, который поменяет местами значения field11 и field12
public class ProcessorExchangeFields implements Processor {

    @Override
    public Message process(Message message) {
        String field11 = message.getField11();

        var msg = message.toBuilder()
                .field11(message.getField12())
                .field12(field11)
                .build();

        return msg;
    }
}
