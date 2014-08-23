package com.chrizel.ld30.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DrawableComponent extends Component {
    public TextureRegion region;

    public DrawableComponent(TextureRegion region) {
        this.region = region;
    }
}