package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.EntityBuilder;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.*;

import java.util.ArrayList;

@Wire
public class MapSystem extends EntityProcessingSystem {
    private ComponentMapper<PositionComponent> positionMapper;

    private Texture tilesTexture;
    private Pixmap pixmap;
    private Texture enemy1;

    private int screenX = 0;
    private int screenY = 0;

    private float cameraSpeed = 100.0f;
    private final int screenWidth = 20;
    private final int screenHeight = 15;
    private OrthographicCamera camera;

    public MapSystem(OrthographicCamera camera, String tilesTexture, String mapName) {
        super(Aspect.getAspectForAll(PlayerComponent.class, PositionComponent.class));
        this.camera = camera;
        this.tilesTexture = new Texture(Gdx.files.internal(tilesTexture));
        this.pixmap = new Pixmap(Gdx.files.internal(mapName));
        this.enemy1 = new Texture(Gdx.files.internal("enemy1.png"));
    }

    public void loadScreen() {
        //System.out.println("loadScreen " + screenX + "/" + screenY);
        for (int y = Math.abs(screenY * screenHeight); y < Math.abs(screenY * screenHeight) + screenHeight; y++) {
            for (int x = screenX * screenWidth; x < (screenX * screenWidth) + screenWidth; x++) {
                int pixel = pixmap.getPixel(x, y);

                if (pixel == Color.rgba8888(0f, 0f, 0f, 1f)) {
                    new EntityBuilder(world)
                            .with(
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                    new DrawableComponent(new TextureRegion(tilesTexture, 0, 0, 16, 16)),
                                    new ColliderComponent(16.0f, 16.0f)
                            )
                            .group("mapObject")
                            .build();
                } else if (pixel == Color.rgba8888(1f, 0f, 0f, 1f)) {
                    new EntityBuilder(world)
                            .with(
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                    new AttackComponent("attack", 1f, 40f, 16f, 16f),
                                    new HealthComponent(100f),
                                    new AutoMoveComponent(0, 16f * 3, 2f),
                                    new MovementComponent(5f),
                                    new AnimationComponent()
                                            .newAnimation("idle", enemy1, 0.25f, true, 16, 16, new int[]{0, 1})
                                            .newAnimation("hit", enemy1, 0.1f, true, 16, 16, new int[]{2, 3})
                                            .newAnimation("attack", enemy1, 0.1f, true, 16, 16, new int[]{4, 5})
                                            .setAnimation("idle"),
                                    new CorpseComponent(new TextureRegion(enemy1, 96, 0, 16, 16)),
                                    new ColliderComponent(8f, 8f)
                            )
                            .group("mapObject")
                            .build();
                }
            }
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
        pixmap.dispose();
        tilesTexture.dispose();
        enemy1.dispose();
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
            if (position.x + 8f > x + screenWidth * 16) {
                screenX += 1;
                load = true;
            } else if (position.x + 8f < x) {
                screenX -= 1;
                load = true;
            } else if (position.y + 8f > y + screenHeight * 16) {
                screenY += 1;
                load = true;
            } else if (position.y + 8f < y) {
                screenY -= 1;
                load = true;
            }

            if (load) {
                ImmutableBag<Entity> mapObjects = world.getManager(GroupManager.class).getEntities("mapObject");
                for (Entity entity : mapObjects) {
                    entity.deleteFromWorld();
                }

                loadScreen();
            }
        }
    }
}
