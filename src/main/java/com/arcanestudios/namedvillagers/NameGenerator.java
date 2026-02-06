package com.arcanestudios.namedvillagers;

import org.bukkit.entity.Villager;
import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.List;
import java.util.Random;

/**
 * Generates random names for villagers based on profession, biome, and configuration
 * 
 * @author Arcane Studios
 * @version 1.0.0
 */
public class NameGenerator {
    
    private final NamedVillagers plugin;
    private final ConfigManager config;
    private final Random random;
    
    public NameGenerator(NamedVillagers plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
        this.random = new Random();
    }
    
    /**
     * Generates a complete name for a villager
     * 
     * @param villager The villager to name
     * @return The generated name
     */
    public String generateName(Villager villager) {
        String profession = getProfessionKey(villager);
        String biome = getBiomeKey(villager.getLocation());
        
        // Get name components
        String firstName = getFirstName(profession, biome);
        String lastName = getLastName(profession, biome);
        String legendaryPrefix = getLegendaryPrefix();
        String professionTitle = getProfessionTitle(villager);
        String levelTitle = getLevelTitle(villager);
        
        // Build the name based on format
        return buildName(firstName, lastName, legendaryPrefix, professionTitle, levelTitle);
    }
    
    /**
     * Gets a random first name from appropriate pools
     */
    private String getFirstName(String profession, String biome) {
        List<String> names = null;
        
        // Try biome-specific names first if enabled
        if (config.isBiomeSpecific()) {
            names = config.getBiomeFirstNames(biome);
        }
        
        // Fall back to profession names if no biome names or biome-specific disabled
        if (names == null || names.isEmpty()) {
            names = config.getProfessionFirstNames(profession);
        }
        
        // Fallback to "none" profession if no names found
        if (names == null || names.isEmpty()) {
            names = config.getProfessionFirstNames("none");
        }
        
        return getRandomElement(names, "Unknown");
    }
    
    /**
     * Gets a random last name from appropriate pools
     */
    private String getLastName(String profession, String biome) {
        List<String> names = null;
        
        // Try biome-specific names first if enabled
        if (config.isBiomeSpecific()) {
            names = config.getBiomeLastNames(biome);
        }
        
        // Fall back to profession names if no biome names or biome-specific disabled
        if (names == null || names.isEmpty()) {
            names = config.getProfessionLastNames(profession);
        }
        
        // Fallback to "none" profession if no names found
        if (names == null || names.isEmpty()) {
            names = config.getProfessionLastNames("none");
        }
        
        return getRandomElement(names, "");
    }
    
    /**
     * Gets a legendary prefix if the RNG roll succeeds
     */
    private String getLegendaryPrefix() {
        int chance = config.getLegendaryChance();
        if (chance <= 0) {
            return null;
        }
        
        int roll = random.nextInt(100);
        if (roll < chance) {
            List<String> prefixes = config.getLegendaryPrefixes();
            return getRandomElement(prefixes, null);
        }
        
        return null;
    }
    
    /**
     * Gets the profession title for the villager
     */
    private String getProfessionTitle(Villager villager) {
        Villager.Profession profession = villager.getProfession();
        
        switch (profession) {
            case ARMORER: return "Armorer";
            case BUTCHER: return "Butcher";
            case CARTOGRAPHER: return "Cartographer";
            case CLERIC: return "Cleric";
            case FARMER: return "Farmer";
            case FISHERMAN: return "Fisherman";
            case FLETCHER: return "Fletcher";
            case LEATHERWORKER: return "Leatherworker";
            case LIBRARIAN: return "Librarian";
            case MASON: return "Mason";
            case SHEPHERD: return "Shepherd";
            case TOOLSMITH: return "Toolsmith";
            case WEAPONSMITH: return "Weaponsmith";
            case NITWIT: return "Nitwit";
            case NONE: return "Villager";
            default: return "Villager";
        }
    }
    
    /**
     * Gets the level title for the villager
     */
    private String getLevelTitle(Villager villager) {
        if (!config.shouldShowLevel()) {
            return null;
        }
        
        int level = villager.getVillagerLevel();
        
        switch (level) {
            case 1: return "Novice";
            case 2: return "Apprentice";
            case 3: return "Journeyman";
            case 4: return "Expert";
            case 5: return "Master";
            default: return null;
        }
    }
    
    /**
     * Builds the final name based on format settings
     */
    private String buildName(String firstName, String lastName, String legendary, 
                            String profession, String level) {
        String format = config.getNameFormat();
        StringBuilder name = new StringBuilder();
        
        // Add legendary prefix if present
        if (legendary != null && !legendary.isEmpty()) {
            name.append(legendary).append(" ");
        }
        
        // Add first name
        name.append(firstName);
        
        // Handle different formats
        switch (format.toLowerCase()) {
            case "simple":
                // Just the first name (with optional legendary prefix)
                break;
                
            case "fullname":
                // First and last name
                if (lastName != null && !lastName.isEmpty()) {
                    name.append(" ").append(lastName);
                }
                break;
                
            case "titled":
            default:
                // First name + "the" + level + profession
                name.append(" the");
                if (level != null && !level.isEmpty()) {
                    name.append(" ").append(level);
                }
                name.append(" ").append(profession);
                break;
        }
        
        return name.toString();
    }
    
    /**
     * Gets the profession key for config lookup
     */
    private String getProfessionKey(Villager villager) {
        return villager.getProfession().name().toLowerCase();
    }
    
    /**
     * Gets the biome key for config lookup
     */
    private String getBiomeKey(Location location) {
        Biome biome = location.getBlock().getBiome();
        String biomeName = biome.name().toLowerCase();
        
        // Map similar biomes to config categories
        if (biomeName.contains("desert")) {
            return "desert";
        } else if (biomeName.contains("plains")) {
            return "plains";
        } else if (biomeName.contains("taiga") || biomeName.contains("forest")) {
            return "taiga";
        } else if (biomeName.contains("jungle")) {
            return "jungle";
        } else if (biomeName.contains("swamp")) {
            return "swamp";
        } else if (biomeName.contains("savanna")) {
            return "savanna";
        } else if (biomeName.contains("snow") || biomeName.contains("frozen") || biomeName.contains("ice")) {
            return "snowy";
        } else if (biomeName.contains("mountain") || biomeName.contains("peak") || biomeName.contains("hill")) {
            return "mountains";
        } else if (biomeName.contains("mushroom")) {
            return "mushroom_fields";
        }
        
        // Default to plains
        return "plains";
    }
    
    /**
     * Gets a random element from a list
     */
    private String getRandomElement(List<String> list, String defaultValue) {
        if (list == null || list.isEmpty()) {
            return defaultValue;
        }
        return list.get(random.nextInt(list.size()));
    }
}
