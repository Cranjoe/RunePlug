/*
 * Copyright (c) 2019, Ganom <https://github.com/Ganom>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.example.gwdhelper;

import com.google.inject.Provides;
import com.example.KotoriUtils.methods.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import com.example.EthanApiPlugin.EthanApiPlugin;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@PluginDescriptor(
	name = "<html><font color=#6b8af6>[K]</font> God Wars Helper</html>",
	enabledByDefault = false,
	description = "Overlay and automated actions for the original God Wars Dungeon bosses.",
	tags = {"pvm", "bossing", "kotori", "ported", "gwd", "sara", "zammy", "arma", "bandos"}
)
public class GodWarsHelperPlugin extends Plugin
{
	public static final int GENERAL_REGION = 11347;
	public static final int ARMA_REGION = 11346;
	public static final int SARA_REGION = 11602;
	public static final int ZAMMY_REGION = 11603;
	public static final Set<Integer> GWD_REGION_IDS = Set.of(GENERAL_REGION,ARMA_REGION,SARA_REGION,ZAMMY_REGION);
	public static final int SERGEANT_STRONGSTACK_AUTO = 6154;
	public static final int SERGEANT_STEELWILL_AUTO = 7071;
	public static final int SERGEANT_GRIMSPIKE_AUTO = 7073;
	public static final int GENERAL_AUTO1 = 7018;
	public static final int GENERAL_AUTO2 = 7019;
	public static final int GENERAL_AUTO3 = 7021;
	public static final int ZAMMY_GENERIC_AUTO_1 = 64;
	public static final int ZAMMY_GENERIC_AUTO_2 = 65;
	public static final int KRIL_AUTO = 6947;
	public static final int KRIL_AUTO_2 = 6948;
	public static final int KRIL_SPEC = 6950;
	public static final int ZAKL_AUTO = 7077;
	public static final int BALFRUG_AUTO = 4630;
	public static final int ZILYANA_MELEE_AUTO = 6964;
	public static final int ZILYANA_AUTO = 6967;
	public static final int ZILYANA_AUTO_2 = 6969;
	public static final int ZILYANA_SPEC = 6970;
	public static final int STARLIGHT_AUTO = 6375;
	public static final int STARLIGHT_AUTO_2 = 6376;
	public static final int BREE_AUTO = 7026;
	public static final int GROWLER_AUTO = 7035;
	public static final int GROWLER_AUTO_2 = 7037;
	public static final int KREE_RANGED = 6978;
	public static final int KREE_RANGED_2 = 6980;
	public static final int SKREE_AUTO = 6955;
	public static final int GEERIN_AUTO = 6956;
	public static final int GEERIN_FLINCH = 6958;
	public static final int KILISA_AUTO = 6957;
	public static final int GENERAL_GRAARDOR_DEATH_ID = 7020;
	public static final int BANDOS_BODYGUARDS_DEATH_ID = 6156;
	public static final int KRIL_TSUTSAROTH_DEATH_ID = 6949;
	public static final int TSTANON_KARLAK_DEATH_ID = 68;
	public static final int ZAMORAK_BODYGUARDS_DEATH_ID = 67;
	public static final int COMMANDER_ZILYANA_DEATH_ID = 6968;
	public static final int BREE_DEATH_ID = 7028;
	public static final int GROWLER_DEATH_ID = 7034;
	public static final int STARLIGHT_DEATH_ID = 6377;
	public static final int KREE_ARRA_DEATH_ID = 6979;
	public static final int ARMADYL_BODYGUARDS_DEATH_ID = 6959;
	public static final WorldArea BANDOS_BOSS_ROOM = new WorldArea(2863,5350,15,24,2);
	public static final WorldArea ZAMMY_BOSS_ROOM = new WorldArea(2916,5317,25,16,2);
	public static final WorldArea SARA_BOSS_ROOM = new WorldArea(2884,5257,25,20,0);
	public static final WorldArea ARMA_BOSS_ROOM = new WorldArea(2820,5295,24,15,2);

	@Inject
	private Client client;
	
	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TimersOverlay timersOverlay;
	
	@Inject
	private KeyManager keyManager;

	@Inject
	private GodWarsHelperConfig config;

	@Getter(AccessLevel.PACKAGE)
	private Set<NPCContainer> npcContainers = new HashSet<>();
	private boolean validRegion;
	private int currentRegion;
	private int lastRegion;
	private boolean inBossRoom;
	private boolean bossAlive;
	private boolean meleeMinionAlive;
	private boolean magicMinionAlive;
	private boolean rangedMinionAlive;
	private boolean allPrayersDeactivated;
	private boolean set1EquippedOnce;
	private boolean set2EquippedOnce;
	private boolean set3EquippedOnce;
	private boolean set4EquippedOnce;
	private boolean set5EquippedOnce;
	private boolean isBandosPrayerHotkeyOn;
	private boolean isZammyPrayerHotkeyOn;
	private boolean isSaraPrayerHotkeyOn;
	private boolean isArmaPrayerHotkeyOn;
	private boolean isSpellHotkey1Pressed;
	private boolean isSpellHotkey2Pressed;
	private boolean isGraardorGearHotkeyPressed;
	private boolean isSteelwillGearHotkeyPressed;
	private boolean isGrimspikeGearHotkeyPressed;
	private boolean isStrongstackGearHotkeyPressed;
	private boolean isBandosGearHotkeyPressed;
	private boolean isKrilGearHotkeyPressed;
	private boolean isBalfrugGearHotkeyPressed;
	private boolean isZalknGearHotkeyPressed;
	private boolean isTstanonGearHotkeyPressed;
	private boolean isZamorakGearHotkeyPressed;
	private boolean isZilyanaGearHotkeyPressed;
	private boolean isGrowlerGearHotkeyPressed;
	private boolean isBreeGearHotkeyPressed;
	private boolean isStarlightGearHotkeyPressed;
	private boolean isSaradominGearHotkeyPressed;
	private boolean isKreearraGearHotkeyPressed;
	private boolean isWingmanGearHotkeyPressed;
	private boolean isFlockleaderGearHotkeyPressed;
	private boolean isFlightGearHotkeyPressed;
	private boolean isArmadylGearHotkeyPressed;

	@Getter(AccessLevel.PACKAGE)
	private long lastTickTime;

	@Provides
	GodWarsHelperConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(GodWarsHelperConfig.class);
	}

	@Override
	public void startUp()
	{
		if (client.getGameState() != GameState.LOGGED_IN || !regionCheck())
		{
			return;
		}
		init();
	}
	
	private void init()
	{
		npcContainers.clear();
		for (NPC npc : client.getNpcs())
		{
			addNpc(npc);
		}
		validRegion = true;
		overlayManager.add(timersOverlay);
	}

	@Override
	public void shutDown()
	{
		npcContainers.clear();
		overlayManager.remove(timersOverlay);
		resetPrayerHotkeyBooleans();
		validRegion = false;
		inBossRoom = false;
		bossAlive = false;
		meleeMinionAlive = false;
		magicMinionAlive = false;
		rangedMinionAlive = false;
		set1EquippedOnce = false;
		set2EquippedOnce = false;
		set3EquippedOnce = false;
		set4EquippedOnce = false;
		set5EquippedOnce = false;
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		GameState gameState = event.getGameState();

		switch(gameState)
		{
			case LOGGED_IN:
				if (regionCheck())
				{
					if (!validRegion)
					{
						init();
					}
				}
				else
				{
					if (validRegion)
					{
						shutDown();
					}
				}
				break;
			case LOGIN_SCREEN:
			case HOPPING:
				if (validRegion)
				{
					shutDown();
				}
				break;
			default:
				break;
		}
	}

	@Subscribe
	private void onNpcSpawned(NpcSpawned event)
	{
		if (!validRegion)
		{
			return;
		}

		addNpc(event.getNpc());
	}

	@Subscribe
	private void onNpcDespawned(NpcDespawned event)
	{
		if (!validRegion)
		{
			return;
		}

		removeNpc(event.getNpc());
	}
	
	@Subscribe
	private void onAnimationChanged(AnimationChanged event)
	{
		//Used to check for NPC death animations because its faster than waiting for the NPC to despawn.
		if (!validRegion)
		{
			return;
		}
		
		Actor actor = event.getActor();
		
		if (actor == null)
		{
			return;
		}
		
		if (actor instanceof NPC)
		{
			NPC npc = (NPC) actor;
			int npcAnimation = npc.getAnimation();
			
			switch(npc.getId())
			{
				case NpcID.GENERAL_GRAARDOR:
				case NpcID.KRIL_TSUTSAROTH:
				case NpcID.COMMANDER_ZILYANA:
				case NpcID.KREEARRA:
					if (npcAnimation == GENERAL_GRAARDOR_DEATH_ID || npcAnimation == KRIL_TSUTSAROTH_DEATH_ID || npcAnimation == COMMANDER_ZILYANA_DEATH_ID ||
							npcAnimation == KREE_ARRA_DEATH_ID)
					{
						bossAlive = false;
						set5EquippedOnce = false;
					}
					break;
				case NpcID.SERGEANT_STRONGSTACK:
				case NpcID.TSTANON_KARLAK:
				case NpcID.STARLIGHT:
				case NpcID.FLIGHT_KILISA:
					if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID || npcAnimation == TSTANON_KARLAK_DEATH_ID || npcAnimation == STARLIGHT_DEATH_ID ||
							npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
					{
						meleeMinionAlive = false;
					}
					break;
				case NpcID.SERGEANT_STEELWILL:
				case NpcID.BALFRUG_KREEYATH:
				case NpcID.GROWLER:
				case NpcID.WINGMAN_SKREE:
					if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID || npcAnimation == ZAMORAK_BODYGUARDS_DEATH_ID || npcAnimation == GROWLER_DEATH_ID ||
							npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
					{
						magicMinionAlive = false;
					}
					break;
				case NpcID.SERGEANT_GRIMSPIKE:
				case NpcID.ZAKLN_GRITCH:
				case NpcID.BREE:
				case NpcID.FLOCKLEADER_GEERIN:
					if (npcAnimation == BANDOS_BODYGUARDS_DEATH_ID || npcAnimation == ZAMORAK_BODYGUARDS_DEATH_ID || npcAnimation == BREE_DEATH_ID ||
							npcAnimation == ARMADYL_BODYGUARDS_DEATH_ID)
					{
						rangedMinionAlive = false;
					}
					break;
				default:
					break;
			}
		}
	}

	@Subscribe
	public void onGameTick(GameTick Event)
	{
		if (!validRegion)
		{
			return;
		}
		
		lastTickTime = System.currentTimeMillis();
		handleBosses();
		bossRoomCheck();
	}
	
	@Subscribe
	public void onClientTick(ClientTick event)
	{
		if (!validRegion || client.getGameState() != GameState.LOGGED_IN || client.getLocalPlayer() == null)
		{
			return;
		}

	}
	
//	@Subscribe
//	public void onMenuOptionClicked(MenuOptionClicked event)
//	{
//		if (!validRegion)
//		{
//			return;
//		}
//		String spell1Option = "<col=39ff14>Cast " + config.spellChoice1().getSpellString() + "</col> -> ";
//		String spell2Option = "<col=39ff14>Cast " + config.spellChoice2().getSpellString() + "</col> -> ";
//		String entryOption = event.getMenuOption();
//
//		if (entryOption.equals(spell1Option))
//		{
//			if (!VarUtilities.isSpellInActiveSpellbook(config.spellChoice1().getSpell().getSpell()) || event.getMenuTarget().equals(" "))
//			{
//				event.consume();
//			}
//		}
//		else if (entryOption.equals(spell2Option))
//		{
//			if (!VarUtilities.isSpellInActiveSpellbook(config.spellChoice2().getSpell().getSpell()) || event.getMenuTarget().equals(" "))
//			{
//				event.consume();
//			}
//		}
//	}

	private void handleBosses()
	{
		for (NPCContainer npc : getNpcContainers())
		{
			npc.setNpcInteracting(npc.getNpc().getInteracting());

			if (npc.getTicksUntilAttack() >= 0)
			{
				npc.setTicksUntilAttack(npc.getTicksUntilAttack() - 1);
			}

			for (int animation : npc.getAnimations())
			{
				if (animation == npc.getNpc().getAnimation() && npc.getTicksUntilAttack() < 1)
				{
					npc.setTicksUntilAttack(npc.getAttackSpeed());
				}
			}
		}
	}

	private boolean regionCheck()
	{
		lastRegion = currentRegion;
		currentRegion = MiscUtilities.getPlayerRegionID();
		return GWD_REGION_IDS.contains(currentRegion);
	}
	
	private void bossRoomCheck()
	{
		if (!validRegion)
		{
			if (inBossRoom)
			{
				inBossRoom = false;
			}
			return;
		}
		
		if (inBossRoom)
		{
			return;
		}
		
		WorldPoint point = WorldPoint.fromLocalInstance(client, client.getLocalPlayer().getLocalLocation());
		currentRegion = point.getRegionID();
		
		switch (currentRegion)
		{
			case GENERAL_REGION:
				if (BANDOS_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
									}
				break;
			case ZAMMY_REGION:
				if (ZAMMY_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
									}
				break;
			case SARA_REGION:
				if (SARA_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
									}
				break;
			case ARMA_REGION:
				if (ARMA_BOSS_ROOM.contains(point))
				{
					inBossRoom = true;
									}
				break;
			default:
				inBossRoom = false;
				break;
		}
	}
	
	private void resetPrayerHotkeyBooleans()
	{
		if (isBandosPrayerHotkeyOn)
		{
			isBandosPrayerHotkeyOn = false;
			MiscUtilities.sendGameMessage("Bandos God Wars Dungeon automatic protection prayers turned off.");
		}
		
		if (isZammyPrayerHotkeyOn)
		{
			isZammyPrayerHotkeyOn = false;
			MiscUtilities.sendGameMessage("Zamorak God Wars Dungeon automatic protection prayers turned off.");
		}
		if (isSaraPrayerHotkeyOn)
		{
			isSaraPrayerHotkeyOn = false;
			MiscUtilities.sendGameMessage("Saradomin God Wars Dungeon automatic protection prayers turned off.");
		}
		if (isArmaPrayerHotkeyOn)
		{
			isArmaPrayerHotkeyOn = false;
			MiscUtilities.sendGameMessage("Armadyl God Wars Dungeon automatic protection prayers turned off.");
		}
	}
	
	private void addNpc(NPC npc)
	{
		if (npc == null)
		{
			return;
		}

		switch (npc.getId())
		{
			case NpcID.GENERAL_GRAARDOR:
			case NpcID.KRIL_TSUTSAROTH:
			case NpcID.COMMANDER_ZILYANA:
			case NpcID.KREEARRA:
				bossAlive = true;
				set1EquippedOnce = false;
				set2EquippedOnce = false;
				set3EquippedOnce = false;
				set4EquippedOnce = false;
				set5EquippedOnce = false;
				allPrayersDeactivated = false;
				npcContainers.add(new NPCContainer(npc));
				break;
			case NpcID.SERGEANT_STEELWILL:
			case NpcID.BALFRUG_KREEYATH:
			case NpcID.GROWLER:
			case NpcID.WINGMAN_SKREE:
				magicMinionAlive = true;
				npcContainers.add(new NPCContainer(npc));
				break;
			case NpcID.SERGEANT_STRONGSTACK:
			case NpcID.TSTANON_KARLAK:
			case NpcID.STARLIGHT:
			case NpcID.FLIGHT_KILISA:
				meleeMinionAlive = true;
				npcContainers.add(new NPCContainer(npc));
				break;
			case NpcID.SERGEANT_GRIMSPIKE:
			case NpcID.ZAKLN_GRITCH:
			case NpcID.BREE:
			case NpcID.FLOCKLEADER_GEERIN:
				rangedMinionAlive = true;
				npcContainers.add(new NPCContainer(npc));
				break;
			default:
				break;
		}
	}

	private void removeNpc(NPC npc)
	{
		if (npc == null)
		{
			return;
		}

		switch (npc.getId())
		{
			case NpcID.GENERAL_GRAARDOR:
			case NpcID.KRIL_TSUTSAROTH:
			case NpcID.COMMANDER_ZILYANA:
			case NpcID.KREEARRA:
			case NpcID.SERGEANT_STRONGSTACK:
			case NpcID.SERGEANT_STEELWILL:
			case NpcID.SERGEANT_GRIMSPIKE:
			case NpcID.TSTANON_KARLAK:
			case NpcID.BALFRUG_KREEYATH:
			case NpcID.ZAKLN_GRITCH:
			case NpcID.STARLIGHT:
			case NpcID.BREE:
			case NpcID.GROWLER:
			case NpcID.FLIGHT_KILISA:
			case NpcID.FLOCKLEADER_GEERIN:
			case NpcID.WINGMAN_SKREE:
				npcContainers.removeIf(c -> c.getNpc() == npc);
				break;
			default:
				break;
		}
	}

	
	private NPCContainer identifyNpcToPrayAgainst(int bossPriorityConfig, int magicMinionPriorityConfig, int rangedMinionPriorityConfig, int meleeMinionPriorityConfig)
	{
		NPCContainer npcAboutToAttack = null;
		int highestPriorityConfig = -1;
		NPCContainer bossContainer = null;
		
		for (NPCContainer npc : npcContainers)
		{
			switch (npc.getMonsterType())
			{
				case GENERAL_GRAARDOR:
				case KRIL_TSUTSAROTH:
				case COMMANDER_ZILYANA:
				case KREEARRA:
					bossContainer = npc;
					if (npc.getTicksUntilAttack() == 1)
					{
						if (bossPriorityConfig >= highestPriorityConfig)
						{
							highestPriorityConfig = bossPriorityConfig;
							npcAboutToAttack = npc;
						}
					}
					break;
				case SERGEANT_STEELWILL:
				case BALFRUG_KREEYATH:
				case GROWLER:
				case WINGMAN_SKREE:
					if (npc.getTicksUntilAttack() == 1)
					{
						if (magicMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = magicMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
					}
					break;
				case SERGEANT_GRIMSPIKE:
				case ZAKLN_GRITCH:
				case BREE:
				case FLOCKLEADER_GEERIN:
					if (npc.getTicksUntilAttack() == 1)
					{
						if (rangedMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = rangedMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
					}
					break;
				case SERGEANT_STRONGSTACK:
				case TSTANON_KARLAK:
				case STARLIGHT:
				case FLIGHT_KILISA:
					if (npc.getTicksUntilAttack() == 1)
					{
						if (meleeMinionPriorityConfig > highestPriorityConfig)
						{
							if (npc.getNpcInteracting() == client.getLocalPlayer())
							{
								highestPriorityConfig = meleeMinionPriorityConfig;
								npcAboutToAttack = npc;
							}
						}
					}
					break;
				default:
					break;
			}
		}
		
		if (npcAboutToAttack == null)
		{
			npcAboutToAttack = bossContainer;
		}
		
		return npcAboutToAttack;
	}









}
