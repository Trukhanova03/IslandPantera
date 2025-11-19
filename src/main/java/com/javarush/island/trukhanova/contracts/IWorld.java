package com.javarush.island.trukhanova.contracts;

import com.javarush.island.trukhanova.map.Location;
import java.util.List;

public interface IWorld {
    Location getLocation(int r, int c);
    boolean isValid(int r, int c);
    int getHeight();
    int getWidth();

    List<IEntity> getAllLivingEntities();
    void initializePopulation(ISettings settings);
    void cleanDeadEntities();
}
