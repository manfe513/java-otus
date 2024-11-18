package ru.vasilev.otus.hw03;

import java.util.*;

public class CustomerReverseOrder {

    private final Deque<Customer> sortedSet = new LinkedList<>();

    public void add(Customer customer) {
        sortedSet.add(customer);
    }

    public Customer take() {
        return sortedSet.removeLast();
    }
}
