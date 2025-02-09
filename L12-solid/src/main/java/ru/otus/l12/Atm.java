package ru.otus.l12;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Atm {

    private static final Logger logger = LoggerFactory.getLogger(Atm.class);

    private Map<Integer, AtmCell> cells = new HashMap<>();
    private int total = 0;

    private final BanknoteHolder banknoteHolder = new BanknoteHolder();


    public void add(Banknote... banknotes) {

        for (Banknote b : banknotes) {
            AtmCell cell = cells.get(b.amount());
            if (cell == null) {
                throw new IllegalArgumentException("Недопустимый номинал - %d".formatted(b.amount()));
            }

            cell.amount++;
            total += b.amount();
        }
    }

    public int getTotal() {
        return cells.values().stream()
                .mapToInt(cell -> cell.amount * cell.nominal)
                .sum();
    }

    public List<Banknote> get(int amount) {
        if (amount > getTotal()) throw new IllegalArgumentException("Запрошено %d, всего %d".formatted(amount, total));

        int sum = 0;
        List<Banknote> takenBanknotes = new ArrayList<>();
//
//        for (Banknote b : banknotes10) {
//            // если нужно 15, то 2 банкноты по 10 = 20, т.е. много => переходим к меньшим номиналам
//            if ((sum + b.amount()) > amount) continue;
//
//            sum += b.amount();
//            takenBanknotes.add(b);
//
//            if (sum == amount) {
//                //remove banknotes and return taken
//                break;
//            }
//        }
//
//        banknoteHolder.remove(takenBanknotes);
//
//        return takenBanknotes;
    }
}
