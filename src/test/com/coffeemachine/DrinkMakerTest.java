package com.coffeemachine;

import com.coffeemachine.domain.DrinkMaker;
import com.coffeemachine.domain.port.BeverageQuantityChecker;
import com.coffeemachine.domain.port.EmailNotifier;
import com.coffeemachine.domain.port.ReportingPort;
import com.coffeemachine.infra.Order;
import com.coffeemachine.infra.adapter.ReportingRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DrinkMakerTest {

    private DrinkMaker drinkMaker;
    private BeverageQuantityChecker beverageQuantityChecker;
    private EmailNotifier emailNotifier;

    @Before
    public void setUp() {
        beverageQuantityChecker = mock(BeverageQuantityChecker.class);
        emailNotifier = mock(EmailNotifier.class);
        ReportingPort reportingPort = new ReportingRepository();
        drinkMaker = new DrinkMaker(reportingPort, beverageQuantityChecker, emailNotifier);
    }

    @Test
    public void send_command_for_1_tea_and_one_sugar() throws Exception {
        Order order = new Order("tea", 1, 1.0, false);

        String result = drinkMaker.sendCommand(order);

        assertEquals("T:1:0 (Drink maker will make one tea with one sugar and a stick)", result);
    }

    @Test
    public void send_command_for_1_chocolate_and_no_sugar() throws Exception {
        Order order = new Order("chocolate", 0, 1.0, false);

        String result = drinkMaker.sendCommand(order);

        assertEquals("H:: (Drink maker will make one chocolate with no sugar)", result);
    }

    @Test
    public void send_command_for_1_coffee_and_2_sugars() throws Exception {
        Order order = new Order("coffee", 2, 1.0, false);

        String result = drinkMaker.sendCommand(order);

        assertEquals("C:2:0 (Drink maker will make one coffee with two sugars and a stick)", result);
    }

    @Test
    public void send_command_for_unknown_drink() throws Exception {
        Order order = new Order("milk", 1, 1.0, false);

        String result = drinkMaker.sendCommand(order);

        assertEquals("M:message-content (Drink maker forwards any message received onto the coffee machine interface for the customer to see)", result);
    }
    
    @Test
    public void send_command_for_1_tea_and_one_sugar_with_not_enough_money() throws Exception {
        Order order = new Order("tea", 1, 0.20, false);

        String result = drinkMaker.sendCommand(order);

        assertEquals("M:message-content (Drink maker forwards any message received onto the coffee machine interface for the customer to see) - missing 0.2", result);
    }

    @Test
    public void send_command_for_1_orange_juice() throws Exception {
        Order order = new Order("orange juice", 0, 1.0, false);

        String result = drinkMaker.sendCommand(order);

        assertEquals("O:: (Drink maker will make one orange juice)", result);
    }

    @Test
    public void send_command_for_1_extra_hot_coffee_with_no_sugar() throws Exception {
        Order order = new Order("extra hot coffee", 0, 1.0, true);

        String result = drinkMaker.sendCommand(order);

        assertEquals("Ch:: (Drink maker will make an extra hot coffee with no sugar)", result);
    }


    @Test
    public void send_command_for_1_extra_hot_chocolate_with_1_sugar() throws Exception {
        Order order = new Order("extra hot chocolate", 1, 1.0, true);

        String result = drinkMaker.sendCommand(order);

        assertEquals("Hh:1:0 (Drink maker will make an extra hot chocolate with one sugar and a stick)", result);
    }

    @Test
    public void send_command_for_1_extra_hot_tea_with_2_sugars() throws Exception {
        Order order = new Order("extra hot tea", 2, 1.0, true);

        String result = drinkMaker.sendCommand(order);

        assertEquals("Th:2:0 (Drink maker will make an extra hot tea with two sugars and a stick)", result);
    }

    @Test
    public void send_command_and_get_report() throws Exception {
        Order teaOrder = new Order("extra hot tea", 2, 1.0, true);
        drinkMaker.sendCommand(teaOrder);
        Order chocolateOrder = new Order("extra hot chocolate", 1, 1.0, true);
        drinkMaker.sendCommand(chocolateOrder);

        String result = drinkMaker.getReport();

        assertEquals("1 tea, 1 chocolate, Total amount : 0.9", result);
    }

    @Test
    public void send_command_with_shortage_water() throws Exception {
        Order order = new Order("extra hot tea", 2, 1.0, true);
        given(beverageQuantityChecker.isEmpty("tea")).willReturn(true);

        String result = drinkMaker.sendCommand(order);

        verify(emailNotifier).notifyMissingDrink("tea");
        assertEquals("M:message-content (There is a water shortage and a notification has been sent)", result);
    }
}