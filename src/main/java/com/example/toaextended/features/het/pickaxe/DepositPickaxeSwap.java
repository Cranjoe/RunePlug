package com.example.toaextended.features.het.pickaxe;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.module.PluginLifecycleComponent;
import com.example.toaextended.util.RaidRoom;
import com.example.toaextended.util.RaidState;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class DepositPickaxeSwap implements PluginLifecycleComponent
{

	private final EventBus eventBus;
	private final Client client;

	@Override
	public boolean isEnabled(ToaExtendedConfig config, RaidState currentState)
	{
		return (currentState.getCurrentRoom() == RaidRoom.HET || currentState.isInLobby()) &&
			config.hetPickaxeMenuSwap();
	}

	@Override
	public void startUp()
	{
		eventBus.register(this);
	}

	@Override
	public void shutDown()
	{
		eventBus.unregister(this);
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded e)
	{
		if (isTakePickaxe(e.getMenuEntry()) && PickaxeUtil.hasPickaxe(client))
		{
			e.getMenuEntry().setDeprioritized(true);
		}
	}

	private static boolean isTakePickaxe(MenuEntry menuEntry)
	{
		return menuEntry.getOption().equals("Take-pickaxe") &&
			menuEntry.getTarget().contains("Statue");
	}
}
