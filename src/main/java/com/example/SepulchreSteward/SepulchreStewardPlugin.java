package com.example.SepulchreSteward;


import com.example.EthanApiPlugin.Collections.NPCs;
import com.example.EthanApiPlugin.Collections.TileObjects;
import com.example.EthanApiPlugin.EthanApiPlugin;
import com.example.EthanApiPlugin.Utility.TileObjectUtility;
import com.example.EthanApiPlugin.Utility.WorldAreaUtility;
import com.example.Packets.MousePackets;
import com.example.Packets.WidgetPackets;
import com.example.SepulchreSteward.Objects.Fire;
import com.example.SepulchreSteward.data.Const;
import com.google.inject.Inject;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

import java.util.*;


@PluginDescriptor(
        name = "SepulchreSteward",
        description = "Your personal steward for the Hallowed Sepulchre.",
        enabledByDefault = false,
        tags = {"poly", "plugin"}
)
@Slf4j
public class SepulchreStewardPlugin extends Plugin {
    @Inject
    private Client client;
    @Inject
    private SepulchreStewardConfig config;
    @Inject
    private SepulchreStewardOverlay overlay;
    @Inject
    private KeyManager keyManager;
    @Inject
    private OverlayManager overlayManager;
    @Inject
    private ClientThread clientThread;
    private boolean started = false;
    public int timeout = 0;

    static ArrayList<WorldPoint> statues = new ArrayList<>();
    static ArrayList<WorldPoint> tileTesting = new ArrayList<>();
    static ArrayList<WorldPoint> pathTesting = new ArrayList<>();
    static ArrayList<Fire> badFires = new ArrayList<>();
    static Map<Fire, Integer> fireStartTimes = new HashMap();


    @Provides
    private SepulchreStewardConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(SepulchreStewardConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        keyManager.registerKeyListener(toggle);
        keyManager.registerKeyListener(debugToggle);
        fireStartTimes.clear();
        badFires.clear();
        overlayManager.add(overlay);
        timeout = 0;
    }

    @Override
    protected void shutDown() throws Exception {
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

        findFires();
        findDarts();
        //pathToStair();
    }

    @Subscribe
    private void onChatMessage(final ChatMessage message) {
        if (client.getGameState() != GameState.LOGGED_IN || !started
                || message.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }

        switch (message.getMessage())
        {
            case Const.GAME_MESSAGE_ENTER_FLOOR:
            case Const.GAME_MESSAGE_ENTER_FLOOR_2:
                fireStartTimes.clear();
                badFires.clear();

        }
    }

//    private void waitForAnimationChange() {
//        final int initialDelay = 0; // Start checking immediately
//        final int period = 100; // Check every 100 milliseconds (0.1 seconds)
//
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                int currentAnimationId = fire.getAnimationId();
//                if (currentAnimationId == Fire.ANIMATION_START || currentAnimationId == Fire.ANIMATION_MID
//                        || currentAnimationId == Fire.ANIMATION_END  ) {
//                    int inferredTick = Fire.inferTickFromAnimation(currentAnimationId);
//                    if (inferredTick != -1) {
//                        fire.setCurrentTick(inferredTick);
//                        timer.cancel();
//                    }
//                }
//            }
//        }, initialDelay, period);
//    }

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

    public void toggle() {
        if (client.getGameState() != GameState.LOGGED_IN) {
            return;
        }
        log.info("SepulchreStewardPlugin: " + (started ? "Stopping" : "Starting") + " plugin");
        started = !started;
    }

    private void pathToStair() {
        pathTesting.clear();

        List<Integer> stairIds = List.of(38462, 38463, 38464, 38465, 38467, 38469, 38471, 38472);
        TileObjects.search().idInList(stairIds).nearestByPath().ifPresent(stair -> {
            HashSet<WorldPoint> set = new HashSet<>();
            WorldAreaUtility.objectInteractableTiles(stair).forEach(wp -> {
                if (wp != null) {
                    set.add(wp);
                }
            });
            List<WorldPoint> path = EthanApiPlugin.pathToGoalSetFromPlayerNoCustomTiles(set);
            if (path != null && !path.isEmpty()){
                WorldPoint goal = path.get(path.size() - 1);
                List<WorldPoint> wams = EthanApiPlugin.pathToClosestGoalFromPlayerUsingCustomDangerous(goal, new HashSet<>(tileTesting));
                if (!wams.isEmpty()) {
                    for (WorldPoint wp : wams) {
                        if (wp != null) {
                            pathTesting.add(wp);
                        }
                    }
                }
            }
        });
    }


