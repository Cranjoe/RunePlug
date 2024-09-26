package com.example.toaextended.features.scabaras.panel;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.features.scabaras.ScabarasHelperMode;
import com.example.toaextended.module.PluginLifecycleComponent;
import com.example.toaextended.util.RaidRoom;
import com.example.toaextended.util.RaidState;
import lombok.RequiredArgsConstructor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.image.BufferedImage;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ScabarasPanelManager implements PluginLifecycleComponent
{

	private static final BufferedImage PANEL_ICON = ImageUtil.loadImageResource(ScabarasPanelManager.class, "icon.png");

	private final ClientToolbar clientToolbar;
	private final ScabarasPanel scabarasPanel;

	private NavigationButton navButton;

	@Override
	public boolean isEnabled(ToaExtendedConfig config, RaidState currentState)
	{
		return config.scabarasHelperMode() == ScabarasHelperMode.SIDE_PANEL &&
			currentState.getCurrentRoom() == RaidRoom.SCABARAS;
	}

	@Override
	public void startUp()
	{
		if (navButton == null)
		{
			navButton = NavigationButton.builder()
				.icon(PANEL_ICON)
				.panel(scabarasPanel)
				.priority(999)
				.tooltip("Scabaras Tile Puzzle Helper")
				.build();
		}

		openPanel();
	}

	@Override
	public void shutDown()
	{
		removePanel();
	}

	private void openPanel()
	{
		clientToolbar.addNavigation(navButton);
		SwingUtilities.invokeLater(() -> clientToolbar.openPanel(navButton));
	}

	private void removePanel()
	{
		clientToolbar.removeNavigation(navButton);
	}
}
