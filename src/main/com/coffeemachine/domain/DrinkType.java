package com.coffeemachine.domain;

import java.util.Arrays;

public enum DrinkType {
    TEA("tea", "T", 0.4),
    COFFEE("coffee", "C", 0.6),
    CHOCOLATE("chocolate", "H", 0.5),
    ORANGE_JUICE("orange juice", "O", 0.6) {
        @Override
        public String display(int numberOfSugar, boolean isWithStick, boolean isExtraHot) {
            StringBuilder command = new StringBuilder()
                    .append(this.getShortLabel())
                    .append(":")
                    .append(":")
                    .append(" (Drink maker will make ")
                    .append("one ")
                    .append(this.getLabel())
                    .append(")");
            return command.toString();
        }
    };

    private String label;
    private String shortLabel;
    private double price;

    DrinkType(String label, String shortLabel, double price) {
        this.label = label;
        this.shortLabel = shortLabel;
        this.price = price;
    }

    public String getLabel() {
        return label;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public double getPrice() {
        return price;
    }

    public static DrinkType findByLabel(String label) {
        return Arrays.stream(DrinkType.values())
                .filter(drinkType -> drinkType.label.equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public String display(int numberOfSugar, boolean isWithStick, boolean isExtraHot) {
        StringBuilder command = new StringBuilder()
                .append(this.shortLabel)
                .append(isExtraHot ? "h":"")
                .append(":")
                .append(numberOfSugar > 0 ? numberOfSugar : "")
                .append(":")
                .append(isWithStick ? 0 : "")
                .append(" (Drink maker will make ")
                .append(isExtraHot ? "an extra hot " : "one ")
                .append(this.label)
                .append(" with ")
                .append(numberOfSugar > 0 ? displayNumberOfSugar(numberOfSugar) : "no")
                .append(numberOfSugar > 1 ? " sugars" : " sugar")
                .append(isWithStick ? " and a stick" : "")
                .append(")");
        return command.toString();
    }

    public static String displayNumberOfSugar(int numberOfSugar) {
        switch (numberOfSugar) {
            case 1 : return "one";
            case 2 : return "two";
            default: throw new IllegalArgumentException("Number of sugar is incorrect");
        }
    }
}
