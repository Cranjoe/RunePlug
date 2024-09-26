package com.example.toaextended.features;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.module.PluginLifecycleComponent;
import com.example.toaextended.util.RaidRoom;
import com.example.toaextended.util.RaidState;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Client;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CameraShakeDisabler implements PluginLifecycleComponent
{

	private final Client client;

	private boolean wasDisabled;

	@Override
	public boolean isEnabled(ToaExtendedConfig config, RaidState raidState)
	{
		return config.disableCameraShake() && raidState.getCurrentRoom() == RaidRoom.WARDEN_P3;
	}

	@Override
	public void startUp()
	{
		wasDisabled = client.isCameraShakeDisabled();
		client.setCameraShakeDisabled(true);
	}

	@Override
	public void shutDown()
	{
		if (wasDisabled)
		{
			client.setCameraShakeDisabled(false);
		}
	}
}
