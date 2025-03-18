package com.example.FishingFriend;


import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.*;

public class FishingFriendOverlay extends OverlayPanel {

    private final Client client;
    private final FishingFriendPlugin plugin;

    @Inject
    private FishingFriendOverlay(Client client, FishingFriendPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);

    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.setPreferredSize(new Dimension(200, 320));
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Fishing Friend")
                .color(Color.CYAN)
                .build());
        panelComponent.getChildren().add(TitleComponent.builder()
                .text(plugin.started ? "Running" : "Paused")
                .color(plugin.started ? Color.GREEN : Color.RED)
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("State: ")
                .leftColor(Color.CYAN)
                .right(plugin.state==null || !plugin.started ? "STOPPED" : plugin.state.name())
                .rightColor(Color.WHITE)
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Elapsed Time: ")
                .leftColor(Color.CYAN)
                .right(plugin.getElapsedTime())
                .rightColor(Color.WHITE)
                .build());

        return super.render(graphics);
    }
}