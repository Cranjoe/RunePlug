package com.example.GlassGuy;


import com.example.EthanApiPlugin.Collections.Bank;
import com.example.EthanApiPlugin.Collections.Inventory;
import com.example.EthanApiPlugin.Collections.NPCs;
import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.InteractionApi.*;
import com.example.Packets.*;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import org.apache.commons.lang3.RandomUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@PluginDescriptor(
        name = "GlassGuy",
        description = "Blows glass",
        enabledByDefault = false,
        tags = {"poly", "plugin"}
)
@Slf4j
public class GlassGuyPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    private GlassGuyConfig config;
    @Inject
    private GlassGuyOverlay overlay;
    @Inject
    private KeyManager keyManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ClientThread clientThread;
    public boolean started = false;
    public int timeout = 0;

    private boolean deposit;
    private boolean isMaking;
    private Instant timer;
    State state;

    @Provides
    private GlassGuyConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(GlassGuyConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        isMaking = false;
        keyManager.registerKeyListener(toggle);
        keyManager.registerKeyListener(debugToggle);
        overlayManager.add(overlay);
        timeout = 0;
    }

    @Override
    protected void shutDown() throws Exception {
        isMaking = false;
        keyManager.unregisterKeyListener(toggle);
        keyManager.unregisterKeyListener(debugToggle);
        overlayManager.remove(overlay);
        timeout = 0;
    }


    @Subscribe
    private void onGameTick(GameTick event) {
        if (timeout > 0) {
            timeout--;
            return;
        }
        if (client.getGameState() != GameState.LOGGED_IN || !started) {
            return;
        }
        state = getNextState();
        handleState();
    }

    private State getNextState() {
        if (client.getLocalPlayer().getAnimation() == 884) {
            return State.BLOWING;
        }
        if (Inventory.getItemAmount(567) == 27 || Inventory.getItemAmount(1775) == 0) {
            return State.BANK;
        }
        if (Inventory.getItemAmount(1775) == 27) {
            return State.BLOW;
        }
        return State.BLOWING;
    }

    private void handleState() {
        switch (state) {
            case TIMEOUT:
                timeout--;
                break;
            case BANK:
                doBanking();
                setTimeout();
                break;
            case BLOW:
                blowGlass();
                setTimeout();
                break;
            case BLOWING:
                setTimeout();
                break;
        }
    }


    private void findBank() {
        Optional<TileObject> chest = TileObjects.search().withName("Bank chest").nearestToPlayer();
        Optional<NPC> banker = NPCs.search().withAction("Bank").nearestToPlayer();
        Optional<TileObject> booth = TileObjects.search().withAction("Bank").nearestToPlayer();
        if (chest.isPresent()){
            TileObjectInteraction.interact(chest.get(), "Use");
            return;
        }
        if (booth.isPresent()){
            TileObjectInteraction.interact(booth.get(), "Bank");
            return;
        }
        if (banker.isPresent()){
            NPCInteraction.interact(banker.get(), "Bank");
            return;
        }
        if (!chest.isPresent() && !booth.isPresent() && !banker.isPresent()){
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "couldn't find bank or banker", null);
            EthanApiPlugin.stopPlugin(this);
        }

        if (!deposit) {
            deposit = true;
        }
    }

    private void doBanking() {
        if (!Bank.isOpen()) {
            findBank();
            return;
        }
        BankInventoryInteraction.useItem(567, "Deposit-All");
        BankInteraction.useItem(1775, "Withdraw-All");
    }

    private void blowGlass() {
        Inventory.search().filter(item -> item.getName().contains("pipe")).first().ifPresentOrElse(pipe -> {
            Widget glass = Inventory.search().filter(item -> item.getName().contains("glass")).first().orElseThrow();
            MousePackets.queueClickPacket();
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetOnWidget(pipe, glass);
            MousePackets.queueClickPacket();
            WidgetPackets.queueResumePause(17694739, 27);
        }, () -> {
            client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "couldn't find pipe", null);
            EthanApiPlugin.stopPlugin(this);
        });
    }

    private void checkRunEnergy() {
        if (runIsOff() && client.getEnergy() >= 30 * 100) {
            MousePackets.queueClickPacket();
            WidgetPackets.queueWidgetActionPacket(1, 10485787, -1, -1);
        }
    }

    private boolean runIsOff() {
        return EthanApiPlugin.getClient().getVarpValue(173) == 0;
    }

    private final HotkeyListener toggle = new HotkeyListener(() -> config.toggle()) {
        @Override
        public void hotkeyPressed() {
            toggle();
        }
    };
    private final HotkeyListener debugToggle = new HotkeyListener(() -> config.debugToggle()) {
        @Override
        public void hotkeyPressed() {
            debugToggle();
        }
    };

    private void setTimeout() {
        timeout = RandomUtils.nextInt(config.tickDelayMin(), config.tickDelayMax());
    }

    public void toggle() {
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        started = !started;
        if (!started) {
            this.state = State.TIMEOUT;
        } else {
            timer = Instant.now();
        }
    }

    public String getElapsedTime() {
        if (!started) {
            return "00:00:00";
        }
        Duration duration = Duration.between(timer, Instant.now());
        long durationInMillis = duration.toMillis();
        long second = (durationInMillis / 1000) % 60;
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public void debugToggle() {
        log.info("==DEBUG START==");
        clientThread.invoke(() -> {
            blowGlass();
//            doBanking();
        });

        log.info("==DEBUG END==");
    }
}