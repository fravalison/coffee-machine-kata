package com.coffeemachine.domain;

import com.coffeemachine.domain.port.BeverageQuantityChecker;
import com.coffeemachine.domain.port.EmailNotifier;
import com.coffeemachine.domain.port.ReportingPort;
import com.coffeemachine.infra.Order;

public class DrinkMaker {

    ReportingPort reportingPort;
    BeverageQuantityChecker beverageQuantityChecker;
    EmailNotifier emailNotifier;

    public DrinkMaker(ReportingPort reportingPort, BeverageQuantityChecker beverageQuantityChecker, EmailNotifier emailNotifier) {
        this.reportingPort = reportingPort;
        this.beverageQuantityChecker = beverageQuantityChecker;
        this.emailNotifier = emailNotifier;
    }

    public String sendCommand(Order order) {
        final DrinkType drink;
        try {
            drink = order.getDrink();
        }catch(IllegalArgumentException iae) {
            return "M:message-content (Drink maker forwards any message received onto the coffee machine interface for the customer to see)";
        }
        if(drink.getPrice() > order.getAmount()) {
            return "M:message-content (Drink maker forwards any message received onto the coffee machine interface for the customer to see) - missing " + (drink.getPrice() - order.getAmount());
        }
        if(beverageQuantityChecker.isEmpty(drink.getLabel())) {
            emailNotifier.notifyMissingDrink(drink.getLabel());
            return "M:message-content (There is a water shortage and a notification has been sent)";
        }
        reportingPort.registerCommandDone(drink, drink.getPrice());
        return drink.display(order.getNumberOfSugar(), order.isWithStick(), order.isExtraHot());
    }

    public String getReport() {
        return reportingPort.report();
    }
}
