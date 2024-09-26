package com.example.toaextended.features.pointstracker;

import com.example.toaextended.ToaExtendedConfig;
import com.example.toaextended.module.PluginLifecycleComponent;
import com.example.toaextended.util.RaidState;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class PointsOverlay extends OverlayPanel implements PluginLifecycleComponent
{

	private final OverlayManager overlayManager;
	private final PointsTracker pointsTracker;
	private final PartyPointsTracker partyPointsTracker;
	private final ToaExtendedConfig config;

	@Inject
	public PointsOverlay(OverlayManager overlayManager, ToaExtendedConfig config, PointsTracker pointsTracker, PartyPointsTracker partyPointsTracker)
	{
		this.overlayManager = overlayManager;
		this.config = config;
		this.pointsTracker = pointsTracker;
		this.partyPointsTracker = partyPointsTracker;
		setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
	}

	@Override
	public boolean isEnabled(ToaExtendedConfig config, RaidState raidState)
	{
		return config.pointsTrackerOverlayEnable() &&
			raidState.isInRaid();
	}

	@Override
	public void startUp()
	{
		this.overlayManager.add(this);
	}

	@Override
	public void shutDown()
	{
		this.overlayManager.remove(this);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().add(
			TitleComponent.builder()
				.text("ToA Points")
				.build()
		);

		addPointsLine("Total:", pointsTracker.getTotalPoints());
		if (partyPointsTracker.isInParty())
		{
			addPointsLine("Personal:", pointsTracker.getPersonalTotalPoints());
		}

		if (config.pointsTrackerShowRoomPoints())
		{
			addPointsLine("Room:", pointsTracker.getPersonalRoomPoints());
		}

		if (config.pointsTrackerShowUniqueChance())
		{
			addChanceLine("Unique:", pointsTracker.getUniqueChance());
		}

		if (config.pointsTrackerShowPetChance())
		{
			addChanceLine("Pet:", pointsTracker.getPetChance());
		}

		return super.render(graphics);
	}

	private void addLine(String left, String right)
	{
		panelComponent.getChildren().add(
			LineComponent.builder()
				.left(left)
				.right(right)
				.build()
		);
	}

	private void addPointsLine(String title, int value)
	{
		addLine(title, PointsTracker.POINTS_FORMAT.format(value));
	}

	private void addChanceLine(String title, double value)
	{
		addLine(title, PointsTracker.PERCENT_FORMAT.format(value / 100));
	}
}
