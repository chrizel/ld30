package com.chrizel.ld30.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.chrizel.ld30.components.*;

import javax.swing.text.Position;

public class PlayerInputSystem extends IteratingSystem {
    ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
    ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    ComponentMapper<FacingComponent> fm = ComponentMapper.getFor(FacingComponent.class);
    ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);

    public PlayerInputSystem() {
        super(Family.getFor(MovementComponent.class, PlayerComponent.class, FacingComponent.class, AnimationComponent.class));
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent movement = mm.get(entity);
        PlayerComponent player = pm.get(entity);
        FacingComponent facing = fm.get(entity);
        AnimationComponent animation = am.get(entity);

        boolean walk = false;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movement.velocityX = player.speed;
            facing.facing = FacingComponent.RIGHT;
            walk = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            movement.velocityX = player.speed * -1;
            facing.facing = FacingComponent.LEFT;
            walk = true;
        } else {
            movement.velocityX = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movement.velocityY = player.speed;
            facing.facing = FacingComponent.UP;
            walk = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movement.velocityY = player.speed * -1;
            facing.facing = FacingComponent.DOWN;
            walk = true;
        } else {
            movement.velocityY = 0;
        }

        animation.setAnimation(walk ? "walk" : "idle");
    }
}
