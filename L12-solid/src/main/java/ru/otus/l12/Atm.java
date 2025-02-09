package ru.otus.l12;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Atm {

    private static final Logger logger = LoggerFactory.getLogger(Atm.class);

    private final TreeSet<AtmCell> cells = new TreeSet<>(
            Comparator.comparingInt(AtmCell::getNominal).reversed()
    );

    @Getter
    private int total = 0;

    public Atm(Set<Integer> nominals) {

        nominals.forEach(n -> {
            var newCell = AtmCell.builder()
                    .nominal(n)
                    .amount(0)
                    .build();

            cells.add(newCell);
        });
    }

    public void add(Banknote... banknotes) {

        for (Banknote b : banknotes) {
            findCellForBanknote(b).amount++;
            total += b.nominal();
        }
    }

    private AtmCell findCellForBanknote(Banknote b) {
        return cells.stream()
                .filter(cell -> cell.nominal == b.nominal())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Недопустимый номинал: %d".formatted(b.nominal())
                        )
                );
    }

    public List<Integer> withdraw(int amount) {
        logger.info(">> Запрос на вывод суммы: {}", amount);
        if (amount > getTotal()) throw new IllegalArgumentException("Запрошено %d, всего %d".formatted(amount, total));

        int sum = 0;
        List<Integer> takenNominals = new ArrayList<>();

        for (AtmCell cell : cells) {
            // если нужно 15, то 2 банкноты по 10 = 20, т.е. много => переходим к меньшим номиналам
            if ((sum + cell.nominal) > amount) continue;

            sum += cell.nominal;
            cell.amount--;
            takenNominals.add(cell.nominal);

            if (sum == amount) {
                printTakenNominals(takenNominals);
                break;
            }
        }

        return takenNominals;
    }

    private void printTakenNominals(List<Integer> takenNominals) {
        logger.info(">> Деньги выданы купюрами:");

        for (int nominal : takenNominals) {
            logger.info("- {}", nominal);
        }
    }

    public void reset() {
        cells.forEach(c -> c.amount = 0);
    }
}
