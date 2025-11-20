package com.javarush.island.trukhanova.settings;

import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.entities.base.*;
import com.javarush.island.trukhanova.entities.predators.*;
import com.javarush.island.trukhanova.entities.herbivores.*;
import java.util.*;

public class SimulationSettings implements ISettings {

    private static final int ISLAND_WIDTH = 100;
    private static final int ISLAND_HEIGHT = 20;
    private static final long TICK_DELAY_MS = 500;
    private static final int START_POPULATION_PERCENT = 5;
    private static final int MAX_TICKS = 1000;
    private static final double ENERGY_CONSUMPTION_PER_TURN = 5;

    private static SimulationSettings instance;
    private SimulationSettings() {
        loadAnimalCharacteristics();
        loadKillChances();
    }

    public static SimulationSettings getInstance() {
        if (instance == null) {
            instance = new SimulationSettings();
        }
        return instance;
    }

    private final Map<Class<?>, EntitySpecs> characteristics = new HashMap<>();
    private Map<Class<?>, Map<Class<?>, Integer>> killChances;

    private void loadAnimalCharacteristics() {

        // Хищники
        characteristics.put(Wolf.class, new EntitySpecs(50.0, 3, 8.0, 30, "\uD83D\uDC3A", 3));
        characteristics.put(Boa.class, new EntitySpecs(15.0, 1, 3.0, 30, "\uD83D\uDC0D", 4));
        characteristics.put(Fox.class, new EntitySpecs(8.0, 2, 2.0, 30, "\uD83E\uDDA8", 3));
        characteristics.put(Bear.class, new EntitySpecs(500.0, 2, 80.0, 5, "\uD83D\uDC3B", 2));
        characteristics.put(Eagle.class, new EntitySpecs(6.0, 3, 1.0, 20, "\uD83E\uDD85", 2));

        // Травоядные и Всеядные
        characteristics.put(Horse.class, new EntitySpecs(400.0, 4, 60.0, 20, "\uD83D\uDC0E", 4));
        characteristics.put(Deer.class, new EntitySpecs(300.0, 4, 50.0, 20, "\uD83E\uDD8C", 4));
        characteristics.put(Rabbit.class, new EntitySpecs(2.0, 2, 0.45, 150, "\uD83D\uDC07", 5));
        characteristics.put(Mouse.class, new EntitySpecs(0.05, 1, 0.01, 500, "\uD83D\uDC01", 10));
        characteristics.put(Goat.class, new EntitySpecs(60.0, 3, 10.0, 140, "\uD83D\uDC10", 3));
        characteristics.put(Sheep.class, new EntitySpecs(70.0, 3, 15.0, 140, "\uD83D\uDC11", 3));
        characteristics.put(Boar.class, new EntitySpecs(400.0, 2, 50.0, 50, "\uD83D\uDC17", 4));
        characteristics.put(Buffalo.class, new EntitySpecs(700.0, 3, 100.0, 10, "\uD83D\uDC03", 3));
        characteristics.put(Duck.class, new EntitySpecs(1.0, 4, 0.15, 200, "\uD83E\uDDA2", 6));
        characteristics.put(Caterpillar.class, new EntitySpecs(0.01, 0, 0.0, 1000, "\uD83D\uDC1B", 15));

        // Растение
        characteristics.put(Plant.class, new EntitySpecs(1.0, 0, 0.0, 200, "\uD83C\uDF31", 0));
    }

    private void loadKillChances() {
        killChances = new HashMap<>();

        // --- Вероятности Хищников ---
        Map<Class<?>, Integer> wolfChances = new HashMap<>();
        wolfChances.put(Horse.class, 10); wolfChances.put(Deer.class, 15); wolfChances.put(Rabbit.class, 60);
        wolfChances.put(Mouse.class, 80); wolfChances.put(Goat.class, 60); wolfChances.put(Sheep.class, 70);
        wolfChances.put(Boar.class, 15); wolfChances.put(Buffalo.class, 10); wolfChances.put(Duck.class, 40);
        killChances.put(Wolf.class, wolfChances);

        Map<Class<?>, Integer> boaChances = new HashMap<>();
        boaChances.put(Duck.class, 20); boaChances.put(Rabbit.class, 20); boaChances.put(Mouse.class, 40);
        killChances.put(Boa.class, boaChances);

        Map<Class<?>, Integer> foxChances = new HashMap<>();
        foxChances.put(Rabbit.class, 70); foxChances.put(Mouse.class, 90); foxChances.put(Duck.class, 60);
        foxChances.put(Caterpillar.class, 40);
        killChances.put(Fox.class, foxChances);

        Map<Class<?>, Integer> bearChances = new HashMap<>();
        bearChances.put(Horse.class, 40); bearChances.put(Deer.class, 80); bearChances.put(Rabbit.class, 80);
        bearChances.put(Mouse.class, 90); bearChances.put(Goat.class, 70); bearChances.put(Sheep.class, 70);
        bearChances.put(Boar.class, 50); bearChances.put(Buffalo.class, 20); bearChances.put(Duck.class, 10);
        killChances.put(Bear.class, bearChances);

        Map<Class<?>, Integer> eagleChances = new HashMap<>();
        eagleChances.put(Rabbit.class, 90); eagleChances.put(Mouse.class, 90); eagleChances.put(Duck.class, 80);
        killChances.put(Eagle.class, eagleChances);

        // --- Вероятности Травоядных ---
        List<Class<?>> strictHerbivores = List.of(Horse.class, Deer.class, Rabbit.class, Mouse.class, Goat.class, Sheep.class, Buffalo.class, Caterpillar.class);
        for (Class<?> herbivore : strictHerbivores) {
            killChances.computeIfAbsent(herbivore, k -> new HashMap<>()).put(Plant.class, 100);
        }

        killChances.computeIfAbsent(Duck.class, k -> new HashMap<>()).put(Caterpillar.class, 90);
        killChances.computeIfAbsent(Duck.class, k -> new HashMap<>()).put(Plant.class, 100);

        killChances.computeIfAbsent(Boar.class, k -> new HashMap<>()).put(Mouse.class, 50);
        killChances.computeIfAbsent(Boar.class, k -> new HashMap<>()).put(Plant.class, 100);
    }


    @Override
    public EntitySpecs getSpecs(Class<?> entityClass) {
        return characteristics.getOrDefault(entityClass, null);
    }

    @Override
    public int getKillChance(Class<?> predator, Class<?> prey) {
        return killChances.getOrDefault(predator, Collections.emptyMap())
                .getOrDefault(prey, 0);
    }

    @Override
    public int getIslandWidth() {
        return ISLAND_WIDTH;
    }

    @Override
    public int getIslandHeight() {
        return ISLAND_HEIGHT;
    }

    @Override
    public long getTickDelayMs() {
        return TICK_DELAY_MS;
    }

    @Override
    public int getStartPopulationPercent() {
        return START_POPULATION_PERCENT;
    }

    @Override
    public int getMaxTicks() {
        return MAX_TICKS;
    }

    @Override
    public double getEnergyConsumptionPerTurn() {
        return ENERGY_CONSUMPTION_PER_TURN;
    }
}

