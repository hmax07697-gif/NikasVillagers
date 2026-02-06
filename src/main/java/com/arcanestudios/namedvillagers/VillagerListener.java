package com.arcanestudios.namedvillagers;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Listens for villager spawn events and applies custom names
 * 
 * @author Arcane Studios
 * @version 1.0.0
 */
public class VillagerListener implements Listener {
    
    private final NamedVillagers plugin;
    
    public VillagerListener(NamedVillagers plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Handles villager spawn events
     * 
     * Priority: HIGHEST to run after other plugins
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVillagerSpawn(CreatureSpawnEvent event) {
        // Only process villagers
        if (event.getEntityType() != EntityType.VILLAGER) {
            return;
        }
        
        // Check if auto-naming is enabled
        if (!plugin.getConfigManager().isAutoNamingEnabled()) {
            return;
        }
        
        Villager villager = (Villager) event.getEntity();
        
        // Check if villager already has a custom name in PDC
        if (hasCustomName(villager)) {
            if (plugin.getConfigManager().isDebugEnabled()) {
                plugin.getLogger().info("Villager already has custom name, skipping generation");
            }
            return;
        }
        
        // Check if villager already has a display name (from another plugin or manual naming)
        if (villager.customName() != null) {
            if (plugin.getConfigManager().isDebugEnabled()) {
                plugin.getLogger().info("Villager has display name, skipping auto-generation");
            }
            return;
        }
        
        // Generate and apply a new name
        applyGeneratedName(villager);
    }
    
    /**
     * Handles zombie villager to villager transformation (curing)
     * 
     * This preserves or regenerates names based on config
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onVillagerCure(EntityTransformEvent event) {
        // Only process zombie villager -> villager transformations
        if (event.getEntity().getType() != EntityType.ZOMBIE_VILLAGER) {
            return;
        }
        
        if (event.getTransformedEntity().getType() != EntityType.VILLAGER) {
            return;
        }
        
        ZombieVillager zombieVillager = (ZombieVillager) event.getEntity();
        Villager villager = (Villager) event.getTransformedEntity();
        
        // Check if zombie villager had a stored name
        String storedName = getStoredName(zombieVillager);
        
        if (storedName != null) {
            // Preserve the name if rename-on-cure is false
            if (!plugin.getConfigManager().shouldRenameOnCure()) {
                // The name will be automatically transferred via PDC during transformation
                if (plugin.getConfigManager().isDebugEnabled()) {
                    plugin.getLogger().info("Preserving villager name after cure: " + storedName);
                }
                return;
            }
        }
        
        // Generate a new name if rename-on-cure is true or no name existed
        if (plugin.getConfigManager().isAutoNamingEnabled()) {
            // Delay name application by 1 tick to ensure entity is fully transformed
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (villager.isValid() && !villager.isDead()) {
                    applyGeneratedName(villager);
                }
            }, 1L);
        }
    }
    
    /**
     * Applies a generated name to a villager
     */
    private void applyGeneratedName(Villager villager) {
        String name = plugin.getNameGenerator().generateName(villager);
        
        // Store the name in PDC
        storeCustomName(villager, name);
        
        // Apply the name as the villager's custom name
        villager.setCustomName(name);
        villager.setCustomNameVisible(true);
        
        if (plugin.getConfigManager().isDebugEnabled()) {
            plugin.getLogger().info("Generated name for villager: " + name);
        }
    }
    
    /**
     * Checks if a villager has a custom name stored in PDC
     */
    private boolean hasCustomName(Villager villager) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        return pdc.has(NamedVillagers.getCustomNameKey(), PersistentDataType.STRING);
    }
    
    /**
     * Gets the stored custom name from PDC
     */
    private String getStoredName(org.bukkit.entity.Entity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.get(NamedVillagers.getCustomNameKey(), PersistentDataType.STRING);
    }
    
    /**
     * Stores a custom name in the villager's PDC
     */
    private void storeCustomName(Villager villager, String name) {
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.set(NamedVillagers.getCustomNameKey(), PersistentDataType.STRING, name);
    }
}
