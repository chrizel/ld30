package com.chrizel.ld30.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.chrizel.ld30.components.MovementComponent;
import com.chrizel.ld30.components.PositionComponent;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);

    public MovementSystem() {
        super(Family.getFor(PositionComponent.class, MovementComponent.class), 50);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        PositionComponent position = pm.get(entity);
        MovementComponent movement = mm.get(entity);

        position.x += movement.velocityX * deltaTime;
        position.y += movement.velocityY * deltaTime;
    }
}
