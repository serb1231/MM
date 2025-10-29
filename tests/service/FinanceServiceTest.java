package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class FinanceServiceTest {

    private DataStore store;
    private FinanceService financeService;
    private EventRequest event;

    @BeforeEach
    void setUp() {
        store = new DataStore();
        financeService = new FinanceService(store);

        event = new EventRequest("ClientA", "Event");
        event.setStatus("OK");
        event.setBudget(1000);
        store.events.add(event);
    }

    @Test
    void requestFinance_ValidEventAddFinanceRequest() {
        financeService.requestFinance(event.getId(), "Serb", "Need table big");

        assertEquals(1, event.getFinances().size());
        assertEquals("Serb", event.getFinances().getFirst().getRequester());
        assertEquals("Need table big", event.getFinances().getFirst().getDetails());
    }

    @Test
    void requestFinance_InvalidEvent() {
        financeService.requestFinance(100, "Serb", "Bad Event");

        assertTrue(event.getFinances().isEmpty(), "No Finance for invalid event");
    }

    @Test
    void processFinance_ApproveAddsBudgetAndUpdatesStatus() {
        financeService.requestFinance(event.getId(), "Serb", "Extra table");
        FinancialRequest fr = event.getFinances().getFirst();

//        simmulate the user input (CLI).
        String input = "1000\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        financeService.processFinance(event.getId(), fr.getId(), "Approve");

//        Adding 1000 to the existing budget => 2000
        assertTrue(fr.getStatus().contains("Approved"));
        assertEquals(2000, event.getBudget());
    }

    @Test
    void processFinance_RejectUpdatesStatusNoChangeBudget() {
        financeService.requestFinance(event.getId(), "Serb", "Extra table");
        FinancialRequest fr = event.getFinances().getFirst();

        financeService.processFinance(event.getId(), fr.getId(), "Reject");

        assertEquals("Rejected", fr.getStatus());
        assertEquals(1000, event.getBudget());
    }

    @Test
    void processFinance_InvalidFinanceId() {
        financeService.requestFinance(event.getId(), "Serb", "Extra table");

        financeService.processFinance(event.getId(), 100, "Approve");

        FinancialRequest fr = event.getFinances().getLast();
        assertEquals(fr.getStatus(), "Pending Finance");
    }
}
