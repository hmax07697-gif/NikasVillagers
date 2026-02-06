package com.arcanestudios.namedvillagers;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;

/**
 * Manages plugin configuration and provides easy access to settings
 * 
 * @author Arcane Studios
 * @version 1.0.0
 */
public class ConfigManager {
    
    private final NamedVillagers plugin;
    private final FileConfiguration config;
    
    public ConfigManager(NamedVillagers plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }
    
    /**
     * Checks if automatic naming is enabled
     * 
     * @return true if auto-naming is enabled
     */
    public boolean isAutoNamingEnabled() {
        return config.getBoolean("settings.auto-name", true);
    }
    
    /**
     * Gets the legendary prefix chance percentage
     * 
     * @return Legendary chance (0-100)
     */
    public int getLegendaryChance() {
        return config.getInt("settings.legendary-chance", 5);
    }
    
    /**
     * Gets the name format setting
     * 
     * @return Name format: simple, titled, or fullname
     */
    public String getNameFormat() {
        return config.getString("settings.format", "titled");
    }
    
    /**
     * Checks if profession level should be shown
     * 
     * @return true if level should be shown
     */
    public boolean shouldShowLevel() {
        return config.getBoolean("settings.show-level", true);
    }
    
    /**
     * Checks if villagers should be renamed when cured
     * 
     * @return true if villagers should be renamed on cure
     */
    public boolean shouldRenameOnCure() {
        return config.getBoolean("settings.rename-on-cure", false);
    }
    
    /**
     * Checks if biome-specific names should be used
     * 
     * @return true if biome-specific naming is enabled
     */
    public boolean isBiomeSpecific() {
        return config.getBoolean("settings.biome-specific", true);
    }
    
    /**
     * Checks if debug mode is enabled
     * 
     * @return true if debug mode is enabled
     */
    public boolean isDebugEnabled() {
        return config.getBoolean("settings.debug", false);
    }
    
    /**
     * Gets a list of legendary prefixes
     * 
     * @return List of legendary prefix strings
     */
    public List<String> getLegendaryPrefixes() {
        return config.getStringList("legendary-prefixes");
    }
    
    /**
     * Gets first names for a specific profession
     * 
     * @param profession The profession name (lowercase)
     * @return List of first names
     */
    public List<String> getProfessionFirstNames(String profession) {
        return config.getStringList("names.professions." + profession + ".first");
    }
    
    /**
     * Gets last names for a specific profession
     * 
     * @param profession The profession name (lowercase)
     * @return List of last names
     */
    public List<String> getProfessionLastNames(String profession) {
        return config.getStringList("names.professions." + profession + ".last");
    }
    
    /**
     * Gets first names for a specific biome
     * 
     * @param biome The biome name (lowercase)
     * @return List of first names
     */
    public List<String> getBiomeFirstNames(String biome) {
        return config.getStringList("biomes." + biome + ".first");
    }
    
    /**
     * Gets last names for a specific biome
     * 
     * @param biome The biome name (lowercase)
     * @return List of last names
     */
    public List<String> getBiomeLastNames(String biome) {
        return config.getStringList("biomes." + biome + ".last");
    }
    
    /**
     * Gets a message from configuration with color codes translated
     * 
     * @param path The message path in config
     * @return The formatted message with color codes
     */
    public String getMessage(String path) {
        String prefix = config.getString("messages.prefix", "&8[&6NamedVillagers&8]&r ");
        String message = config.getString("messages." + path, "");
        
        // Only add prefix to non-empty messages and not to the prefix itself
        if (!message.isEmpty() && !path.equals("prefix")) {
            message = prefix + message;
        }
        
        return translateColorCodes(message);
    }
    
    /**
     * Translates & color codes to Minecraft color codes
     * 
     * @param message The message with & codes
     * @return The message with § codes
     */
    private String translateColorCodes(String message) {
        return message.replace('&', '§');
    }
}
