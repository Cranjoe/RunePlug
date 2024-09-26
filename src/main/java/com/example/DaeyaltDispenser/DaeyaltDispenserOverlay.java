package com.example.DaeyaltDispenser;


import com.example.EthanApiPlugin.Collections.TileObjects;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Optional;

public class DaeyaltDispenserOverlay extends OverlayPanel {

    private final Client client;
    private final DaeyaltDispenserPlugin plugin;

    @Inject
    private DaeyaltDispenserOverlay(Client client, DaeyaltDispenserPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.BOTTOM_LEFT);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);

    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.setPreferredSize(new Dimension(200, 320));
        panelComponent.getChildren().add(TitleComponent.builder()
                .text("Daeyalt Dispenser")
                .color(Color.BLUE)
                .build());
        panelComponent.getChildren().add(TitleComponent.builder()
                .text(plugin.started ? "Running" : "Paused")
                .color(plugin.started ? Color.GREEN : Color.RED)
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("State: ")
                .leftColor(Color.BLUE)
                .right(plugin.state==null || !plugin.started ? "STOPPED" : plugin.state.name())
                .rightColor(Color.WHITE)
                .build());
        panelComponent.getChildren().add(LineComponent.builder()
                .left("Elapsed Time: ")
                .leftColor(Color.BLUE)
                .right(plugin.getElapsedTime())
                .rightColor(Color.WHITE)
                .build());

        return super.render(graphics);
    }
}