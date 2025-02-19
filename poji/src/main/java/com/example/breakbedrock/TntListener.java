package com.example.breakbedrock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class TntListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block placedBlock = event.getBlock();
        ItemStack itemInHand = event.getItemInHand();
        
        // 检查是否是我们的特殊TNT
        if (placedBlock.getType() == Material.TNT && 
            itemInHand.hasItemMeta() && 
            itemInHand.getItemMeta().displayName() != null && 
            itemInHand.getItemMeta().displayName().toString().contains("破鸡")) {
            
            Block blockBelow = placedBlock.getLocation().subtract(0, 1, 0).getBlock();
            
            // 取消原版的方块放置
            event.setCancelled(true);
            
            // 减少玩家手中的物品数量
            ItemStack newItemInHand = itemInHand.clone();
            newItemInHand.setAmount(itemInHand.getAmount() - 1);
            event.getPlayer().getInventory().setItemInMainHand(newItemInHand);
            
            // 如果下面是基岩
            if (blockBelow.getType() == Material.BEDROCK) {
                // 移除基岩
                blockBelow.setType(Material.AIR);
                
                // 播放特效
                blockBelow.getWorld().spawnParticle(Particle.EXPLOSION, 
                    blockBelow.getLocation().add(0.5, 0.5, 0.5), 
                    1);
                blockBelow.getWorld().playSound(
                    blockBelow.getLocation(),
                    Sound.ENTITY_GENERIC_EXPLODE,
                    1.0f,
                    1.0f
                );
            } else {
                // 如果不是基岩，掉落物品
                blockBelow.getWorld().dropItemNaturally(
                    placedBlock.getLocation(),
                    BreakBedrock.createSpecialTnt()
                );
            }
        }
    }
} 