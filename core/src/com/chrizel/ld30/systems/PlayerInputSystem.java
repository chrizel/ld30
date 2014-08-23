package com.chrizel.ld30.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.chrizel.ld30.components.*;

public class PlayerInputSystem extends IteratingSystem {
    ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);
    ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    ComponentMapper<FacingComponent> fm = ComponentMapper.getFor(FacingComponent.class);
    ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    ComponentMapper<AttackComponent> attackMapper = ComponentMapper.getFor(AttackComponent.class);

    public PlayerInputSystem(int priority) {
        super(Family.getFor(
                MovementComponent.class,
                PlayerComponent.class,
                FacingComponent.class,
                AnimationComponent.class,
                AttackComponent.class), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        MovementComponent movement = mm.get(entity);
        PlayerComponent player = pm.get(entity);
        FacingComponent facing = fm.get(entity);
        AnimationComponent animation = am.get(entity);
        AttackComponent attack = attackMapper.get(entity);

        boolean walk = false;
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movement.velocityX = player.speed;
            if (!shift) {
                facing.facing = FacingComponent.RIGHT;
            }
            walk = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            movement.velocityX = player.speed * -1;
            if (!shift) {
                facing.facing = FacingComponent.LEFT;
            }
            walk = true;
        } else {
            movement.velocityX = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movement.velocityY = player.speed;
            if (!shift) {
                facing.facing = FacingComponent.UP;
            }
            walk = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movement.velocityY = player.speed * -1;
            if (!shift) {
                facing.facing = FacingComponent.DOWN;
            }
            walk = true;
        } else {
            movement.velocityY = 0;
        }

        if (!attack.isAttacking) {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                attack.isAttacking = true;
            } else {
                animation.setAnimation(walk ? "walk" : "idle");
            }
        }
    }
}
