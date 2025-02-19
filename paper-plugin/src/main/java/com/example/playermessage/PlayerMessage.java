package com.example.playermessage;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import net.kyori.adventure.text.Component;

public class PlayerMessage extends JavaPlugin implements PluginMessageListener {
    private static final String CHANNEL = "playermessage:main";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getMessenger().registerIncomingPluginChannel(this, CHANNEL, this);
        
        getLogger().info("PlayerMessage plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getMessenger().unregisterIncomingPluginChannel(this, CHANNEL);
        getLogger().info("PlayerMessage plugin has been disabled!");
    }

    @Override
    public void onPluginMessageReceived(String channel, org.bukkit.entity.Player player, byte[] message) {
        if (!channel.equals(CHANNEL)) return;
        String messageStr = new String(message);
        getServer().broadcast(Component.text(messageStr));
    }
} 