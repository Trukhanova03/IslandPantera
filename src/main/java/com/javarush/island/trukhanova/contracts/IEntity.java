package com.javarush.island.trukhanova.contracts;

import com.javarush.island.trukhanova.contracts.IWorld;

public interface IEntity {
    void runLifeCycle(IWorld world);
    boolean isAlive();
    void setDead();
    double getEaten(double requiredWeight);
    String getDisplaySymbol();

    int getRow();
    int getCol();
}
