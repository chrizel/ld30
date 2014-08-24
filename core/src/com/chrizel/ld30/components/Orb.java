package com.chrizel.ld30.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Orb extends Component {
    public TextureRegion enabled;
    public TextureRegion disabled;

    public Orb(TextureRegion enabled, TextureRegion disabled) {
        this.enabled = enabled;
        this.disabled = disabled;
    }
}
