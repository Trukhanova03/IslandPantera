package com.javarush.island.trukhanova.contracts;

import com.javarush.island.trukhanova.settings.EntitySpecs;

public interface ISettings {
    EntitySpecs getSpecs(Class<?> entityClass);
    int getKillChance(Class<?> predator, Class<?> prey);

    int getIslandWidth();
    int getIslandHeight();
    long getTickDelayMs();
    int getStartPopulationPercent();
    int getMaxTicks();
    double getEnergyConsumptionPerTurn();
}
