package com.javarush.island.trukhanova.entities.base;

import com.javarush.island.trukhanova.contracts.IEater;
import com.javarush.island.trukhanova.contracts.IMovable;
import com.javarush.island.trukhanova.contracts.IReproducible;
import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.contracts.IWorld;
import com.javarush.island.trukhanova.map.Location;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends AEntity implements IMovable, IEater, IReproducible {

    protected double satiationLevel;

    public Animal(int row, int col, ISettings settings) {
        super(row, col, settings);
        this.satiationLevel = specs.maxSatiation / 2;
    }

    @Override
    public void runLifeCycle(IWorld world) {
        if (!isAlive) return;

        updateStatus();
        if (!isAlive) return;

        move(world);
        eat(world);
        reproduce(world);
    }

    private void updateStatus() {
        satiationLevel -= settings.getEnergyConsumptionPerTurn();
        if (satiationLevel <= 0) { this.setDead(); }
    }

    @Override
    public void move(IWorld world) {
        if (specs.maxSpeed == 0) return;

        int steps = ThreadLocalRandom.current().nextInt(specs.maxSpeed + 1);
        if (steps == 0) return;

        int deltaR = ThreadLocalRandom.current().nextInt(-1, 2);
        int deltaC = ThreadLocalRandom.current().nextInt(-1, 2);

        int newR = getRow() + deltaR * steps;
        int newC = getCol() + deltaC * steps;

        if (world.isValid(newR, newC)) {
            Location oldLocation = world.getLocation(getRow(), getCol());
            Location newLocation = world.getLocation(newR, newC);

            if (newLocation.getEntityCount(this.getClass()) < specs.maxPerLocation) {
                this.setLocation(newR, newC);
                oldLocation.removeEntity(this);
                newLocation.addEntity(this);
            }
        }
    }

    @Override
    public abstract void eat(IWorld world);

    @Override
    public void reproduce(IWorld world) {
        if (satiationLevel < specs.maxSatiation * 0.75) return;

        Location currentLocation = world.getLocation(getRow(), getCol());

        if (currentLocation.getEntityCount(this.getClass()) >= 2 &&
                currentLocation.getEntityCount(this.getClass()) + specs.reproduceCount < specs.maxPerLocation) {

            try {
                for (int i = 0; i < specs.reproduceCount; i++) {

                    Animal offspring = this.getClass().getDeclaredConstructor(int.class, int.class, ISettings.class).newInstance(getRow(), getCol(), settings);
                    currentLocation.addEntity(offspring);
                    satiationLevel *= 0.75;
                }
            } catch (Exception e) {
                System.err.println("Ошибка размножения: " + this.getClass().getSimpleName());
            }
        }
    }
}

