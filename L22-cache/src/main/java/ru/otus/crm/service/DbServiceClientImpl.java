package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    private HwCache<Long, Client> cache;

    public DbServiceClientImpl(
            TransactionManager transactionManager,
            DataTemplate<Client> clientDataTemplate,
            HwCache<Long, Client> cache) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();

            Client savedClient;
            if (client.getId() == null) {
                savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
            } else {
                savedClient = clientDataTemplate.update(session, clientCloned);
                log.info("updated client: {}", savedClient);
            }

            cache.put(client.getId(), client);

            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(id);
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        }

        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);

            if (clientOptional.isPresent()) {
                var client = clientOptional.get();
                cache.put(client.getId(), client);
            }

            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);

            clientList.forEach(c -> cache.put(c.getId(), c));
            log.info("clientList:{}", clientList);

            return clientList;
        });
    }
}
