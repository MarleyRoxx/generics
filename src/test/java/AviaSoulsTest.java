import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AviaSoulsTest {

    private final Ticket ticket1 =
            new Ticket("Москва", "Новосибирск", 22_000, 10, 14);

    private final Ticket ticket2 =
            new Ticket("Москва", "Кемерово", 19_000, 12, 16);

    private final Ticket ticket3 =
            new Ticket("Москва", "Томск", 23_000, 11, 14);

    private final Ticket ticket4 =
            new Ticket("Москва", "Краснодар", 21_000, 9, 13);

    private final Ticket ticket5 =
            new Ticket("Москва", "Кемерово", 17_000, 8, 12);

    private final Ticket ticket6 =
            new Ticket("Москва", "Новосибирск", 13_000, 20, 24);

    private final AviaSouls manager = new AviaSouls();

    private final TicketTimeComparator comparator =
            new TicketTimeComparator();

    @BeforeEach
    public void setUp() {
        manager.add(ticket1);
        manager.add(ticket2);
        manager.add(ticket3);
        manager.add(ticket4);
        manager.add(ticket5);
        manager.add(ticket6);
    }

    @Test
    public void shouldCompareWhenFirstTicketIsCheaper() {
        int actual = ticket6.compareTo(ticket1);

        Assertions.assertTrue(actual < 0);
    }

    @Test
    public void shouldCompareWhenFirstTicketIsMoreExpensive() {
        int actual = ticket1.compareTo(ticket6);

        Assertions.assertTrue(actual > 0);
    }

    @Test
    public void shouldCompareWhenTicketPricesAreEqual() {
        Ticket samePriceTicket =
                new Ticket("Москва", "Омск", 22_000, 15, 19);

        int actual = ticket1.compareTo(samePriceTicket);

        Assertions.assertEquals(0, actual);
    }

    @Test
    public void shouldSearchAndSortNovosibirskTicketsByPrice() {
        Ticket[] expected = {ticket6, ticket1};

        Ticket[] actual = manager.search(
                "Москва", "Новосибирск"
        );

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldSearchAndSortKemerovoTicketsByPrice() {
        Ticket[] expected = {ticket5, ticket2};

        Ticket[] actual = manager.search(
                "Москва", "Кемерово"
        );

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindSingleTicket() {
        Ticket[] expected = {ticket3};

        Ticket[] actual = manager.search(
                "Москва", "Томск"
        );

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyArrayWhenDestinationNotFound() {
        Ticket[] expected = {};

        Ticket[] actual = manager.search(
                "Москва", "Сочи"
        );

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyArrayWhenDepartureNotFound() {
        Ticket[] expected = {};

        Ticket[] actual = manager.search(
                "Санкт-Петербург", "Новосибирск"
        );

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldCompareWhenFirstFlightIsShorter() {
        // ticket3 — 3 часа, ticket1 — 4 часа.
        int actual = comparator.compare(ticket3, ticket1);

        Assertions.assertTrue(actual < 0);
    }

    @Test
    public void shouldCompareWhenFirstFlightIsLonger() {
        // ticket1 — 4 часа, ticket3 — 3 часа.
        int actual = comparator.compare(ticket1, ticket3);

        Assertions.assertTrue(actual > 0);
    }

    @Test
    public void shouldCompareWhenFlightTimesAreEqual() {
        // Оба перелёта длятся 4 часа.
        int actual = comparator.compare(ticket2, ticket5);

        Assertions.assertEquals(0, actual);
    }

    @Test
    public void shouldSearchAndSortTicketsByFlightTime() {
        Ticket longFlight =
                new Ticket("Москва", "Новосибирск", 10_000, 8, 14);

        Ticket shortFlight =
                new Ticket("Москва", "Новосибирск", 25_000, 10, 12);

        manager.add(longFlight);   // 6 часов
        manager.add(shortFlight);  // 2 часа

        Ticket[] expected = {
                shortFlight,
                ticket1,
                ticket6,
                longFlight
        };

        Ticket[] actual = manager.searchAndSortBy(
                "Москва",
                "Новосибирск",
                comparator
        );

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldKeepOrderWhenFlightTimesAreEqual() {
        // Длительность обоих перелётов — 4 часа.
        Ticket[] expected = {ticket2, ticket5};

        Ticket[] actual = manager.searchAndSortBy(
                "Москва",
                "Кемерово",
                comparator
        );

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldFindSingleTicketWithComparator() {
        Ticket[] expected = {ticket3};

        Ticket[] actual = manager.searchAndSortBy(
                "Москва",
                "Томск",
                comparator
        );

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void shouldReturnEmptyArrayWithComparator() {
        Ticket[] expected = {};

        Ticket[] actual = manager.searchAndSortBy(
                "Москва",
                "Сочи",
                comparator
        );

        Assertions.assertArrayEquals(expected, actual);
    }
}