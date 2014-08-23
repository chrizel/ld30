package com.chrizel.ld30.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CorpseComponent extends Component {
    public TextureRegion corpseTexture;

    public CorpseComponent(TextureRegion corpseTexture) {
        this.corpseTexture = corpseTexture;
    }
}
