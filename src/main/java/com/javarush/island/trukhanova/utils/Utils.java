package com.javarush.island.trukhanova.utils;

import com.javarush.island.trukhanova.entities.base.Animal;
import com.javarush.island.trukhanova.entities.base.Plant;
import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.IWorld;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static void printState(IWorld world, int currentTick) {
        final String CLEAR_SCREEN = "\033[H\033[2J";

        System.out.print(CLEAR_SCREEN);
        System.out.flush();

        System.out.println("=======================================================================");
        System.out.println("                   СИМУЛЯЦИЯ ОСТРОВА (Tick: " + currentTick + ")");
        System.out.println("=======================================================================");

        List<IEntity> allEntities = world.getAllLivingEntities();

        Map<String, Long> populationStats = allEntities.stream()
                .collect(Collectors.groupingBy(e -> e.getClass().getSimpleName(), Collectors.counting()));

        System.out.print("[STAT] Популяция: ");
        if (populationStats.isEmpty()) {
            System.out.println("Остров пуст.");
        } else {
            populationStats.forEach((type, count) ->
                    System.out.print(type + ": " + count + " | ")
            );
            System.out.println("ВСЕГО: " + allEntities.size());
        }

        int displayHeight = Math.min(world.getHeight(), 20);
        int displayWidth = Math.min(world.getWidth(), 40);

        System.out.println("\n[MAP] Фрагмент Карты (" + displayHeight + "x" + displayWidth + "):");

        for (int r = 0; r < displayHeight; r++) {
            for (int c = 0; c < displayWidth; c++) {
                String symbol = getCellSymbol(world, r, c);
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------");
    }

    private static String getCellSymbol(IWorld world, int r, int c) {
        List<IEntity> entities = world.getLocation(r, c).getAllEntities();
        if (entities.isEmpty()) return ".";

        return entities.stream()
                .filter(e -> e instanceof Animal)
                .findAny()
                .map(IEntity::getDisplaySymbol)
                .orElseGet(() -> entities.stream()
                        .filter(e -> e instanceof Plant)
                        .findAny()
                        .map(IEntity::getDisplaySymbol)
                        .orElse(".")
                );
    }
}