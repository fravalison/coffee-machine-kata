package com.coffeemachine.domain.port;

import com.coffeemachine.domain.DrinkType;

public interface ReportingPort {
    void registerCommandDone(DrinkType drink, double price);

    String report();
}
