package com.example.toaextended.features.tomb;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.module.PluginLifecycleComponent;
import com.example.toaextended.util.InventoryUtil;
import com.example.toaextended.util.RaidRoom;
import com.example.toaextended.util.RaidState;
import com.example.toaextended.util.RaidStateTracker;
import com.google.common.collect.ImmutableSet;
import lombok.RequiredArgsConstructor;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CursedPhalanxDetector implements PluginLifecycleComponent
{
	private static final Set<Integer> CURSED_PHALANX_ITEM_IDS = ImmutableSet.of(
		ItemID.CURSED_PHALANX,
		ItemID.OSMUMTENS_FANG_OR
	);

	private boolean isEligibleForKit = true;

	private final EventBus eventBus;
	private final Client client;
	private final RaidStateTracker raidStateTracker;

	@Override
	public boolean isEnabled(final ToaExtendedConfig config, final RaidState raidState)
	{
		return raidState.isInRaid() &&
			config.cursedPhalanxDetect();
	}

	@Override
	public void startUp()
	{
		isEligibleForKit = true;
		eventBus.register(this);
	}

	@Override
	public void shutDown()
	{
		eventBus.unregister(this);
	}

	@Subscribe
	private void onChatMessage(ChatMessage e)
	{
		if (e.getType() != ChatMessageType.GAMEMESSAGE || !isEligibleForKit)
		{
			return;
		}

		if (e.getMessage().contains("Total deaths"))
		{
			isEligibleForKit = false;
		}
	}

	@Subscribe
	private void onMenuOptionClicked(final MenuOptionClicked event)
	{
		if (!isEligibleForKit ||
			raidStateTracker.getCurrentState().getCurrentRoom() != RaidRoom.TOMB ||
			client.getVarbitValue(Varbits.TOA_RAID_LEVEL) < 500)
		{
			return;
		}

		final MenuEntry menuEntry = event.getMenuEntry();
		if (!menuEntry.getOption().equals("Open"))
		{
			return;
		}

		boolean wearingPhalanx = InventoryUtil.containsAny(client.getItemContainer(InventoryID.EQUIPMENT), CURSED_PHALANX_ITEM_IDS);
		boolean carryingPhalanx = InventoryUtil.containsAny(client.getItemContainer(InventoryID.INVENTORY), CURSED_PHALANX_ITEM_IDS);

		if (wearingPhalanx || carryingPhalanx)
		{
			event.consume();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Remove and/or drop cursed phalanx before doing that.", null);
		}
	}
}
