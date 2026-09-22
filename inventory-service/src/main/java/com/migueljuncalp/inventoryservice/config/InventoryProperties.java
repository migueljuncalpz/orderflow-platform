package com.migueljuncalp.inventoryservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "inventory")
public record InventoryProperties(
        @DefaultValue("2s") Duration processingDelay
) {
}
