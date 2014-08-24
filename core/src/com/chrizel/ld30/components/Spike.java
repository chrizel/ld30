package com.chrizel.ld30.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Spike extends Component {
    public boolean orange;
    public TextureRegion enabled;
    public TextureRegion disabled;

    public Spike(boolean orange, TextureRegion enabled, TextureRegion disabled) {
        this.orange = orange;
        this.enabled = enabled;
        this.disabled = disabled;
    }
}
