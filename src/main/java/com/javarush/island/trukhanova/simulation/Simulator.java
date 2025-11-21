package com.javarush.island.trukhanova.simulation;

import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.contracts.IWorld;
import com.javarush.island.trukhanova.map.Island;
import com.javarush.island.trukhanova.utils.Utils;
import java.util.List;
import java.util.concurrent.*;

public class Simulator {

    private final ScheduledExecutorService mainScheduler = Executors.newSingleThreadScheduledExecutor();
    private final ExecutorService lifeCycleExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final IWorld world;
    private final ISettings settings;

    public Simulator(IWorld world, ISettings settings) {
        this.world = world;
        this.settings = settings;
    }

    public void startSimulation() {
        mainScheduler.scheduleAtFixedRate(this::runCycle, 0, settings.getTickDelayMs(), TimeUnit.MILLISECONDS);
    }

    private void runCycle() {

        if (world instanceof Island) {
            ((Island) world).incrementTick();
        }
        int currentTick = world instanceof Island ? ((Island) world).getTick() : 0;

        if (currentTick > settings.getMaxTicks()) {
            System.out.println("Симуляция завершена: достигнуто максимальное количество тактов.");
            stopSimulation();
            return;
        }

        List<IEntity> allEntities = world.getAllLivingEntities();
        long animalCount = allEntities.stream().filter(e -> e instanceof com.javarush.island.trukhanova.entities.base.Animal).count();

        if (animalCount == 0 && currentTick > 1) {
            System.out.println("Симуляция завершена: все животные умерли.");
            stopSimulation();
            return;
        }

        int entityCount = allEntities.size();
        CountDownLatch latch = new CountDownLatch(entityCount);

        for (IEntity entity : allEntities) {
            lifeCycleExecutor.submit(new LifeCycleTask(entity, world, latch));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        world.cleanDeadEntities();
        Utils.printState(world, currentTick);
    }

    public void stopSimulation() {
        mainScheduler.shutdown();
        lifeCycleExecutor.shutdown();
    }
}

