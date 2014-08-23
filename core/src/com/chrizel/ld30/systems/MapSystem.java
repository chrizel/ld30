package com.chrizel.ld30.systems;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.ColliderComponent;
import com.chrizel.ld30.components.DrawableComponent;
import com.chrizel.ld30.components.PositionComponent;

@Wire
public class MapSystem extends VoidEntitySystem {
    private Texture tilesTexture;
    private Pixmap pixmap;
    private final int screenWidth = 20;
    private final int screenHeight = 15;

    public MapSystem(String tilesTexture, String mapName) {
        this.tilesTexture = new Texture(Gdx.files.internal(tilesTexture));
        this.pixmap = new Pixmap(Gdx.files.internal(mapName));
    }

    public void loadScreen(int x, int y) {
        new EntityBuilder(world)
                .with(
                        new PositionComponent(50.0f, 50.0f),
                        new DrawableComponent(new TextureRegion(tilesTexture, 0, 0, 16, 16)),
                        new ColliderComponent(16.0f, 16.0f)
                )
                .build();


        new EntityBuilder(world)
                .with(
                        new PositionComponent(-50.0f, -50.0f),
                        new DrawableComponent(new TextureRegion(tilesTexture, 0, 0, 16, 16)),
                        new ColliderComponent(16.0f, 16.0f)
                )
                .build();
    }

    @Override
    protected void processSystem() {
        //???
    }

    @Override
    protected void dispose() {
        super.dispose();
        pixmap.dispose();
        tilesTexture.dispose();
    }
}
