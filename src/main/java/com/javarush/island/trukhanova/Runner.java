package com.javarush.island.trukhanova;

import com.javarush.island.trukhanova.contracts.ISettings;
import com.javarush.island.trukhanova.contracts.IWorld;
import com.javarush.island.trukhanova.map.Island;
import com.javarush.island.trukhanova.simulation.Simulator;
import com.javarush.island.trukhanova.settings.SimulationSettings;

public class Runner {
    public static void main(String[] args) {
        ISettings settings = SimulationSettings.getInstance();

        System.out.println("Начало симуляции острова "
                + settings.getIslandHeight() + "x"
                + settings.getIslandWidth() + ".");

        IWorld island = new Island(settings);

        Simulator simulator = new Simulator(island, settings);
        simulator.start();
        Runtime.getRuntime().addShutdownHook(new Thread(simulator::stopSimulation));
    }
}
