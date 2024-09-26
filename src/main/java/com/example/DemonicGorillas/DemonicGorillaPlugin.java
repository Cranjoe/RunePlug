package com.example.DemonicGorillas;

import com.google.common.collect.ImmutableSet;
import com.example.KotoriUtils.rlapi.WorldAreaExtended;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.HeadIcon;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(name = "<html><font color=#6b8af6>[P]</font> Demonic Gorillas</html>", enabledByDefault = false, description = "Count demonic gorilla attacks and display their next possible attack styles", tags = {"combat", "overlay", "pve", "pvm", "demonics", "gorilla", "ported", "kotori"})
public class DemonicGorillaPlugin extends Plugin {
    private static final Set<Integer> DEMONIC_PROJECTILES = (Set<Integer>)ImmutableSet.of(Integer.valueOf(1302), Integer.valueOf(1304), Integer.valueOf(856));

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private DemonicGorillaOverlay overlay;

    @Inject
    private ClientThread clientThread;

    private Map<NPC, DemonicGorilla> gorillas;

    private List<WorldPoint> recentBoulders;

    private List<PendingGorillaAttack> pendingAttacks;

    private Map<Player, MemorizedPlayer> memorizedPlayers;

    private ArrayList<Projectile> gorillaProjectiles;

    Map<NPC, DemonicGorilla> getGorillas() {
        return this.gorillas;
    }

    private static final Set<Integer> REGION_IDS = Set.of(Integer.valueOf(8280), Integer.valueOf(8536));

    private boolean atGorillas;

    protected void startUp() {
        if (this.client.getGameState() != GameState.LOGGED_IN || !atDemonicGorillas())
            return;
        init();
    }

    protected void shutDown() {
        this.atGorillas = false;
        this.overlayManager.remove(this.overlay);
        this.gorillas = null;
        this.recentBoulders = null;
        this.pendingAttacks = null;
        this.memorizedPlayers = null;
        this.gorillaProjectiles = null;
    }

    private void init() {
        this.atGorillas = true;
        this.overlayManager.add(this.overlay);
        this.gorillas = new HashMap<>();
        this.recentBoulders = new ArrayList<>();
        this.pendingAttacks = new ArrayList<>();
        this.gorillaProjectiles = new ArrayList<>();
        this.memorizedPlayers = new HashMap<>();
        this.clientThread.invoke(this::reset);
    }

    private void clear() {
        this.recentBoulders.clear();
        this.pendingAttacks.clear();
        this.memorizedPlayers.clear();
        this.gorillas.clear();
    }

    private void reset() {
        this.recentBoulders.clear();
        this.pendingAttacks.clear();
        resetGorillas();
        resetPlayers();
    }

    private void resetGorillas() {
        this.gorillas.clear();
        for (NPC npc : client.getNpcs()) {
            if (isNpcGorilla(npc.getId()))
                this.gorillas.put(npc, new DemonicGorilla(npc, this.client));
        }
    }

    private void resetPlayers() {
        this.memorizedPlayers.clear();
        for (Player player : client.getPlayers())
            this.memorizedPlayers.put(player, new MemorizedPlayer(player));
    }

    private static boolean isNpcGorilla(int npcId) {
        return (npcId == 7144 || npcId == 7145 || npcId == 7146 || npcId == 7147 || npcId == 7148 || npcId == 7149);
    }

    private void checkGorillaAttackStyleSwitch(DemonicGorilla gorilla, DemonicGorilla.AttackStyle... protectedStyles) {
        if (gorilla.getAttacksUntilSwitch() <= 0 || gorilla
                .getNextPosibleAttackStyles().isEmpty()) {
//            gorilla.setNextPosibleAttackStyles(
//                    (List<DemonicGorilla.AttackStyle>)Arrays.<DemonicGorilla.AttackStyle>stream(DemonicGorilla.ALL_REGULAR_ATTACK_STYLES)
//                            .filter(x -> Arrays.<DemonicGorilla.AttackStyle>stream(protectedStyles).noneMatch(()))
//                            .collect(Collectors.toList()));
            gorilla.setNextPosibleAttackStyles(
                    Arrays.stream(DemonicGorilla.ALL_REGULAR_ATTACK_STYLES)
                            .filter(x -> Arrays.stream(protectedStyles).noneMatch(protectedStyle -> protectedStyle == x))
                            .collect(Collectors.toList())
            );


            gorilla.setAttacksUntilSwitch(3);
            gorilla.setChangedAttackStyleThisTick(true);
        }
    }

