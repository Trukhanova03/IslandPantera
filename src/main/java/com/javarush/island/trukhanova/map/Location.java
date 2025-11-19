package com.javarush.island.trukhanova.map;

import com.javarush.island.trukhanova.contracts.IEntity;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class Location {

    private final ConcurrentHashMap<Class<?>, CopyOnWriteArraySet<IEntity>> entities;

    public Location() {
        this.entities = new ConcurrentHashMap<>();
    }

    public void addEntity(IEntity entity) {
        CopyOnWriteArraySet<IEntity> set = entities.computeIfAbsent(
                entity.getClass(),
                k -> new CopyOnWriteArraySet<>()
        );
        set.add(entity);
    }

    public void removeEntity(IEntity entity) {
        CopyOnWriteArraySet<IEntity> set = entities.get(entity.getClass());
        if (set != null) {
            set.remove(entity);
        }
    }

    public <T extends IEntity> Set<T> getEntities(Class<T> entityClass) {
        CopyOnWriteArraySet<IEntity> set = entities.get(entityClass);
        if (set == null) {
            return Collections.emptySet();
        }

        return (Set<T>) set.stream().collect(Collectors.toSet());
    }

    public List<IEntity> getAllEntities() {
        return entities.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public int getEntityCount(Class<?> entityClass) {
        return entities.getOrDefault(entityClass, new CopyOnWriteArraySet<>()).size();
    }
}
