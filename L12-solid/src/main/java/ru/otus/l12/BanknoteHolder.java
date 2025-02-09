package ru.otus.l12;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class BanknoteHolder {

    private static final Logger logger = LoggerFactory.getLogger(BanknoteHolder.class);

    /**
     * key - номинал
     * value - кол-во банкнот
     */
    private final Map<Integer, Integer> nominals;

    public BanknoteHolder(int... nominals) {
         this.nominals = new HashMap<>(nominals.length);

         for (int n : nominals) {
             this.nominals.put(n, 0);
         }
    }

    public void add(int amount) {
        getBanknoteList(banknote).add(banknote);
    }

    public void remove(List<Banknote> takenBanknotes) {
        for (Banknote b : takenBanknotes) {
           getBanknoteList(b).remove(b);
        }
    }

    private List<Banknote> getBanknoteList(Banknote b) {

        return switch (b.amount()) {
            case 10 -> banknotes10;
            case 5 -> banknotes5;
            case 2 -> banknotes2;
            case 1 -> banknotes1;
            default -> throw new IllegalArgumentException("Недопустимый номинал - %d".formatted(b.amount()));
        };
    }
}