    private DemonicGorilla.AttackStyle getProtectedStyle(Player player) {
        HeadIcon headIcon = player.getOverheadIcon();
        if (headIcon == null)
            return null;
        switch (headIcon) {
            case DEFLECT_MELEE:
                return DemonicGorilla.AttackStyle.MELEE;
            case DEFLECT_RANGE:
                return DemonicGorilla.AttackStyle.RANGED;
            case DEFLECT_MAGE:
                return DemonicGorilla.AttackStyle.MAGIC;
        }
        return null;
    }

    private void onGorillaAttack(DemonicGorilla gorilla, DemonicGorilla.AttackStyle attackStyle) {
        gorilla.setInitiatedCombat(true);
        Player target = (Player)gorilla.getNpc().getInteracting();
        DemonicGorilla.AttackStyle protectedStyle = null;
        if (target != null)
            protectedStyle = getProtectedStyle(target);
        boolean correctPrayer = (target == null || (attackStyle != null && attackStyle.equals(protectedStyle)));
        if (attackStyle == DemonicGorilla.AttackStyle.BOULDER) {
            gorilla.setNextPosibleAttackStyles((List<DemonicGorilla.AttackStyle>)gorilla
                    .getNextPosibleAttackStyles()
                    .stream()
                    .filter(x -> (x != DemonicGorilla.AttackStyle.MELEE))
                    .collect(Collectors.toList()));
        } else {
            if (correctPrayer) {
                gorilla.setAttacksUntilSwitch(gorilla.getAttacksUntilSwitch() - 1);
            } else {
                int damagesOnTick = this.client.getTickCount();
                if (attackStyle == DemonicGorilla.AttackStyle.MAGIC) {
                    MemorizedPlayer mp = this.memorizedPlayers.get(target);
                    WorldArea lastPlayerArea = mp.getLastWorldArea();
                    if (lastPlayerArea != null) {
                        int dist = gorilla.getNpc().getWorldArea().distanceTo(lastPlayerArea);
                        damagesOnTick += (dist + 12) / 8;
                    }
                } else if (attackStyle == DemonicGorilla.AttackStyle.RANGED) {
                    MemorizedPlayer mp = this.memorizedPlayers.get(target);
                    WorldArea lastPlayerArea = mp.getLastWorldArea();
                    if (lastPlayerArea != null) {
                        int dist = gorilla.getNpc().getWorldArea().distanceTo(lastPlayerArea);
                        damagesOnTick += (dist + 9) / 6;
                    }
                }
                this.pendingAttacks.add(new PendingGorillaAttack(gorilla, attackStyle, target, damagesOnTick));
            }
            gorilla.setNextPosibleAttackStyles((List<DemonicGorilla.AttackStyle>)gorilla
                    .getNextPosibleAttackStyles()
                    .stream()
                    .filter(x -> (x == attackStyle))
                    .collect(Collectors.toList()));
            if (gorilla.getNextPosibleAttackStyles().isEmpty()) {
                gorilla.setNextPosibleAttackStyles(
                        (List<DemonicGorilla.AttackStyle>)Arrays.<DemonicGorilla.AttackStyle>stream(DemonicGorilla.ALL_REGULAR_ATTACK_STYLES)
                                .filter(x -> (x == attackStyle))
                                .collect(Collectors.toList()));
                gorilla.setAttacksUntilSwitch(3 - (
                        correctPrayer ? 1 : 0));
            }
        }
        checkGorillaAttackStyleSwitch(gorilla, new DemonicGorilla.AttackStyle[] { protectedStyle });
        int tickCounter = this.client.getTickCount();
        gorilla.setNextAttackTick(tickCounter + 5);
    }

