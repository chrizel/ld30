package com.chrizel.ld30.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.EntityBuilder;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.*;

@Wire
public class MapSystem extends VoidEntitySystem {
    private ComponentMapper<PositionComponent> positionMapper;

    private Texture tilesTexture;
    private Texture heroTexture;
    private Pixmap pixmap1;
    private Pixmap pixmap2;

    private Texture enemy1;

    public int activeMap = 0;
    private int screenX = 0;
    private int screenY = 0;

    private float cameraSpeed = 100.0f;
    private final int screenWidth = 20;
    private final int screenHeight = 15;
    private OrthographicCamera camera;

    public boolean spikeState = true;

    public MapSystem(OrthographicCamera camera, String tilesTexture, String mapName1, String mapName2) {
        super();
        this.camera = camera;
        this.tilesTexture = new Texture(Gdx.files.internal(tilesTexture));
        this.pixmap1 = new Pixmap(Gdx.files.internal(mapName1));
        this.pixmap2 = new Pixmap(Gdx.files.internal(mapName2));
        this.enemy1 = new Texture(Gdx.files.internal("enemy1.png"));
        heroTexture = new Texture("hero.png");
    }

    @Override
    protected void initialize() {
        super.initialize();
        loadScreen();
    }

    public void loadScreen() {
        Pixmap pixmap = activeMap == 0 ? pixmap1 : pixmap2;
        Texture tilesTexture = this.tilesTexture;

        // Remove old entities...
        ImmutableBag<Entity> mapObjects = world.getManager(GroupManager.class).getEntities("mapObject");
        while (!mapObjects.isEmpty()) {
            mapObjects.get(0).deleteFromWorld();
        }

        for (int y = Math.abs(screenY * screenHeight); y < Math.abs(screenY * screenHeight) + screenHeight; y++) {
            for (int x = screenX * screenWidth; x < (screenX * screenWidth) + screenWidth; x++) {
                int pixel = pixmap.getPixel(x, y);

                if (pixel == Color.rgba8888(0f, 0f, 0f, 1f)) {
                    // wall
                    new EntityBuilder(world)
                            .with(
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                    new Drawable(new TextureRegion(tilesTexture, (128 * activeMap) + 0, 0, 16, 16)),
                                    new Collider(16.0f, 16.0f)
                            )
                            .group("mapObject")
                            .build();
                } else if (pixel == Color.rgba8888(0f, 0f, 1f, 1f)) {
                    // portal
                    new EntityBuilder(world)
                            .with(
                                    new PortalComponent(),
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                    new Drawable(new TextureRegion(tilesTexture, (128 * activeMap) + 16, 0, 16, 16))
                            )
                            .group("mapObject")
                            .build();
                } else if (pixel == -8388353 /* orange */) {
                    // orange spikes
                    new EntityBuilder(world)
                            .with(
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                    new Drawable(new TextureRegion(tilesTexture, 0, 16, 16, 16)),
                                    new Spike(true, new TextureRegion(tilesTexture, 0, 16, 16, 16), new TextureRegion(tilesTexture, 16, 16, 16, 16)),
                                    new Collider(16f, 16f)
                            )
                            .group("spike")
                            .group("mapObject")
                            .build();
                } else if (pixel == 517472255 /* bright blue */) {
                    // blue spikes
                    new EntityBuilder(world)
                            .with(
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                    new Drawable(),
                                    new Spike(false, new TextureRegion(tilesTexture, 16, 32, 16, 16), new TextureRegion(tilesTexture, 0, 32, 16, 16)),
                                    new Collider(16f, 16f)
                            )
                            .group("spike")
                            .group("mapObject")
                            .build();
                } else if (pixel == -16711681 /* purple */) {
                    // orb
                    new EntityBuilder(world)
                            .with(
                                    new Orb(new TextureRegion(tilesTexture, 0, 48, 16, 16), new TextureRegion(tilesTexture, 16, 48, 16, 16)),
                                    new Hitable(.5f),
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                    new Drawable(),
                                    new Collider(8f, 8f)
                            )
                            .group("mapObject")
                            .build();
                } else if (pixel == Color.rgba8888(1f, 0f, 0f, 1f)) {
                    // green blob enemy
                    new EntityBuilder(world)
                            .with(
                                    new Drawable(),
                                    new Hitable(),
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f),
                                    new AttackComponent("attack", 1f, 40f, 16f, 16f),
                                    new HealthComponent(100f),
                                    new EnemyAI(0, 16f * 3, 2f),
                                    new MovementComponent(5f),
                                    new AnimationComponent()
                                            .newAnimation("idle", enemy1, 0.25f, true, 16, 16, new int[]{0, 1})
                                            .newAnimation("hit", enemy1, 0.1f, true, 16, 16, new int[]{2, 3})
                                            .newAnimation("attack", enemy1, 0.1f, true, 16, 16, new int[]{4, 5})
                                            .setAnimation("idle"),
                                    new CorpseComponent(new TextureRegion(enemy1, 96, 0, 16, 16)),
                                    new Collider(8f, 8f)
                            )
                            .group("mapObject")
                            .build();
                } else if (pixel == Color.rgba8888(0f, 1f, 0f, 1f)) {
                    // Player already exists in game?
                    if (world.getManager(TagManager.class).isRegistered("player")) {
                        continue;
                    }

                    float attackSpeed = 0.25f;
                    new EntityBuilder(world)
                            .with(
                                    new PositionComponent(x * 16f, (screenHeight - 1 - y) * 16f, 100f),
                                    new Hitable(),
                                    new PlayerComponent(),
                                    new FacingComponent(FacingComponent.RIGHT),
                                    new MovementComponent(0, 0, 7f),
                                    new AttackComponent("swing", attackSpeed, 300f, 13f, 13f),
                                    new HealthComponent(100f),
                                    new Collider(8f, 8f),
                                    new CorpseComponent(new TextureRegion(heroTexture, 160, 0, 16, 16)),
                                    new Drawable(),
                                    new AnimationComponent()
                                            .newAnimation("idle", heroTexture, 1, true, 16, 16, new int[]{0})
                                            .newAnimation("walk", heroTexture, 0.08f, true, 16, 16, new int[]{0, 1, 2, 3})
                                            .newAnimation("hit", heroTexture, 0.025f, true, 16, 16, new int[]{0, 1, 2, 3})
                                            .newAnimation("swing", heroTexture, attackSpeed / 5, false, 16, 16, new int[]{5, 6, 7, 8, 9})
                                            .setAnimation("idle")
                            )
                            .tag("player")
                            .build();
                } else if (pixel == -1) {
                    // nothing, just background
                } else {
                    System.out.println("MapSystem: unknown color: " + pixel);
                }
            }
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
        pixmap1.dispose();
        pixmap2.dispose();
        tilesTexture.dispose();
        enemy1.dispose();
        heroTexture.dispose();
    }

    @Override
    protected void processSystem() {
        TagManager tagManager = world.getManager(TagManager.class);

        if (activeMap == 0) {
            Gdx.gl.glClearColor(.9f, 1f, .9f, 1);
        } else {
            Gdx.gl.glClearColor(1f, .9f, 1f, 1);
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float x = screenX * screenWidth * 16;
        float y = screenY * screenHeight * 16;

        // Is the camera on the right position?
        if (camera.position.x - 160 != x) {
            camera.position.x = x + 160;
        } else if (camera.position.y != y + 120) {
            camera.position.y = y + 120;
        } else if (tagManager.isRegistered("player")) {
            Entity player = tagManager.getEntity("player");
            PositionComponent position = positionMapper.get(player);
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
                loadScreen();
            }
        }
    }
}
