package com.example.breakbedrock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class BreakBedrock extends JavaPlugin {
    private static BreakBedrock instance;
    private NamespacedKey specialTntKey;

    @Override
    public void onEnable() {
        instance = this;
        specialTntKey = new NamespacedKey(this, "special_tnt");
        
        // 注册配方
        registerSpecialTntRecipe();
        
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new TntListener(), this);
        
        getLogger().info("BreakBedrock plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("BreakBedrock plugin has been disabled!");
    }

    private void registerSpecialTntRecipe() {
        ItemStack specialTnt = createSpecialTnt();
        
        ShapelessRecipe recipe = new ShapelessRecipe(specialTntKey, specialTnt);
        recipe.addIngredient(Material.ENDER_PEARL);
        recipe.addIngredient(Material.PISTON);
        recipe.addIngredient(Material.TNT);
        recipe.addIngredient(Material.DIAMOND);
        
        Bukkit.addRecipe(recipe);
    }

    public static ItemStack createSpecialTnt() {
        ItemStack tnt = new ItemStack(Material.TNT);
        ItemMeta meta = tnt.getItemMeta();
        
        meta.displayName(Component.text("破鸡").color(NamedTextColor.RED));
        meta.addEnchant(Enchantment.SHARPNESS, 5, true);
        
        tnt.setItemMeta(meta);
        return tnt;
    }

    public static BreakBedrock getInstance() {
        return instance;
    }
} 