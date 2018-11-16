package com.coffeemachine.domain.port;

public interface EmailNotifier {
    void notifyMissingDrink(String drink);
}
