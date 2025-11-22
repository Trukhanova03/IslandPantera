package com.javarush.island.trukhanova.simulation;

import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.IWorld;
import lombok.Setter;

import java.util.concurrent.CountDownLatch;



public class LifeCycleTask implements Runnable {

    private final IEntity entity;
    private final IWorld world;
    @Setter
    private CountDownLatch latch;

    public LifeCycleTask(IEntity entity, IWorld world) {
        this.entity = entity;
        this.world = world;
    }

    @Override
    public void run() {
        try {
            if (entity.isAlive()) {
                entity.runLifeCycle(world);
            }
        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
    }
}