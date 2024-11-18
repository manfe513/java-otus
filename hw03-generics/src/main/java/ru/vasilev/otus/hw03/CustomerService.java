package ru.vasilev.otus.hw03;

import java.util.*;

public class CustomerService {

    private final NavigableMap<Customer, String> map = new TreeMap<>(
            Comparator.comparingLong(Customer::getScores)
    );

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = map.firstEntry();

        return entry != null ? copyOfEntry(entry) : null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = map.higherEntry(customer);

        return entry != null ? copyOfEntry(entry) : null;
    }

    private Map.Entry<Customer, String> copyOfEntry(Map.Entry<Customer, String> entry) {
        return Map.entry(entry.getKey().copy(), entry.getValue());
    }

    public void add(Customer customer, String data) {
        map.put(customer.copy(), data);
    }
}
