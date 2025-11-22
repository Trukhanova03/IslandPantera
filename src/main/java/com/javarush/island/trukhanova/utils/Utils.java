package com.javarush.island.trukhanova.utils;

import com.javarush.island.trukhanova.contracts.IEntity;
import com.javarush.island.trukhanova.contracts.IWorld;
import com.javarush.island.trukhanova.map.Location;
import com.javarush.island.trukhanova.entities.base.Animal;
import com.javarush.island.trukhanova.entities.base.Plant; // Убедитесь, что импорт Plant корректен

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    private static final int DISPLAY_HEIGHT = 10;
    private static final int DISPLAY_WIDTH = 20;
    private static final String HORIZONTAL_LINE = "+------";
    private static final String CORNER = "+";
    private static final int CELL_CONTENT_LENGTH = 5;

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

        System.out.println("\n[MAP] Фрагмент Карты (" + DISPLAY_HEIGHT + "x" + DISPLAY_WIDTH + "):");

        printHorizontalBorder(DISPLAY_WIDTH);

        for (int r = 0; r < DISPLAY_HEIGHT; r++) {
            System.out.print("|");
            for (int c = 0; c < DISPLAY_WIDTH; c++) {

                String content = getCellContent(world, r, c);

                System.out.print(String.format(" %-5s|", content));
            }
            System.out.println();

            printHorizontalBorder(DISPLAY_WIDTH);
        }
        System.out.println("-----------------------------------------------------------------------");
    }

    private static void printHorizontalBorder(int width) {
        for (int c = 0; c < width; c++) {
            System.out.print(HORIZONTAL_LINE);
        }
        System.out.println(CORNER);
    }

    private static String getCellContent(IWorld world, int r, int c) {
        Location location = world.getLocation(r, c);
        if (location == null) return ".....";

        List<IEntity> entities = location.getAllEntities();
        if (entities.isEmpty()) return ".....";

        StringBuilder uniqueSymbols = new StringBuilder();

        Map<Class<?>, Boolean> addedClasses = new java.util.HashMap<>();

        entities.stream()
                .filter(e -> e instanceof Animal)
                .forEach(e -> {
                    if (!addedClasses.containsKey(e.getClass()) && uniqueSymbols.length() < CELL_CONTENT_LENGTH) {
                        uniqueSymbols.append(e.getDisplaySymbol());
                        addedClasses.put(e.getClass(), true);
                    }
                });

        if (uniqueSymbols.length() < CELL_CONTENT_LENGTH) {
            entities.stream()
                    .filter(e -> e instanceof Plant)
                    .forEach(e -> {
                        if (!addedClasses.containsKey(e.getClass()) && uniqueSymbols.length() < CELL_CONTENT_LENGTH) {
                            uniqueSymbols.append(e.getDisplaySymbol());
                            addedClasses.put(e.getClass(), true);
                        }
                    });
        }


        String result = uniqueSymbols.toString();

        int currentLength = result.length();
        if (currentLength < CELL_CONTENT_LENGTH) {
            result += ".".repeat(CELL_CONTENT_LENGTH - currentLength);
        }

        return result.substring(0, CELL_CONTENT_LENGTH);
    }
}