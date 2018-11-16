package com.coffeemachine.infra;

import com.coffeemachine.domain.DrinkType;

public class Order {
    private String drink;
    private int numberOfSugar;
    private boolean withStick;
    private double amount;
    private boolean extraHot;

    public Order(String drink, int numberOfSugar, double amount, boolean extraHot) {
        this.drink = drink;
        this.numberOfSugar = numberOfSugar;
        this.withStick = numberOfSugar > 0;
        this.amount = amount;
        this.extraHot = extraHot;
    }

    public DrinkType getDrink() {
        if(extraHot) {
            String softDrink = this.drink.replace("extra hot ", "");
            return DrinkType.findByLabel(softDrink);
        }
        return DrinkType.findByLabel(drink);
    }

    public int getNumberOfSugar() {
       return numberOfSugar;
    }

    public boolean isWithStick() {
        return withStick;
    }

    public double getAmount() {
        return amount;
    }

    public boolean isExtraHot() {
        return extraHot;
    }

}
