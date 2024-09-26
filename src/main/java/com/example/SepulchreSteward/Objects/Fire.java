package com.example.SepulchreSteward.Objects;

import com.example.EthanApiPlugin.Collections.TileObjects;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.TileObject;

import java.util.Objects;

public class Fire  {
    public static final int ANIMATION_START = 8660;
    public static final int ANIMATION_MID = 8661;
    public static final int ANIMATION_MID_2 = 8662;
    public static final int ANIMATION_END = 8663;
    public static final int ANIMATION_REST= 8664;

    @Getter
    private final TileObject fireObject;

    @Getter
    @Setter
    private int animationId;

    public Fire(TileObject fireObject) {
        this.fireObject = fireObject;
    }

    @Getter
    @Setter
    private int currentTick;
    @Getter
    @Setter
    private int maxTick = 8;

    public void updateTick() {
        this.currentTick++;
        if (this.currentTick > this.maxTick) {
            this.currentTick = 1;
        }
    }

    public static int inferTickFromAnimation(int animationId) {
        if (animationId == ANIMATION_START) {
            return 1;
        } else if (animationId == ANIMATION_MID) {
            return 2;
        } else if (animationId == ANIMATION_END) {
            return 5;
        } else {
            return -1; // Indicative of an error or unknown state
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Fire fire = (Fire) obj;
        // Assume a unique identifier for equality check; adjust according to your needs
        return Objects.equals(this.fireObject, fire.fireObject);
    }
}
