package com.javarush.island.trukhanova.simulation;

import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.IWorld;
import java.util.concurrent.CountDownLatch;

public class LifeCycleTask implements Runnable {

    private final IEntity entity;
    private final IWorld world;
    private final CountDownLatch latch;

    public LifeCycleTask(IEntity entity, IWorld world, CountDownLatch latch) {
        this.entity = entity;
        this.world = world;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            if (entity.isAlive()) {
                entity.runLifeCycle(world);
            }
        } finally {
            latch.countDown();
        }
    }
}

