package com.ticketingSystem.server.service;

import com.ticketingSystem.server.model.Configuration;
import com.ticketingSystem.server.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationService {
    private final ConfigurationRepository repository;

    @Autowired
    public ConfigurationService(ConfigurationRepository repository) {
        if (repository == null) {
            throw new IllegalStateException("Repository injection failed!");
        }
        this.repository = repository;
    }


    public List<Configuration> getAllConfigurations() {
        return repository.findAll();
    }

    public Configuration saveConfiguration(Configuration configuration) {
        return repository.save(configuration);
    }

    public Configuration getConfigurationById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteConfigurationById(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Configuration not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
