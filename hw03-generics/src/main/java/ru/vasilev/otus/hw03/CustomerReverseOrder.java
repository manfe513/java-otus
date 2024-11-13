package ru.vasilev.otus.hw03;

import java.util.*;

public class CustomerReverseOrder {

    private final LinkedHashSet<Customer> sortedSet = new LinkedHashSet<>();

    public void add(Customer customer) {
        sortedSet.add(customer);
    }

    public Customer take() {
        return sortedSet.removeLast();
    }
}
