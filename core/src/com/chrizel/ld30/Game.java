package com.chrizel.ld30;

import com.artemis.Entity;
import com.artemis.World;
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
	public void create () {
        OrthographicCamera camera = new OrthographicCamera(640, 480);
        camera.position.set(160, 120, 0);
        camera.zoom = 0.5f;
        camera.update();

        heroTexture = new Texture("hero.png");
        mapSystem = new MapSystem(camera, "tiles.png", "map1.png");

        world = new World();
        world.setSystem(new PlayerInputSystem());
        world.setSystem(new CollisionSystem());
        world.setSystem(new MovementSystem());
        world.setSystem(new AttackSystem());
        world.setSystem(new RenderSystem(camera));
        world.setSystem(new AnimationSystem(camera));
        world.setSystem(mapSystem);
        world.initialize();

        Entity player = new EntityBuilder(world)
                .with(
                        new PositionComponent(152f, 112f),
                        new PlayerComponent(120.0f),
                        new FacingComponent(FacingComponent.DOWN),
                        new MovementComponent(),
                        new AttackComponent("swing"),
                        new ColliderComponent(8f, 8f),
                        new AnimationComponent()
                                .newAnimation("idle", heroTexture, 1, true, 16, 16, new int[]{0})
                                .newAnimation("walk", heroTexture, 0.1f, true, 16, 16, new int[]{0, 1, 2, 3})
                                .newAnimation("swing", heroTexture, 0.025f, false, 16, 16, new int[]{5, 6, 7, 8, 9})
                                .setAnimation("idle")
                )
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
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();
	}
}
