package ru.otus.dao;

import java.util.List;
import java.util.Optional;
import ru.otus.from_orm_hw.crm.model.Client;

public interface ClientDao {

    Optional<Client> findById(long id);

    List<Client> getAll();

    Optional<Client> createClient(Client client);

    Optional<Client> addPhone(Long clientId, String number);

    Optional<Client> deletePhone(Long clientId, String number);

    Optional<Client> updateAddress(Long clientId, String address);
}
