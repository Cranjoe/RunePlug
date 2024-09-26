package com.example.SepulchreSteward;


import com.google.common.base.Strings;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;

public class SepulchreStewardOverlay extends OverlayPanel {

    private final Client client;
    private final SepulchreStewardPlugin plugin;

    @Inject
    private SepulchreStewardOverlay(Client client, SepulchreStewardPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);

    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (SepulchreStewardPlugin.tileTesting == null) {
            return null;
        }
        if (SepulchreStewardPlugin.pathTesting == null) {
            return null;
        }
        if (SepulchreStewardPlugin.statues == null) {
            return null;
        }
        for (WorldPoint worldPoint : SepulchreStewardPlugin.tileTesting) {
            drawTile(graphics, worldPoint, Color.MAGENTA, 25, "", new BasicStroke((float) 1));
        }
        for (WorldPoint worldPoint : SepulchreStewardPlugin.pathTesting) {
            drawTile(graphics, worldPoint, Color.CYAN, 25, "", new BasicStroke((float) 1));
        }
        for (WorldPoint worldPoint : SepulchreStewardPlugin.statues) {
            drawTile(graphics, worldPoint, Color.RED, 25, "f", new BasicStroke((float) 1));
        }


        return null;
    }

    private void drawTile(Graphics2D graphics, WorldPoint point, Color color, int alpha, String label, Stroke borderStroke) {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();

        if (point.distanceTo(playerLocation) >= 60) {
            return;
        }

        LocalPoint lp = LocalPoint.fromWorld(client, point);
        if (lp == null) {
            return;
        }

        Polygon poly = Perspective.getCanvasTilePoly(client, lp);
        if (poly != null) {
            OverlayUtil.renderPolygon(graphics, poly, color, new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha), borderStroke);
        }
        if (!Strings.isNullOrEmpty(label)) {
            Point canvasTextLocation = Perspective.getCanvasTextLocation(client, graphics, lp, label, 0);
            if (canvasTextLocation != null) {
                graphics.setFont(new Font("Arial", 1, 15));
                OverlayUtil.renderTextLocation(graphics, canvasTextLocation, label, color);
            }
        }
    }
}