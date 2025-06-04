package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.model.User;
import ru.otus.repository.AddressRepository;
import ru.otus.repository.ClientRepository;
import ru.otus.repository.PhoneRepository;
import ru.otus.repository.UserRepository;

@Component("actionDemo")
public class AppRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(AppRunner.class);

    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final UserRepository userRepository;

    public AppRunner(
            UserRepository userRepository,
            ClientRepository clientRepository,
            AddressRepository addressRepository,
            PhoneRepository phoneRepository) {
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {

        Client client = new Client("Иван");

        Address address = new Address("ул. Ленина");
        client.setAddress(address);

        client.addPhone(new Phone("+7-900-111-22-33"));
        client.addPhone(new Phone("+7-900-444-55-66"));

        clientRepository.save(client);
        addressRepository.save(address);

        userRepository.save(new User("Пользователь", "user", "user"));
        userRepository.save(new User("Administrator", "admin", "admin"));
    }
}