    private void checkGorillaAttacks() {
        int tickCounter = this.client.getTickCount();
        for (Iterator<DemonicGorilla> iterator = this.gorillas.values().iterator(); iterator.hasNext(); ) {
            DemonicGorilla gorilla = iterator.next();
            Player interacting = (Player)gorilla.getNpc().getInteracting();
            MemorizedPlayer mp = this.memorizedPlayers.get(interacting);
            if (gorilla.getLastTickInteracting() != null && interacting == null) {
                gorilla.setInitiatedCombat(false);
            } else if (mp != null && mp.getLastWorldArea() != null &&
                    !gorilla.isInitiatedCombat() && tickCounter < gorilla
                    .getNextAttackTick() && gorilla
                    .getNpc().getWorldArea().isInMeleeDistance(mp.getLastWorldArea())) {
                gorilla.setInitiatedCombat(true);
                gorilla.setNextAttackTick(tickCounter + 1);
            }
            int animationId = gorilla.getNpc().getAnimation();
            if (gorilla.isTakenDamageRecently() && tickCounter >= gorilla
                    .getNextAttackTick() + 4) {
                gorilla.setNextAttackTick(tickCounter + 2);
                gorilla.setInitiatedCombat(true);
                if (mp != null && mp.getLastWorldArea() != null &&
                        !gorilla.getNpc().getWorldArea().isInMeleeDistance(mp.getLastWorldArea()) &&
                        !gorilla.getNpc().getWorldArea().intersectsWith(mp.getLastWorldArea())) {
                    gorilla.setNextPosibleAttackStyles((List<DemonicGorilla.AttackStyle>)gorilla
                            .getNextPosibleAttackStyles()
                            .stream()
                            .filter(x -> (x != DemonicGorilla.AttackStyle.MELEE))
                            .collect(Collectors.toList()));
                    if (interacting != null)
                        checkGorillaAttackStyleSwitch(gorilla, new DemonicGorilla.AttackStyle[] { DemonicGorilla.AttackStyle.MELEE,
                                getProtectedStyle(interacting) });
                }
            } else if (animationId != gorilla.getLastTickAnimation()) {
                if (animationId == 7226) {
                    onGorillaAttack(gorilla, DemonicGorilla.AttackStyle.MELEE);
                } else if (animationId == 7225) {
                    onGorillaAttack(gorilla, DemonicGorilla.AttackStyle.MAGIC);
                } else if (animationId == 7227) {
                    onGorillaAttack(gorilla, DemonicGorilla.AttackStyle.RANGED);
                } else if (animationId == 7228 && interacting != null && gorilla
                        .getNextPosibleAttackStyles().stream().anyMatch(x -> (x == DemonicGorilla.AttackStyle.MAGIC || x == DemonicGorilla.AttackStyle.RANGED))) {
                    if (gorilla.getOverheadIcon() == gorilla.getLastTickOverheadIcon()) {
                        onGorillaAttack(gorilla, DemonicGorilla.AttackStyle.BOULDER);
                    } else {
                        if (tickCounter >= gorilla.getNextAttackTick()) {
                            gorilla.setChangedPrayerThisTick(true);
                            int projectileId = gorilla.getRecentProjectileId();
                            if (projectileId == 1304) {
                                onGorillaAttack(gorilla, DemonicGorilla.AttackStyle.MAGIC);
                            } else if (projectileId == 1302) {
                                onGorillaAttack(gorilla, DemonicGorilla.AttackStyle.RANGED);
                            } else if (mp != null) {
                                WorldArea lastPlayerArea = mp.getLastWorldArea();
                                if (lastPlayerArea != null && this.recentBoulders.stream()
                                        .anyMatch(x -> (x.distanceTo(lastPlayerArea) == 0))) {
                                    onGorillaAttack(gorilla, DemonicGorilla.AttackStyle.BOULDER);
                                } else if (!mp.getRecentHitsplats().isEmpty()) {
                                    onGorillaAttack(gorilla, DemonicGorilla.AttackStyle.MELEE);
                                }
                            }
                        }
                        gorilla.setNextAttackTick(tickCounter + 5);
                        gorilla.setChangedPrayerThisTick(true);
                    }
                }
            }
            if (gorilla.getDisabledMeleeMovementForTicks() > 0) {
                gorilla.setDisabledMeleeMovementForTicks(gorilla.getDisabledMeleeMovementForTicks() - 1);
            } else if (gorilla.isInitiatedCombat() && gorilla
                    .getNpc().getInteracting() != null &&
                    !gorilla.isChangedAttackStyleThisTick() && gorilla
                    .getNextPosibleAttackStyles().size() >= 2 && gorilla
                    .getNextPosibleAttackStyles().stream()
                    .anyMatch(x -> (x == DemonicGorilla.AttackStyle.MELEE))) {
                if (mp != null && mp.getLastWorldArea() != null && gorilla.getLastWorldArea() != null) {
                    WorldArea predictedNewArea = WorldAreaExtended.calculateNextTravellingPoint(this.client, gorilla
                            .getLastWorldArea(), mp.getLastWorldArea(), true, x -> {
                        WorldArea area1 = new WorldArea(x, 1, 1);
                        //return (this.gorillas.values().stream().noneMatch(()) && this.memorizedPlayers.values().stream().noneMatch(()));
                        return (this.gorillas.values().stream().noneMatch(gorillaArea -> gorillaArea.getLastWorldArea().intersectsWith(area1))
                                && this.memorizedPlayers.values().stream().noneMatch(playerArea -> playerArea.getLastWorldArea().intersectsWith(area1)));
                    });

                    if (predictedNewArea != null) {
                        int distance = gorilla.getNpc().getWorldArea().distanceTo(mp.getLastWorldArea());
                        WorldPoint predictedMovement = predictedNewArea.toWorldPoint();
                        if (distance <= 10 && mp.getLastWorldArea().hasLineOfSightTo(this.client.getTopLevelWorldView(), gorilla.getLastWorldArea()))
                            if (predictedMovement.distanceTo(gorilla.getLastWorldArea().toWorldPoint()) != 0) {
                                if (predictedMovement.distanceTo(gorilla.getNpc().getWorldLocation()) == 0) {
                                    gorilla.setNextPosibleAttackStyles((List<DemonicGorilla.AttackStyle>)gorilla
                                            .getNextPosibleAttackStyles()
                                            .stream()
                                            .filter(x -> (x == DemonicGorilla.AttackStyle.MELEE))
                                            .collect(Collectors.toList()));
                                } else {
                                    gorilla.setNextPosibleAttackStyles((List<DemonicGorilla.AttackStyle>)gorilla
                                            .getNextPosibleAttackStyles()
                                            .stream()
                                            .filter(x -> (x != DemonicGorilla.AttackStyle.MELEE))
                                            .collect(Collectors.toList()));
                                }
                            } else if (tickCounter >= gorilla.getNextAttackTick() && gorilla
                                    .getRecentProjectileId() == -1 && this.recentBoulders
                                    .stream().noneMatch(x -> (x.distanceTo(mp.getLastWorldArea()) == 0))) {
                                gorilla.setNextPosibleAttackStyles((List<DemonicGorilla.AttackStyle>)gorilla
                                        .getNextPosibleAttackStyles()
                                        .stream()
                                        .filter(x -> (x == DemonicGorilla.AttackStyle.MELEE))
                                        .collect(Collectors.toList()));
                            }
                    }
                }
            }
            if (gorilla.isTakenDamageRecently())
                gorilla.setInitiatedCombat(true);
            if (gorilla.getOverheadIcon() != gorilla.getLastTickOverheadIcon())
                if (gorilla.isChangedAttackStyleLastTick() || gorilla
                        .isChangedAttackStyleThisTick()) {
                    gorilla.setDisabledMeleeMovementForTicks(2);
                } else {
                    gorilla.setDisabledMeleeMovementForTicks(1);
                }
            gorilla.setLastTickAnimation(gorilla.getNpc().getAnimation());
            gorilla.setLastWorldArea(gorilla.getNpc().getWorldArea());
            gorilla.setLastTickInteracting(gorilla.getNpc().getInteracting());
            gorilla.setTakenDamageRecently(false);
            gorilla.setChangedPrayerThisTick(false);
            gorilla.setChangedAttackStyleLastTick(gorilla.isChangedAttackStyleThisTick());
            gorilla.setChangedAttackStyleThisTick(false);
            gorilla.setLastTickOverheadIcon(gorilla.getOverheadIcon());
            gorilla.setRecentProjectileId(-1);
        }
    }

