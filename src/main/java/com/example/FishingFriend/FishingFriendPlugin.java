package com.example.FishingFriend;


import com.example.EthanApiPlugin.Collections.*;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.EthanApiPlugin.Utility.TileObjectUtility;
import com.example.FishingFriend.Data.Const;
import com.example.FishingFriend.Data.State;
import com.example.InteractionApi.BankInventoryInteraction;
import com.example.InteractionApi.InventoryInteraction;
import com.example.InteractionApi.NPCInteraction;
import com.example.InteractionApi.TileObjectInteraction;
import com.example.Packets.MousePackets;
import com.example.Packets.MovementPackets;
import com.example.Packets.WidgetPackets;
import com.example.PathingTesting.PathingTesting;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.World;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.PostAnimation;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.RandomUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@PluginDescriptor(
        name = "Fishing Friend",
        description = "Your friendly neighborhood fisher",
        enabledByDefault = false,
        tags = {"poly", "plugin"}
)
@Slf4j
public class FishingFriendPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    FishingFriendConfig config;
    @Inject
    private FishingFriendOverlay overlay;
    @Inject
    private KeyManager keyManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ClientThread clientThread;
    boolean started = false;
    public int timeout = 0;
    private Instant timer;

    boolean bankPin = false;
    State state;

    @Provides
    private FishingFriendConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(FishingFriendConfig.class);
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
        if (Inventory.full() && shouldBank()) {
            return State.BANK;
        }
        if (Bank.isOpen() && hasBadFish()) {
            return State.BANK;
        }
        if ((isDroppingItems()) && hasBadFish() || Inventory.full()) {
            // if the user should be dropping items, we'll check if they're done
            // should sit at this state til it's finished.
            return State.DROP_ITEMS;
        }
        return State.FIND_OBJECT;
    }

    private void handleState() {
        switch (state) {
            case TIMEOUT:
                timeout--;
                break;
            case BANK:
                if (Widgets.search().withId(13959169).first().isPresent()) {
                    bankPin = true;
                    return;
                }
                if (Widgets.search().withId(786445).first().isEmpty()) {
                    TileObjects.search().withAction("Bank").nearestToPlayer().ifPresent(tileObject -> {
                        TileObjectInteraction.interact(tileObject, "Bank");
                    });
                    NPCs.search().withAction("Bank").nearestToPlayer().ifPresent(npc -> {
                        HashSet<WorldPoint> points = new HashSet<>();
                        points.add(npc.getWorldLocation());
                        if (EthanApiPlugin.pathToGoalSetFromPlayerNoCustomTiles(points) != null) {
                            NPCInteraction.interact(npc, "Bank");
                        }
                    });
                    TileObjects.search().withName("Bank chest").nearestToPlayer().ifPresent(tileObject -> {
                        TileObjectInteraction.interact(tileObject, "Use");
                    });
                    if (TileObjects.search().withAction("Bank").nearestToPlayer().isEmpty() && NPCs.search().withAction("Bank").nearestToPlayer().isEmpty()) {
                        EthanApiPlugin.sendClientMessage("Bank is not found, move to an area with a bank.");
                    }
                    return;
                }
                List<Widget> items = BankInventory.search().result();
                for (Widget item : items) {
                    if (!item.getName().toLowerCase().contains("barbarian rod") && !item.getName().toLowerCase().contains("feather"))  {
                        BankInventoryInteraction.useItem(item, "Deposit-All");
                        return;
                    }
                }
                break;
            case ANIMATING:
                setTimeout();
                break;
            case FIND_OBJECT:
                findFishingSpot();
                setTimeout();
                break;
            case DROP_ITEMS:
                dropItems();
                break;
        }
    }

    private void findFishingSpot() {
        NPCs.search().withId(Const.BARB_FISHING_SPOT_ID).nearestToPlayer().ifPresentOrElse(fishingSpot -> {
            NPCInteraction.interact(fishingSpot, Const.FISHING_INTERACTION);
        },
        () -> {
            WorldPoint wp = new WorldPoint(1266, 3551, 0);
            log.info("Walking to fishing spot");
            MousePackets.queueClickPacket();
            MovementPackets.queueMovement(wp);
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

    private void dropItems() {
        if (!config.bankSturgeon()) {
            List<Widget> itemsToDrop = Inventory.search()
                    .filter(item -> item.getName().contains(Const.TROUT) || item.getName().contains(Const.SALMON) || item.getName().contains(Const.STURGEON))
                    .result();

            for (int i = 0; i < Math.min(itemsToDrop.size(), RandomUtils.nextInt(config.dropPerTickOne(), config.dropPerTickTwo())); i++) {
                InventoryInteraction.useItem(itemsToDrop.get(i), "Drop");
            }
        } else {
            List<Widget> itemsToDrop = Inventory.search()
                    .filter(item -> item.getName().contains(Const.TROUT) || item.getName().contains(Const.SALMON))
                    .result();

            for (int i = 0; i < Math.min(itemsToDrop.size(), RandomUtils.nextInt(config.dropPerTickOne(), config.dropPerTickTwo())); i++) {
                InventoryInteraction.useItem(itemsToDrop.get(i), "Drop");
            }
        }
    }

    private boolean hasBadFish() {
        List<Widget> inventory = Inventory.search().result();
        for (Widget item : inventory) {
            if (!config.bankSturgeon()) {
                if (item.getName().contains(Const.TROUT) || item.getName().contains(Const.SALMON) || item.getName().contains(Const.STURGEON)) {
                    return true;
                }
            }
            if (item.getName().contains(Const.TROUT) || item.getName().contains(Const.SALMON)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldBank() {
        if (!config.bankSturgeon()) {
            return false;
        }

        List<Widget> inventory = Inventory.search().result();
        int i = 0;
        for (Widget item : inventory) {
            if (item.getName().contains(Const.TROUT) || item.getName().contains(Const.SALMON)) {
                i++;
            }
        }
        return i < 4;
    }

    private boolean isDroppingItems() {
        return state == State.DROP_ITEMS;
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