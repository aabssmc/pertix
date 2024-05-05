//
//
// credit Gaider10 & vi945786
//
//

package lol.aabss.pertix;

import net.minecraft.util.Identifier;

public enum HeartType {
    EMPTY("container", 16 + 0 * 9, 0),
    RED_FULL("full", 16 + 4 * 9, 0),
    RED_HALF("half", 16 + 5 * 9, 0),
    YELLOW_FULL("absorbing_full", 16 + 16 * 9, 0),
    YELLOW_HALF("absorbing_half", 16 + 17 * 9, 0);

    public final Identifier icon;
    public final int u;
    public final int v;

    HeartType(String heartIcon, int u, int v) {
        icon = new Identifier("minecraft", "textures/gui/sprites/hud/heart/" + heartIcon + ".png");
        this.u = u;
        this.v = v;
    }
}