    @Subscribe
    private void onProjectileMoved(ProjectileMoved event) {
        if (!this.atGorillas)
            return;
        Projectile projectile = event.getProjectile();
        int projectileId = projectile.getId();
        if (!DEMONIC_PROJECTILES.contains(Integer.valueOf(projectileId)))
            return;
        if (this.gorillaProjectiles.contains(projectile))
            return;
        this.gorillaProjectiles.add(projectile);
        WorldPoint loc = WorldPoint.fromLocal(this.client.getTopLevelWorldView(), projectile.getX1(), projectile.getY1(), this.client.getTopLevelWorldView().getPlane());
        if (projectileId == 856) {
            this.recentBoulders.add(loc);
        } else {
            for (DemonicGorilla gorilla : this.gorillas.values()) {
                if (gorilla.getNpc().getWorldLocation().distanceTo(loc) == 0)
                    gorilla.setRecentProjectileId(projectile.getId());
            }
        }
    }

    private void checkPendingAttacks() {
        Iterator<PendingGorillaAttack> it = this.pendingAttacks.iterator();
        int tickCounter = this.client.getTickCount();
        while (it.hasNext()) {
            PendingGorillaAttack attack = it.next();
            if (tickCounter >= attack.getFinishesOnTick()) {
                boolean shouldDecreaseCounter = false;
                DemonicGorilla gorilla = attack.getAttacker();
                MemorizedPlayer target = this.memorizedPlayers.get(attack.getTarget());
                if (target == null) {
                    shouldDecreaseCounter = true;
                } else if (target.getRecentHitsplats().isEmpty()) {
                    shouldDecreaseCounter = true;
                } else if (target.getRecentHitsplats().stream()
                        .anyMatch(x -> (x.getHitsplatType() == 12))) {
                    shouldDecreaseCounter = true;
                }
                if (shouldDecreaseCounter) {
                    gorilla.setAttacksUntilSwitch(gorilla.getAttacksUntilSwitch() - 1);
                    checkGorillaAttackStyleSwitch(gorilla, new DemonicGorilla.AttackStyle[0]);
                }
                it.remove();
            }
        }
    }

