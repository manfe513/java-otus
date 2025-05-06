package ru.otus.listener.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

// todo: 4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
// Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
// Для него уже есть тест, убедитесь, что тест проходит
public class HistoryListener implements Listener, HistoryReader {

    private Map<Long, Message> messages = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        messages.put(msg.getId(), msg.toBuilder().build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messages.get(id));
    }
}
