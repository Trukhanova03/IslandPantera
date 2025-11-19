package com.javarush.island.trukhanova.entities.base;

import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.contracts.IWorld;

public class Plant extends AEntity {

    public Plant(int row, int col, ISettings settings) { super(row, col, settings); }

    @Override
    public void runLifeCycle(IWorld world) {
        if (isAlive && weight < specs.maxWeight) {
            weight += specs.maxWeight * 0.1;
            if (weight > specs.maxWeight) { weight = specs.maxWeight; }
        }
    }
}

