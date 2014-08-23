package com.chrizel.ld30;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.*;
import com.chrizel.ld30.systems.AnimationSystem;
import com.chrizel.ld30.systems.MovementSystem;
import com.chrizel.ld30.systems.PlayerInputSystem;
import com.chrizel.ld30.systems.RenderSystem;

public class Game extends ApplicationAdapter {
    private Texture heroTexture;
    private PooledEngine engine;
	
	@Override
	public void create () {
        OrthographicCamera camera = new OrthographicCamera(640, 480);
        camera.position.set(0, 0, 0);
        camera.zoom = 0.5f;
        camera.update();

        heroTexture = new Texture("hero.png");

        engine = new PooledEngine();
        engine.addSystem(new MovementSystem());
        engine.addSystem(new PlayerInputSystem());
        engine.addSystem(new RenderSystem(camera));
        engine.addSystem(new AnimationSystem(camera));

        Entity player = engine.createEntity();
        player.add(new PositionComponent(0, 0));
        player.add(new PlayerComponent());
        player.add(new FacingComponent(FacingComponent.DOWN));
        player.add(new MovementComponent());
        player.add(new AnimationComponent()
            .newAnimation("idle", heroTexture, 1, 16, 16, new int[] { 0 })
            .newAnimation("walk", heroTexture, 0.1f, 16, 16, new int[] { 0, 1, 2, 3 })
            .setAnimation("idle")
        );

        engine.addEntity(player);

        //Gdx.input.setInputProcessor(stage);
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
        engine.update(Gdx.graphics.getDeltaTime());
	}
}
