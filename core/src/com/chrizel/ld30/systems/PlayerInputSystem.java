package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.chrizel.ld30.components.*;

@Wire
public class PlayerInputSystem extends EntityProcessingSystem {
    ComponentMapper<MovementComponent> mm;
    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<FacingComponent> fm;
    ComponentMapper<AnimationComponent> am;
    ComponentMapper<AttackComponent> attackMapper;

    public PlayerInputSystem() {
        super(Aspect.getAspectForAll(
                MovementComponent.class,
                PlayerComponent.class,
                FacingComponent.class,
                AnimationComponent.class,
                AttackComponent.class));
    }

    @Override
    protected void process(Entity e) {
        MovementComponent movement = mm.get(e);
        PlayerComponent player = pm.get(e);
        FacingComponent facing = fm.get(e);
        AnimationComponent animation = am.get(e);
        AttackComponent attack = attackMapper.get(e);

        boolean walk = false;
        boolean shift = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.velocityX = 16f;
            if (!shift) {
                facing.facing = FacingComponent.RIGHT;
            }
            walk = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.velocityX = 16f * -1;
            if (!shift) {
                facing.facing = FacingComponent.LEFT;
            }
            walk = true;
        } else {
            movement.velocityX = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            movement.velocityY = 16f;
            if (!shift) {
                facing.facing = FacingComponent.UP;
            }
            walk = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            movement.velocityY = 16f * -1;
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
