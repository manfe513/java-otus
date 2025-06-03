package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.from_orm_hw.core.repository.DataTemplate;
import ru.otus.from_orm_hw.core.sessionmanager.TransactionManager;
import ru.otus.from_orm_hw.crm.model.Address;
import ru.otus.from_orm_hw.crm.model.Client;
import ru.otus.from_orm_hw.crm.model.Phone;

public class DbClientDao implements ClientDao {

    private final Logger logger = LoggerFactory.getLogger(DbClientDao.class);

    private final TransactionManager tm;
    private final DataTemplate<Client> dataTemplate;

    public DbClientDao(TransactionManager tm, DataTemplate<Client> dataTemplate) {
        this.tm = tm;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Optional<Client> findById(long id) {
        return tm.doInReadOnlyTransaction(session -> {
            var clientOptional = dataTemplate.findById(session, id);
            logger.info("user: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> getAll() {
        return tm.doInReadOnlyTransaction(session -> {
            var clientsOptional = dataTemplate.findAll(session);
            logger.info("user: {}", clientsOptional);
            return clientsOptional;
        });
    }

    @Override
    public Optional<Client> createClient(final Client client) {
        return tm.doInTransaction(session -> {
            Client createdClient = dataTemplate.insert(session, client);
            return Optional.ofNullable(createdClient);
        });
    }

    @Override
    public Optional<Client> addPhone(final Long clientId, final String number) {

        return updateClient(clientId, client -> {
            Phone phone = new Phone();
            phone.setNumber(number);

            client.getPhones().add(phone);
            return client;
        });
    }

    @Override
    public Optional<Client> deletePhone(final Long clientId, final String number) {

        return updateClient(clientId, client -> {
            client.getPhones().removeIf(p -> p.getNumber().equals(number));

            return client;
        });
    }

    @Override
    public Optional<Client> updateAddress(final Long clientId, final String address) {

        return updateClient(clientId, client -> {
            Address newAddress = new Address();
            newAddress.setStreet(address);

            client.setAddress(newAddress);

            return client;
        });
    }

    private Optional<Client> updateClient(Long clientId, Function<Client, Client> operation) {
        return tm.doInTransaction(session -> {
            var clientOptional = dataTemplate.findById(session, clientId);

            if (clientOptional.isPresent()) {
                Client client = clientOptional.get();

                return Optional.ofNullable(session.merge(operation.apply(client)));
            }
            return Optional.empty();
        });
    }
}
