package com.coffeemachine.infra.adapter;

import com.coffeemachine.domain.DrinkType;
import com.coffeemachine.domain.port.ReportingPort;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class ReportingRepository implements ReportingPort {

    private List<CommandDone> commandsDone = new ArrayList<>();

    @Override
    public void registerCommandDone(DrinkType drink, double price) {
        commandsDone.add(new CommandDone(drink, price));
    }

    @Override
    public String report() {
        String numberOfDrinksReport = buildNumberOfDrinksReport();
        String totalAmountReport = buildTotalAmountReport();
        return numberOfDrinksReport + ", " + totalAmountReport;
    }

    private String buildNumberOfDrinksReport() {
        EnumMap<DrinkType, Long> numberOfDrinks = commandsDone.stream()
                .collect(groupingBy(CommandDone::getDrinkType, ()-> new EnumMap<>(DrinkType.class), counting()));
        return numberOfDrinks.entrySet()
                .stream()
                .map(drinkTypeLongEntry -> buildNumberOfDrinkReport(drinkTypeLongEntry.getKey(), drinkTypeLongEntry.getValue()))
                .collect(Collectors.joining(", "));
    }

    private String buildNumberOfDrinkReport(DrinkType drinkType, Long sum) {
        return sum + " " + drinkType.getLabel();
    }

    private String buildTotalAmountReport() {
        double totalAmount = commandsDone.stream()
                .mapToDouble(CommandDone::getPrice)
                .sum();
        return "Total amount : " + totalAmount;
    }

    public static class CommandDone {

        private DrinkType drinkType;
        private double price;

        public CommandDone(DrinkType drinkType, double price) {
            this.drinkType = drinkType;
            this.price = price;
        }

        public DrinkType getDrinkType() {
            return drinkType;
        }

        public double getPrice() {
            return price;
        }
    }

}
