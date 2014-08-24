package com.chrizel.ld30;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.utils.EntityBuilder;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.*;
import com.chrizel.ld30.systems.*;

public class Game extends ApplicationAdapter {
    private Texture heroTexture;
    private World world;
    private MapSystem mapSystem;

    @Override
    public void create() {
        OrthographicCamera camera = new OrthographicCamera(640, 480);
        camera.position.set(160, 120, 0);
        camera.zoom = 0.5f;
        camera.update();

        heroTexture = new Texture("hero.png");
        mapSystem = new MapSystem(camera, "tiles1.png", "tiles2.png", "map1.png", "map2.png");

        world = new World();
        world.setManager(new TagManager());
        world.setManager(new GroupManager());
        world.setSystem(new PlayerInputSystem());
        world.setSystem(new CollisionSystem());
        world.setSystem(new MovementSystem());
        world.setSystem(new AttackAnimationSystem());
        world.setSystem(new AutoMoveSystem());
        world.setSystem(new DamageSystem());
        world.setSystem(new DeathSystem());
        world.setSystem(new PortalSystem());
        world.setSystem(mapSystem);
        world.setSystem(new RenderSystem(camera));
        world.setSystem(new AnimationSystem(camera));
        world.setSystem(new HUDSystem());
        world.initialize();

        float attackSpeed = 0.25f;
        Entity player = new EntityBuilder(world)
                .with(
                        new PositionComponent(152f, 112f),
                        new PlayerComponent(),
                        new FacingComponent(FacingComponent.RIGHT),
                        new MovementComponent(0, 0, 7f),
                        new AttackComponent("swing", attackSpeed, 300f, 13f, 13f),
                        new HealthComponent(100f),
                        new ColliderComponent(8f, 8f),
                        new CorpseComponent(new TextureRegion(heroTexture, 160, 0, 16, 16)),
                        new AnimationComponent()
                                .newAnimation("idle", heroTexture, 1, true, 16, 16, new int[]{0})
                                .newAnimation("walk", heroTexture, 0.08f, true, 16, 16, new int[]{0, 1, 2, 3})
                                .newAnimation("hit", heroTexture, 0.025f, true, 16, 16, new int[]{0, 1, 2, 3})
                                .newAnimation("swing", heroTexture, attackSpeed / 5, false, 16, 16, new int[]{5, 6, 7, 8, 9})
                                .setAnimation("idle")
                )
                .tag("player")
                .build();

        mapSystem.loadScreen();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        heroTexture.dispose();
    }

    @Override
    public void render() {
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();
    }
}
