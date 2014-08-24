package com.chrizel.ld30.systems;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

@Wire
public class HelpSystem extends VoidEntitySystem {
    MapSystem mapSystem;

    private final Texture texture;
    private final SpriteBatch batch;

    public HelpSystem() {
        texture = new Texture(Gdx.files.internal("controls.png"));
        batch = new SpriteBatch();
    }

    @Override
    protected void dispose() {
        texture.dispose();
        batch.dispose();
    }

    @Override
    protected void processSystem() {
        if (mapSystem.screenX == 0 && mapSystem.screenY == 0) {
            batch.begin();
            batch.draw(texture, 0, 0);
            batch.end();
        }
    }
}
