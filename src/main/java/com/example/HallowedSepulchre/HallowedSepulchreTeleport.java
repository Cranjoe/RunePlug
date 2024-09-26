package com.example.HallowedSepulchre;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import net.runelite.api.GraphicsObject;

@Getter(AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
class HallowedSepulchreTeleport
{
	@NonNull
	@EqualsAndHashCode.Include
	private final GraphicsObject graphicsObject;

	private int ticksUntilDespawn = 4;

	private final int id;

	private final boolean isSpawning;

	public HallowedSepulchreTeleport(GraphicsObject object, int id) {
		this.graphicsObject = object;
		this. id = id;

		this.isSpawning = id == 1815;
		if (id == 1815) {
			ticksUntilDespawn = 1;
		}
	}

	void updateTicksUntilNextAnimation()
	{
			ticksUntilDespawn--;
	}
}