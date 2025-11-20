package com.javarush.island.trukhanova.entities.predators;

import com.javarush.island.trukhanova.entities.base.EatingHelper;
import com.javarush.island.trukhanova.entities.herbivores.*;
import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.contracts.IWorld;
import java.util.List;

public class Eagle extends EatingHelper {
    public Eagle(int row, int col, ISettings settings) {
        super(row, col, settings);
    }

    @Override
    public void eat(IWorld world) {
        tryToEat(world, List.of(Rabbit.class, Mouse.class, Duck.class));
    }
}

