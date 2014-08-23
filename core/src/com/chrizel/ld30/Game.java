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
import com.chrizel.ld30.systems.*;

public class Game extends ApplicationAdapter {
    private Texture heroTexture;
    private Texture tilesTexture;
    private PooledEngine engine;
	
	@Override
	public void create () {
        OrthographicCamera camera = new OrthographicCamera(640, 480);
        camera.position.set(0, 0, 0);
        camera.zoom = 0.5f;
        camera.update();

        heroTexture = new Texture("hero.png");
        tilesTexture = new Texture("tiles.png");

        engine = new PooledEngine();
        engine.addSystem(new PlayerInputSystem(50));
        engine.addSystem(new CollisionSystem(60));
        engine.addSystem(new MovementSystem(75));
        engine.addSystem(new AttackSystem(75));
        engine.addSystem(new RenderSystem(camera, 100));
        engine.addSystem(new AnimationSystem(camera, 100));


        Entity player = engine.createEntity();
        player.add(new PositionComponent(0, 0));
        player.add(new PlayerComponent(100.0f));
        player.add(new FacingComponent(FacingComponent.DOWN));
        player.add(new MovementComponent());
        player.add(new AttackComponent("swing"));
        player.add(new ColliderComponent(16f, 16f));
        player.add(new AnimationComponent()
                        .newAnimation("idle", heroTexture, 1, true, 16, 16, new int[]{0})
                        .newAnimation("walk", heroTexture, 0.1f, true, 16, 16, new int[]{0, 1, 2, 3})
                        .newAnimation("swing", heroTexture, 0.025f, false, 16, 16, new int[]{5, 6, 7, 8, 9})
                        .setAnimation("idle")
        );

        engine.addEntity(player);

        Entity map = engine.createEntity();
        map.add(new MapComponent("map1.png"));
        engine.addEntity(map);
/*
        Entity wall = engine.createEntity();
        wall.add(new PositionComponent(50.0f, 50.0f));
        wall.add(new DrawableComponent(new TextureRegion(tilesTexture, 0, 0, 16, 16)));
        wall.add(new ColliderComponent(16.0f, 16.0f));
        engine.addEntity(wall);


        Entity wall2 = engine.createEntity();
        wall2.add(new PositionComponent(-50.0f, -50.0f));
        wall2.add(new DrawableComponent(new TextureRegion(tilesTexture, 0, 0, 16, 16)));
        wall2.add(new ColliderComponent(16.0f, 16.0f));
        engine.addEntity(wall2);
*/
        //Gdx.input.setInputProcessor(stage);
	}

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        heroTexture.dispose();
        tilesTexture.dispose();
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        engine.update(Gdx.graphics.getDeltaTime());
	}
}