    private void findFires() {
        tileTesting.clear();

        //Collect all bad fires
        TileObjects.search().withId(Const.FIRE_ID)
                .result().forEach(fireObject -> {
                    if (fireObject != null) {
                        int currentAnimationId = TileObjectUtility.getAnimationId(fireObject);
                        if (currentAnimationId == Fire.ANIMATION_START || currentAnimationId == Fire.ANIMATION_MID || currentAnimationId == Fire.ANIMATION_END)  { //                    WorldPoint fireLocation = fire.getFireObject().getWorldLocation();
                            Fire fire = new Fire(fireObject);
                            int inferredTick = Fire.inferTickFromAnimation(currentAnimationId);
                            if (inferredTick != -1 && !badFires.contains(fire)) {
                                fire.setAnimationId(currentAnimationId);
                                fire.setCurrentTick(inferredTick);
                                badFires.add(fire);
                            }
                        }
                    }
                });

        //Check if the bad fires have changed animation, if so, calculate the cycle length
        badFires.forEach(fire -> {
            int currentAnimationId = TileObjectUtility.getAnimationId(fire.getFireObject());
            if (fire.getAnimationId() == currentAnimationId) {
                if (fireStartTimes.containsKey(fire)) {
                    // Calculate the cycle length
                    int cycleLength = client.getTickCount() - fireStartTimes.get(fire);
                    fire.setMaxTick(cycleLength);
                    fireStartTimes.remove(fire);

                } else {
                    // This is the first time this fire object is seen at ANIMATION_START
                    fireStartTimes.put(fire, client.getTickCount());
                }
            }
        });

        //Highlight the bad tiles during the bad ticks
        List<Integer> badTicks = Arrays.asList(1,2,3); // TODO: Const somewhere
        for (Fire fire : badFires) {
            if (badTicks.contains(fire.getCurrentTick()) || fire.getCurrentTick() == fire.getMaxTick()) {
                int orientation = ((GameObject) fire.getFireObject()).getOrientation();
                WorldPoint fireLocation = fire.getFireObject().getWorldLocation();
                if (orientation == 1536 || orientation == 512) {
                    if (!tileTesting.contains(fireLocation)) {
                        tileTesting.add(fireLocation);
                        tileTesting.add(fireLocation.dx(1));
                        tileTesting.add(fireLocation.dx(-1));
                    }
                }
                if (orientation == 0 || orientation == 1024) {
                    if (!tileTesting.contains(fireLocation)) {
                        tileTesting.add(fireLocation);
                        tileTesting.add(fireLocation.dy(1));
                        tileTesting.add(fireLocation.dy(-1));
                    }
                }
            }
            fire.updateTick(); //Update ticks for all fires
        }
    }

    private void findDarts() {
        statues.clear();
        List<Integer> crossbowmanIds = Arrays.asList(Const.CROSSBOW_ID_1, 38445);
        TileObjects.search().idInList(crossbowmanIds).result().forEach(dartStatue -> {
            if (dartStatue != null) {
                int animationId = TileObjectUtility.getAnimationId(dartStatue);
                if (animationId != 8681 && animationId != -1) {
                    WorldPoint wp = dartStatue.getWorldLocation();
                    statues.add(wp);
                }
            }
        });

        for (NPC npc : NPCs.search().withId(9673).result()) {
            if (npc.getOrientation() == 1536 || npc.getOrientation() == 512) {
                WorldPoint wp = npc.getWorldLocation();
                tileTesting.add(wp);
                tileTesting.add(wp.dx(1));
                tileTesting.add(wp.dx(-1));
            }
            if (npc.getOrientation() == 0 || npc.getOrientation() == 1024) {
                WorldPoint wp = npc.getWorldLocation();
                tileTesting.add(wp);
                tileTesting.add(wp.dy(1));
                tileTesting.add(wp.dy(-1));
            }
        }
    }

    public void debugToggle() {
        log.info("==SEPULCHRE DEBUG START==");
        tileTesting.clear();
        pathTesting.clear();


        NPCs.search().withId(9672).nearestToPlayer().ifPresent(npc -> {
            if (npc.getOrientation() == 1536 || npc.getOrientation() == 512) {
                WorldPoint wp = npc.getWorldLocation();
                tileTesting.add(wp);
                tileTesting.add(wp.dx(1));
                tileTesting.add(wp.dx(-1));
            }
        });

        clientThread.invoke(() -> {
            client.getGraphicsObjects().forEach(graphicsObject -> {
                if (graphicsObject.getId() == 1796) {
                    graphicsObject.getLocation();
                    tileTesting.add(WorldPoint.fromLocal(client, graphicsObject.getLocation()));
                }

                NPCs.search().withId(9672).result().forEach(npc -> {
                    if (npc != null) {
                        WorldPoint wp = npc.getWorldLocation();
                        log.info("npc: " + wp);
                        if (wp != null) {
                            tileTesting.add(wp);
                        }
                    }
                });
            });


        });

        log.info("==SEPULCHRE DEBUG END==");
    }
}