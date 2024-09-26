package com.example.toaextended.features;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.module.PluginLifecycleComponent;
import com.example.toaextended.util.RaidRoom;
import com.example.toaextended.util.RaidState;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LeftClickBankAll implements PluginLifecycleComponent
{

	private static final String MENU_ENTRY_OPTION = "Bank-all";

	@Inject
	private EventBus eventBus;

	@Override
	public boolean isEnabled(ToaExtendedConfig config, RaidState raidState)
	{
		return config.leftClickBankAll() &&
			(raidState.isInLobby() || raidState.getCurrentRoom() == RaidRoom.TOMB);
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
		if (MENU_ENTRY_OPTION.equals(e.getOption()))
		{
			e.getMenuEntry().setForceLeftClick(true);
		}
	}
}
