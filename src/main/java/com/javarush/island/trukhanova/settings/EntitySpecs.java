package com.javarush.island.trukhanova.settings;

public class EntitySpecs {
    public final double maxWeight;
    public final int maxSpeed;
    public final double maxSatiation;
    public final int maxPerLocation;
    public final String displaySymbol;
    public final int reproduceCount;
    public final int initialCount;

    public EntitySpecs(double weight,
                       int speed,
                       double satiation,
                       int maxLoc,
                       String symbol,
                       int reproduceCount,
                       int initialCount) {
        this.maxWeight = weight;
        this.maxSpeed = speed;
        this.maxSatiation = satiation;
        this.maxPerLocation = maxLoc;
        this.displaySymbol = symbol;
        this.reproduceCount = reproduceCount;
        this.initialCount = initialCount;
    }

    public int getInitialCount() {
        return initialCount;
    }
}