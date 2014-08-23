package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.ColliderComponent;
import com.chrizel.ld30.components.DrawableComponent;
import com.chrizel.ld30.components.PlayerComponent;
import com.chrizel.ld30.components.PositionComponent;

import java.util.ArrayList;

@Wire
public class MapSystem extends EntityProcessingSystem {
    private ComponentMapper<PositionComponent> positionMapper;

    private Texture tilesTexture;
    private Pixmap pixmap;

    private int screenX = 0;
    private int screenY = 0;

    private float cameraSpeed = 100.0f;
    private final int screenWidth = 20;
    private final int screenHeight = 15;
    private OrthographicCamera camera;

    private ArrayList<Entity> screenEntities;

    public MapSystem(OrthographicCamera camera, String tilesTexture, String mapName) {
        super(Aspect.getAspectForAll(PlayerComponent.class, PositionComponent.class));
        this.camera = camera;
        this.tilesTexture = new Texture(Gdx.files.internal(tilesTexture));
        this.pixmap = new Pixmap(Gdx.files.internal(mapName));

        screenEntities = new ArrayList<Entity>(100);
    }

    public void loadScreen() {
        System.out.println("loadScreen " + screenX + "/" + screenY);
        for (int y = Math.abs(screenY * screenHeight); y < Math.abs(screenY * screenHeight) + screenHeight; y++) {
            for (int x = screenX * screenWidth; x < (screenX * screenWidth) + screenWidth; x++) {
                int pixel = pixmap.getPixel(x, y);

                if (pixel == Color.rgba8888(0f, 0f, 0f, 1f)) {
                    screenEntities.add(
                            new EntityBuilder(world)
                                    .with(
                                            new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                            new DrawableComponent(new TextureRegion(tilesTexture, 0, 0, 16, 16)),
                                            new ColliderComponent(16.0f, 16.0f)
                                    )
                                    .build());
                }
            }
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
        pixmap.dispose();
        tilesTexture.dispose();
    }

    @Override
    protected void process(Entity e) {
        float x = screenX * screenWidth * 16;
        float y = screenY * screenHeight * 16;

        // Is the camera on the right position?
        if (camera.position.x - 160 != x) {
            camera.position.x = x + 160;
        } else if (camera.position.y != y + 120) {
            camera.position.y = y + 120;
        } else {
            PositionComponent position = positionMapper.get(e);
            boolean load = false;

            // Is the player out of screen?
            if (position.x > x + screenWidth * 16) {
                screenX += 1;
                load = true;
            } else if (position.x < x) {
                screenX -= 1;
                load = true;
            } else if (position.y > y + screenHeight * 16) {
                screenY += 1;
                load = true;
            } else if (position.y < y) {
                screenY -= 1;
                load = true;
            }

            if (load) {
                for (Entity entity : screenEntities) {
                    entity.deleteFromWorld();
                }
                screenEntities.clear();

                loadScreen();
            }
        }
    }
}
