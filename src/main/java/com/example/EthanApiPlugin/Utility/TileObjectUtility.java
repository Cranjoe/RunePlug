package com.example.EthanApiPlugin.Utility;

import net.runelite.api.Animation;
import net.runelite.api.DynamicObject;
import net.runelite.api.GameObject;
import net.runelite.api.Renderable;
import net.runelite.api.TileObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TileObjectUtility {
    private static final Logger log = LoggerFactory.getLogger(TileObjectUtility.class);

    public static int getAnimationId(TileObject object) {
        try {
            if (object instanceof GameObject) {
                Renderable renderable = ((GameObject)object).getRenderable();
                if (renderable instanceof DynamicObject) {
                    Animation animation = ((DynamicObject)renderable).getAnimation();
                    if (animation != null)
                        return animation.getId();
                }
            }
            log.info("" + object.getId() + " no animation id error (null)");
            return -1;
        } catch (NullPointerException np) {
            np.printStackTrace();
            return -1;
        }
    }
}
