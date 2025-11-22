package com.javarush.island.trukhanova.map;

import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.contracts.IWorld;
import com.javarush.island.trukhanova.entities.base.*;
import com.javarush.island.trukhanova.entities.predators.*;
import com.javarush.island.trukhanova.entities.herbivores.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Island implements IWorld {

    private final Location[][] grid;
    private final int width;
    private final int height;
    private final ISettings settings;
    private int tickCounter = 0;

    public Island(ISettings settings) {
        this.settings = settings;
        this.width = settings.getIslandWidth();
        this.height = settings.getIslandHeight();

        this.grid = new Location[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = new Location();
            }
        }
    }

    @Override public Location getLocation(int r, int c) { return (isValid(r, c)) ? grid[r][c] : null; }
    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return height; }
    @Override public boolean isValid(int r, int c) { return r >= 0 && r < height && c >= 0 && c < width; }
    public void incrementTick() { this.tickCounter++; }
    public int getTick() { return this.tickCounter; }


    @Override
    public void initializePopulation(ISettings settings) {

        List<Class<? extends AEntity>> entityTypes = List.of(
                Wolf.class, Boa.class, Fox.class, Bear.class, Eagle.class,
                Horse.class, Deer.class, Rabbit.class, Mouse.class, Goat.class,
                Sheep.class, Boar.class, Buffalo.class, Duck.class, Caterpillar.class,
                Plant.class
        );


        for (Class<? extends AEntity> type : entityTypes) {

            int maxPerLocation = settings.getSpecs(type).maxPerLocation;
            int initialCount = settings.getSpecs(type).getInitialCount();

            for (int k = 0; k < initialCount; k++) {

                int r = ThreadLocalRandom.current().nextInt(height);
                int c = ThreadLocalRandom.current().nextInt(width);
                Location location = grid[r][c];


                if (location.getEntityCount(type) < maxPerLocation) {
                    try {
                        AEntity newEntity = type
                                .getDeclaredConstructor(int.class, int.class, ISettings.class)
                                .newInstance(r, c, settings);

                        location.addEntity(newEntity);
                    } catch (Exception e) {
                        System.err.println("Ошибка инициализации сущности " + type.getSimpleName() + ": " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void cleanDeadEntities() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Location loc = grid[i][j];

                loc.getAllEntities().stream()
                        .filter(e -> !e.isAlive())
                        .forEach(loc::removeEntity);
            }
        }
    }

    @Override
    public List<IEntity> getAllLivingEntities() {
        List<IEntity> all = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                all.addAll(grid[i][j].getAllEntities());
            }
        }
        return all.stream().filter(IEntity::isAlive).collect(Collectors.toList());
    }
}