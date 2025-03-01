package com.ticketingSystem.server.controller;

import com.ticketingSystem.server.model.Configuration;
import com.ticketingSystem.server.service.ConfigurationService;
import com.ticketingSystem.server.service.SimulationService;
import com.ticketingSystem.server.system.SimulationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;
    private final ConfigurationService configService;

    @Autowired
    public SimulationController(SimulationService simulationService, ConfigurationService service) {
        this.simulationService = simulationService;
        this.configService = service;
    }

    // Endpoint to start the simulation
    // Endpoint to start the simulation by configuration ID
    @PostMapping("/start/{id}")
    public String startSimulation(@PathVariable Long id) {
        try {
            // Retrieve the configuration from the database by ID
            Configuration configuration = configService.getConfigurationById(id);

            if (configuration == null) {
                return "Configuration with ID " + id + " not found.";
            }

            if (simulationService.isSystemRunning()) {
                return "Simulation is already running.";
            }

            // Start the simulation using the retrieved configuration
            SimulationResult result = simulationService.startSimulation(configuration);
            if (result != null) {
                System.out.println(result);
            }
            return "Simulation started successfully with configuration ID: " + id;
        } catch (InterruptedException e) {
            return "Error starting simulation: " + e.getMessage();
        } catch (Exception e) {
            return "An error occurred while starting the simulation: " + e.getMessage();
        }
    }

    // Endpoint to stop the simulation
    @PostMapping("/stop")
    public String stopSimulation() {
        try {
            if (!simulationService.isSystemRunning()) {
                return "Simulation is already stopped.";
            }

            simulationService.stopSimulation();
            return "Simulation stopped successfully.";
        } catch (Exception e) {
            return "Error stopping simulation: " + e.getMessage();
        }
    }
}
