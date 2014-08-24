package com.chrizel.ld30.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spike extends Component {
    public TextureRegion enabled;
    public TextureRegion disabled;
    public int type;

    public static int ORANGE = 0;
    public static int BLUE = 1;
    public static int GREEN = 2;

    public Spike(int type, TextureRegion enabled, TextureRegion disabled) {
        this.type = type;
        this.enabled = enabled;
        this.disabled = disabled;
    }
}
