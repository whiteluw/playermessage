package com.example.playermessage;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import org.slf4j.Logger;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.nio.charset.StandardCharsets;
import com.velocitypowered.api.event.connection.DisconnectEvent.LoginStatus;
import com.velocitypowered.api.proxy.ServerConnection;

@Plugin(
    id = "playermessage",
    name = "PlayerMessage",
    version = "1.0.0",
    description = "A plugin to handle player connection messages",
    authors = {"whitelu"}
)
public class VelocityPlugin {
    private static final String CHANNEL = "playermessage:main";
    private final ProxyServer server;
    private final Logger logger;
    private final MinecraftChannelIdentifier identifier;

    @Inject
    public VelocityPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        this.identifier = MinecraftChannelIdentifier.from(CHANNEL);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getChannelRegistrar().register(identifier);
        logger.info("PlayerMessage Velocity plugin enabled");
    }

    @Subscribe
    public void onServerConnect(ServerPreConnectEvent event) {
        Optional<RegisteredServer> currentServer = event.getPlayer().getCurrentServer().map(s -> s.getServer());
        if (currentServer.isPresent() && event.getResult().getServer().isPresent()) {
            String playerName = event.getPlayer().getUsername();
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    logger.error("Thread was interrupted while waiting", e);
                }
            });
            String message = "§e" + playerName + "已连接到其他服务器";
            server.getAllServers().forEach(server -> {
                server.sendPluginMessage(identifier, message.getBytes(StandardCharsets.UTF_8));
            });
        }
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        if (event.getLoginStatus() != LoginStatus.SUCCESSFUL_LOGIN) {
            return;
        }

        Optional<ServerConnection> serverConnection = event.getPlayer().getCurrentServer();
        if (serverConnection.isPresent()) {
            String playerName = event.getPlayer().getUsername();
            String message = "§e" + playerName + "离开了游戏";
            server.getAllServers().forEach(server -> {
                server.sendPluginMessage(identifier, message.getBytes(StandardCharsets.UTF_8));
            });
            logger.info("Player " + playerName + " has left the game");
        }
    }
} 