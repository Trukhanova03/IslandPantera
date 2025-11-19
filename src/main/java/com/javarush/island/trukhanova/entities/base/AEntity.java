package com.javarush.island.trukhanova.entities.base;

import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.settings.EntitySpecs;

public abstract class AEntity implements IEntity {

    protected double weight;
    protected EntitySpecs specs;
    protected final ISettings settings;

    protected volatile boolean isAlive = true;

    protected int row;
    protected int col;

    public AEntity(int row, int col, ISettings settings) {
        this.row = row;
        this.col = col;
        this.settings = settings;
        this.specs = settings.getSpecs(this.getClass());
        this.weight = this.specs.maxWeight;
    }

    @Override
    public synchronized double getEaten(double requiredWeight) {
        if (!isAlive) return 0;

        double availableWeight = this.weight;

        if (availableWeight > requiredWeight) {
            this.weight -= requiredWeight;
            return requiredWeight;
        } else {
            this.weight = 0;
            this.setDead();
            return availableWeight;
        }
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void setDead() {
        this.isAlive = false;
    }

    @Override
    public String getDisplaySymbol() {
        return specs.displaySymbol;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }

    public void setLocation(int row, int col) {
        this.row = row; this.col = col;
    }
}
