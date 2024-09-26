package com.example.toaextended.features.scabaras.overlay;

import lombok.Data;
import net.runelite.api.coords.LocalPoint;

import java.awt.*;

@Data
public class MatchingTile
{

	private final LocalPoint localPoint;
	private final String name;
	private final Color color;
	private boolean matched;

}
