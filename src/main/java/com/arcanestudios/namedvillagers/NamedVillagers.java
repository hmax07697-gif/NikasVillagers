package com.arcanestudios.namedvillagers;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * NamedVillagers - Automatic villager naming system
 * 
 * Main plugin class that handles initialization and provides the PDC key
 * for storing custom villager names persistently.
 * 
 * @author Arcane Studios
 * @version 1.0.0
 */
public class NamedVillagers extends JavaPlugin {
    
    private static NamedVillagers instance;
    private static NamespacedKey customNameKey;
    
    private ConfigManager configManager;
    private NameGenerator nameGenerator;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize the NamespacedKey for PDC storage
        customNameKey = new NamespacedKey(this, "custom_name");
        
        // Initialize configuration
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        
        // Initialize name generator
        nameGenerator = new NameGenerator(this);
        
        // Register event listener
        getServer().getPluginManager().registerEvents(new VillagerListener(this), this);
        
        // Register command handler
        CommandHandler commandHandler = new CommandHandler(this);
        getCommand("namedvillagers").setExecutor(commandHandler);
        getCommand("namedvillagers").setTabCompleter(commandHandler);
        
        // Log successful startup
        getLogger().info("NamedVillagers v" + getDescription().getVersion() + " has been enabled!");
        getLogger().info("Automatic naming: " + (configManager.isAutoNamingEnabled() ? "ENABLED" : "DISABLED"));
        getLogger().info("Legendary chance: " + configManager.getLegendaryChance() + "%");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("NamedVillagers has been disabled!");
        instance = null;
    }
    
    /**
     * Gets the plugin instance
     * 
     * @return The NamedVillagers plugin instance
     */
    public static NamedVillagers getInstance() {
        return instance;
    }
    
    /**
     * Gets the NamespacedKey used for storing custom names in PDC
     * 
     * @return The custom name NamespacedKey
     */
    public static NamespacedKey getCustomNameKey() {
        return customNameKey;
    }
    
    /**
     * Gets the configuration manager
     * 
     * @return The ConfigManager instance
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Gets the name generator
     * 
     * @return The NameGenerator instance
     */
    public NameGenerator getNameGenerator() {
        return nameGenerator;
    }
    
    /**
     * Reloads the plugin configuration and reinitializes components
     */
    public void reloadPlugin() {
        reloadConfig();
        configManager = new ConfigManager(this);
        nameGenerator = new NameGenerator(this);
        getLogger().info("Configuration reloaded successfully!");
    }
}
