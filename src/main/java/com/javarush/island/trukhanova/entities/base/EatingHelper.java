package com.javarush.island.trukhanova.entities.base;

import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.contracts.IWorld;
import com.javarush.island.trukhanova.map.Location;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class EatingHelper extends Animal {

    public EatingHelper(int row, int col, ISettings settings) { super(row, col, settings); }

    protected void tryToEat(IWorld world, List<Class<? extends IEntity>> preyTypes) {
        Location currentLocation = world.getLocation(getRow(), getCol());
        if (currentLocation == null) return;

        for (Class<? extends IEntity> preyType : preyTypes) {

            IEntity target = currentLocation.getEntities(preyType).stream()
                    .filter(IEntity::isAlive).findAny().orElse(null);

            if (target != null) {

                int chance = settings.getKillChance(this.getClass(), target.getClass());

                if (ThreadLocalRandom.current().nextInt(100) < chance) {
                    double needed = specs.maxSatiation - satiationLevel;
                    double eaten = target.getEaten(needed);

                    satiationLevel += eaten;
                    if (satiationLevel > specs.maxSatiation) {
                        satiationLevel = specs.maxSatiation;
                    }
                    return;
                }
            }
        }
    }
}

