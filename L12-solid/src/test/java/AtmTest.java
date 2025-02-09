import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.l12.Atm;
import ru.otus.l12.AtmCell;
import ru.otus.l12.Banknote;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AtmTest {

    private final Atm atm = new Atm(Set.of(10, 5, 2, 1));

    @BeforeEach
    void beforeEach() {
        atm.reset();
    }

    @Test
    void addBanknote() {

        atm.add(new Banknote(10));
        atm.add(new Banknote(10));

        assertEquals(20, atm.getTotal());
    }

    @Test
    void addBanknote_incorrect() {
        assertThrows(
                IllegalArgumentException.class,
                () -> atm.add(new Banknote(11))
        );
    }

    @Test
    void withdraw() {
        atm.add(new Banknote(10));
        atm.add(new Banknote(10));
        atm.add(new Banknote(5));
        atm.add(new Banknote(2));

        List<Integer> takenNominals = atm.withdraw(17);

        assertEquals(List.of(10, 5, 2), takenNominals);
    }

    @Test
    void withdraw_incorrect() {
        atm.add(new Banknote(10));

        assertThrows(IllegalArgumentException.class, () -> atm.withdraw(20));
    }
}