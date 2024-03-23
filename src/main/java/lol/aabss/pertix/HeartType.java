//
//
// credit Gaider10 & vi945786
//
//

package lol.aabss.pertix;

import net.minecraft.util.Identifier;

public enum HeartType {
    EMPTY("container"),
    RED_FULL("full"),
    RED_HALF("half"),
    YELLOW_FULL("absorbing_full"),
    YELLOW_HALF("absorbing_half");

    public final Identifier icon;

    HeartType(String heartIcon) {
        icon = new Identifier("minecraft", "textures/gui/sprites/hud/heart/" + heartIcon + ".png");
    }
}