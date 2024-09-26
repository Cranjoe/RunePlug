package com.example.toaextended.module;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.util.RaidState;

public interface PluginLifecycleComponent
{

	default boolean isEnabled(ToaExtendedConfig config, RaidState raidState)
	{
		return true;
	}

	void startUp();

	void shutDown();

}
