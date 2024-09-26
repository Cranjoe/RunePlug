package com.example.DaeyaltDispenser;


import com.example.DaeyaltDispenser.Data.Const;
import com.example.DaeyaltDispenser.Data.State;
import com.example.EthanApiPlugin.Collections.*;
import com.example.EthanApiPlugin.Collections.query.TileObjectQuery;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.EthanApiPlugin.Utility.TileObjectUtility;
import com.example.InteractionApi.BankInventoryInteraction;
import com.example.InteractionApi.NPCInteraction;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.Packets.*;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PostAnimation;
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
import java.util.HashSet;
import java.util.List;

@PluginDescriptor(
        name = "DaeyaltDispenser",
        description = "Dispenses Daeyalt Essence",
        enabledByDefault = false,
        tags = {"poly", "plugin"}
)
@Slf4j
public class DaeyaltDispenserPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    DaeyaltDispenserConfig config;
    @Inject
    private DaeyaltDispenserOverlay overlay;
    @Inject
    private KeyManager keyManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ClientThread clientThread;
    boolean started = false;
    public int timeout = 0;
    private Instant timer;

    State state;

    @Provides
    private DaeyaltDispenserConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(DaeyaltDispenserConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        keyManager.registerKeyListener(toggle);
        overlayManager.add(overlay);
        timeout = 0;
    }

    @Override
    protected void shutDown() throws Exception {
        keyManager.unregisterKeyListener(toggle);
        overlayManager.remove(overlay);
        timer = null;
        timeout = 0;
    }

    @Subscribe
    private void onPostAnimation(PostAnimation event) {
        log.info("Animation: " + event.getAnimation().getId());
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
        if (EthanApiPlugin.isMoving() || client.getLocalPlayer().getAnimation() != -1) {
            return State.ANIMATING;
        }
        if (timeout > 0) {
            return State.TIMEOUT;
        }
        return State.FIND_OBJECT;
    }

    private void handleState() {
        switch (state) {
            case TIMEOUT:
                timeout--;
                break;
            case ANIMATING:
                setTimeout();
            case FIND_OBJECT:
                findDaeyalt();
                setTimeout();
                break;
        }
    }

    private void findDaeyalt() {
        TileObjects.search().withName(Const.DAEYALT_ESSENCE).result().forEach(daeyalt -> {
            if (TileObjectUtility.getAnimationId(daeyalt) == 505) {
                TileObjectInteraction.interact(daeyalt, "Mine");
                return;
            }
        });
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
}