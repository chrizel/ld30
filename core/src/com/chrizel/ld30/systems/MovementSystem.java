package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.chrizel.ld30.components.MovementComponent;
import com.chrizel.ld30.components.PositionComponent;

@Wire
public class MovementSystem extends EntityProcessingSystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<MovementComponent> mm;

    public MovementSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, MovementComponent.class));
    }

    @Override
    protected void process(Entity e) {
        PositionComponent position = pm.get(e);
        MovementComponent movement = mm.get(e);

        position.x += movement.velocityX * Gdx.graphics.getDeltaTime();
        position.y += movement.velocityY * Gdx.graphics.getDeltaTime();
    }
}
