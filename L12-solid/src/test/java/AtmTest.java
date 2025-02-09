import org.junit.jupiter.api.Test;
import ru.otus.l12.Atm;
import ru.otus.l12.Banknote;

import static org.junit.jupiter.api.Assertions.*;

class AtmTest {

    private final Atm atm = new Atm();

    @Test
    void addBanknote() {

        atm.add(new Banknote(10));
        atm.add(new Banknote(10));

        assertEquals(20, atm.getTotal());
    }

    @Test
    void getAmount_incorrect() {
        atm.add(new Banknote(10));

        assertThrows(IllegalArgumentException.class, () -> atm.get(20));
    }
}