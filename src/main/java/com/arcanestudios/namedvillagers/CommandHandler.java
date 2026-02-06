package com.arcanestudios.namedvillagers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles all plugin commands
 * 
 * @author Arcane Studios
 * @version 1.0.0
 */
public class CommandHandler implements CommandExecutor, TabCompleter {
    
    private final NamedVillagers plugin;
    
    public CommandHandler(NamedVillagers plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Handle console differently
        if (!(sender instanceof Player)) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                return handleReload(sender);
            }
            sender.sendMessage(plugin.getConfigManager().getMessage("invalid-usage"));
            return true;
        }
        
        Player player = (Player) sender;
        
        // No arguments - show usage
        if (args.length == 0) {
            player.sendMessage(plugin.getConfigManager().getMessage("invalid-usage"));
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "reload":
                return handleReload(sender);
                
            case "random":
                return handleRandomName(player);
                
            default:
                // Treat as custom name
                return handleCustomName(player, String.join(" ", args));
        }
    }
    
    /**
     * Handles the reload subcommand
     */
    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("namedvillagers.reload")) {
            sender.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        plugin.reloadPlugin();
        sender.sendMessage(plugin.getConfigManager().getMessage("config-reloaded"));
        return true;
    }
    
    /**
     * Handles the random name subcommand
     */
    private boolean handleRandomName(Player player) {
        if (!player.hasPermission("namedvillagers.rename")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        Villager villager = getTargetVillager(player);
        if (villager == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("not-looking-at-villager"));
            return true;
        }
        
        // Generate random name
        String name = plugin.getNameGenerator().generateName(villager);
        
        // Apply name
        applyName(villager, name);
        
        // Send success message
        String message = plugin.getConfigManager().getMessage("villager-random-named");
        message = message.replace("{name}", name);
        player.sendMessage(message);
        
        return true;
    }
    
    /**
     * Handles custom name assignment
     */
    private boolean handleCustomName(Player player, String customName) {
        if (!player.hasPermission("namedvillagers.rename")) {
            player.sendMessage(plugin.getConfigManager().getMessage("no-permission"));
            return true;
        }
        
        Villager villager = getTargetVillager(player);
        if (villager == null) {
            player.sendMessage(plugin.getConfigManager().getMessage("not-looking-at-villager"));
            return true;
        }
        
        // Apply custom name
        applyName(villager, customName);
        
        // Send success message
        String message = plugin.getConfigManager().getMessage("villager-renamed");
        message = message.replace("{name}", customName);
        player.sendMessage(message);
        
        return true;
    }
    
    /**
     * Gets the villager the player is looking at
     */
    private Villager getTargetVillager(Player player) {
        // Ray trace to find what the player is looking at
        RayTraceResult result = player.getWorld().rayTraceEntities(
            player.getEyeLocation(),
            player.getLocation().getDirection(),
            5.0, // 5 block range
            entity -> entity instanceof Villager
        );
        
        if (result != null) {
            Entity hitEntity = result.getHitEntity();
            if (hitEntity instanceof Villager) {
                return (Villager) hitEntity;
            }
        }
        
        return null;
    }
    
    /**
     * Applies a name to a villager and stores it in PDC
     */
    private void applyName(Villager villager, String name) {
        // Store in PDC
        PersistentDataContainer pdc = villager.getPersistentDataContainer();
        pdc.set(NamedVillagers.getCustomNameKey(), PersistentDataType.STRING, name);
        
        // Apply as display name
        villager.setCustomName(name);
        villager.setCustomNameVisible(true);
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // First argument - subcommands
            List<String> subCommands = Arrays.asList("random", "reload");
            
            for (String subCmd : subCommands) {
                if (subCmd.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(subCmd);
                }
            }
        }
        
        return completions;
    }
}
