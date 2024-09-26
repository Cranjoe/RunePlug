package com.example.toaextended.features.scabaras.overlay;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.features.scabaras.ScabarasHelperMode;
import com.example.toaextended.module.PluginLifecycleComponent;
import com.example.toaextended.util.RaidRoom;
import com.example.toaextended.util.RaidState;
import lombok.RequiredArgsConstructor;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ScabarasOverlayManager implements PluginLifecycleComponent
{

	private final EventBus eventBus;
	private final OverlayManager overlayManager;
	private final ScabarasOverlay scabarasOverlay;

	@Override
	public boolean isEnabled(ToaExtendedConfig config, RaidState currentState)
	{
		return config.scabarasHelperMode() == ScabarasHelperMode.OVERLAY &&
			currentState.getCurrentRoom() == RaidRoom.SCABARAS;
	}

	@Override
	public void startUp()
	{
		eventBus.register(this);
		installOverlay();
	}

	@Override
	public void shutDown()
	{
		eventBus.unregister(this);
		removeOverlay();
	}

	private void installOverlay()
	{
		overlayManager.add(scabarasOverlay);
	}

	private void removeOverlay()
	{
		overlayManager.remove(scabarasOverlay);
	}
}
