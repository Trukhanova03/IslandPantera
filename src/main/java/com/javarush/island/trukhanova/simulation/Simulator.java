package com.javarush.island.trukhanova.simulation;

import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.contracts.IWorld;
import com.javarush.island.trukhanova.entities.base.Animal;
import com.javarush.island.trukhanova.entities.base.Plant;
import com.javarush.island.trukhanova.map.Island;
import com.javarush.island.trukhanova.utils.Utils;

import java.util.List;
import java.util.concurrent.*;
import java.util.ArrayList;


public class Simulator {

    private final IWorld world;
    private final ISettings settings;
    private final ScheduledExecutorService mainScheduler;
    private final ExecutorService lifeCycleExecutor;

    private volatile boolean isStopped = false;

    public Simulator(IWorld world, ISettings settings) {
        this.world = world;
        this.settings = settings;

        this.mainScheduler = Executors.newSingleThreadScheduledExecutor();
        this.lifeCycleExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void start() {
        world.initializePopulation(settings);
        Utils.printState(world, 0);

        mainScheduler.scheduleAtFixedRate(this::runCycle, 0, 1, TimeUnit.SECONDS);
    }

    private void runCycle() {
        if (isStopped) return;

        if (!(world instanceof Island)) return;
        Island island = (Island) world;

        island.incrementTick();
        int currentTick = island.getTick();

        List<IEntity> allEntities = world.getAllLivingEntities();
        List<LifeCycleTask> tasks = new ArrayList<>();

        for (IEntity entity : allEntities) {
            if (entity instanceof Animal || entity instanceof Plant) {
                tasks.add(new LifeCycleTask(entity, world));
            }
        }

        CountDownLatch latch = new CountDownLatch(tasks.size());
        for (LifeCycleTask task : tasks) {
            task.setLatch(latch);
            lifeCycleExecutor.submit(task);
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        world.cleanDeadEntities();

        long animalCount = world.getAllLivingEntities().stream()
                .filter(e -> e instanceof Animal).count();

        if (currentTick >= settings.getMaxTicks() || (animalCount == 0 && currentTick > 1)) {
            System.out.println("Симуляция завершена: " +
                    (animalCount == 0 ? "все животные умерли." : "достигнуто максимальное количество тактов."));

            Utils.printState(world, currentTick);
            stopSimulation();
            return;
        }

        Utils.printState(world, currentTick);
    }

    public void stopSimulation() {
        if (isStopped) {
            return;
        }
        isStopped = true;

        mainScheduler.shutdown();
        lifeCycleExecutor.shutdown();

        System.out.println("Симулятор остановлен.");
    }
}