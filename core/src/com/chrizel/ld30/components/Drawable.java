package com.chrizel.ld30.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Drawable extends Component {
    public TextureRegion region;
    public float tint;

    public Drawable(TextureRegion region, float tint) {
        this.region = region;
        this.tint = tint;
    }

    public Drawable(TextureRegion region) {
        this(region, Color.WHITE.toFloatBits());
    }

    public Drawable() {
        this(null);
    }
}
