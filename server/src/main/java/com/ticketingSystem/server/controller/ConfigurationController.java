package com.ticketingSystem.server.controller;

import com.ticketingSystem.server.model.Configuration;
import com.ticketingSystem.server.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configurations")
public class ConfigurationController {

    private final ConfigurationService service;

    @Autowired
    public ConfigurationController(ConfigurationService configurationService) {
        this.service = configurationService;
    }

    // Save a new configuration
    @PostMapping("/save")
    public ResponseEntity<Configuration> saveConfiguration(@RequestBody Configuration configuration) {
        try {
            Configuration savedConfiguration = service.saveConfiguration(configuration);
            return ResponseEntity.ok(savedConfiguration);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Retrieve a configuration by ID
    @GetMapping("/{id}")
    public ResponseEntity<Configuration> getConfigurationById(@PathVariable Long id) {
        try {
            Configuration configuration = service.getConfigurationById(id);
            return ResponseEntity.ok(configuration);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (RuntimeException e) { // Handle case when ID not found
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Retrieve all configurations
    @GetMapping("/getAll")
    public ResponseEntity<List<Configuration>> getAllConfigurations() {
        try {
            List<Configuration> configurations = service.getAllConfigurations();
            return ResponseEntity.ok(configurations);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "Configuration Controller is working!";
    }

    // Delete a configuration by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteConfigurationById(@PathVariable Long id) {
        try {
            service.deleteConfigurationById(id);
            return ResponseEntity.ok("Configuration with ID " + id + " has been deleted.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid ID provided.");
        } catch (RuntimeException e) { // Handle case when ID not found
            return ResponseEntity.status(404).body("Configuration with ID " + id + " not found.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while deleting the configuration.");
        }
    }
}
