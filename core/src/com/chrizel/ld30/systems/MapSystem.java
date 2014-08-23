package com.chrizel.ld30.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.ColliderComponent;
import com.chrizel.ld30.components.DrawableComponent;
import com.chrizel.ld30.components.MapComponent;
import com.chrizel.ld30.components.PositionComponent;

public class MapSystem extends IteratingSystem {
    ComponentMapper<MapComponent> mc = ComponentMapper.getFor(MapComponent.class);
    boolean run = false;
    Engine engine;
    private Texture tilesTexture;

    public MapSystem(Texture tilesTexture) {
        super(Family.getFor(MapComponent.class));
        this.tilesTexture = tilesTexture;
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = engine;
        super.addedToEngine(engine);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        this.engine = null;
        super.removedFromEngine(engine);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        if (!run) {
            run = true;

            Entity wall = ((PooledEngine)engine).createEntity();
            wall.add(new PositionComponent(50.0f, 50.0f));
            wall.add(new DrawableComponent(new TextureRegion(tilesTexture, 0, 0, 16, 16)));
            wall.add(new ColliderComponent(16.0f, 16.0f));
            engine.addEntity(wall);
        }
    }
}
