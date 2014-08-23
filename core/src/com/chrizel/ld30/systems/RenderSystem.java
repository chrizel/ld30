package com.chrizel.ld30.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chrizel.ld30.components.DrawableComponent;
import com.chrizel.ld30.components.PositionComponent;

public class RenderSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<DrawableComponent> dm = ComponentMapper.getFor(DrawableComponent.class);

    public RenderSystem(OrthographicCamera camera) {
        batch = new SpriteBatch();
        this.camera = camera;
    }

    public void dispose() {
        batch.dispose();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, DrawableComponent.class));
    }

    @Override
    public void removedFromEngine(Engine engine) {
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent positionComponent;
        DrawableComponent drawableComponent;

        camera.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);

            positionComponent = pm.get(e);
            drawableComponent = dm.get(e);

            batch.draw(drawableComponent.region, positionComponent.x, positionComponent.y);
        }

        batch.end();
    }
}