    private void updatePlayers() {
        for (MemorizedPlayer mp : this.memorizedPlayers.values()) {
            mp.setLastWorldArea(mp.getPlayer().getWorldArea());
            mp.getRecentHitsplats().clear();
        }
    }

    @Subscribe
    private void onHitsplatApplied(HitsplatApplied event) {
        if (!this.atGorillas || this.gorillas.isEmpty())
            return;
        if (event.getActor() instanceof Player) {
            Player player = (Player)event.getActor();
            MemorizedPlayer mp = this.memorizedPlayers.get(player);
            if (mp != null)
                mp.getRecentHitsplats().add(event.getHitsplat());
        } else if (event.getActor() instanceof NPC) {
            DemonicGorilla gorilla = this.gorillas.get(event.getActor());
            int hitsplatType = event.getHitsplat().getHitsplatType();
            if (gorilla != null && (hitsplatType == 12 || hitsplatType == 16))
                gorilla.setTakenDamageRecently(true);
        }
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        GameState gs = event.getGameState();
        switch (gs) {
            case LOGGED_IN:
                if (atDemonicGorillas()) {
                    if (!this.atGorillas)
                        init();
                    break;
                }
                if (this.atGorillas)
                    shutDown();
                break;
            case HOPPING:
            case LOGGING_IN:
            case CONNECTION_LOST:
            case LOGIN_SCREEN:
                if (this.atGorillas)
                    shutDown();
                break;
        }
    }

    @Subscribe
    private void onPlayerSpawned(PlayerSpawned event) {
        if (!this.atGorillas || this.gorillas.isEmpty())
            return;
        Player player = event.getPlayer();
        this.memorizedPlayers.put(player, new MemorizedPlayer(player));
    }

    @Subscribe
    private void onPlayerDespawned(PlayerDespawned event) {
        if (!this.atGorillas || this.gorillas.isEmpty())
            return;
        this.memorizedPlayers.remove(event.getPlayer());
    }

    @Subscribe
    private void onNpcSpawned(NpcSpawned event) {
        if (!this.atGorillas)
            return;
        NPC npc = event.getNpc();
        if (isNpcGorilla(npc.getId())) {
            if (this.gorillas.isEmpty())
                resetPlayers();
            this.gorillas.put(npc, new DemonicGorilla(npc, this.client));
        }
    }

    @Subscribe
    private void onNpcDespawned(NpcDespawned event) {
        if (!this.atGorillas)
            return;
        if (this.gorillas.remove(event.getNpc()) != null && this.gorillas.isEmpty())
            clear();
    }

    @Subscribe
    private void onGameTick(GameTick event) {
        if (!this.atGorillas)
            return;
        checkGorillaAttacks();
        checkPendingAttacks();
        updatePlayers();
        this.recentBoulders.clear();
        clearProjectileArray();
    }

    private void clearProjectileArray() {
        this.gorillaProjectiles.removeIf(p -> (p.getRemainingCycles() <= 0));
    }

    private boolean atDemonicGorillas() {
        return REGION_IDS.contains(client.getLocalPlayer().getWorldLocation().getRegionID());
    }
}
