package ru.vasilev.otus.hw03;

import java.util.*;

public class CustomerService {

    private final NavigableMap<Customer, String> map = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Optional<Map.Entry<Customer, String>> optional = map.entrySet().stream()
                .min(Comparator.comparingLong(x -> x.getKey().getScores()));

        return optional.map(customerStringEntry ->
                        Map.entry(customerStringEntry.getKey().copy(), customerStringEntry.getValue()))
                .orElse(null);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = map.higherEntry(customer);

        return entry != null ? Map.entry(entry.getKey().copy(), entry.getValue()) : null;
    }

    public void add(Customer customer, String data) {
        map.put(customer.copy(), data);
    }
}
