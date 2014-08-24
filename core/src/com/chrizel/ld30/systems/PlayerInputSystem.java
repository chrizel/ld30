package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.chrizel.ld30.components.*;

@Wire
public class PlayerInputSystem extends EntityProcessingSystem {
    ComponentMapper<MovementComponent> mm;
    ComponentMapper<PlayerComponent> pm;
    ComponentMapper<FacingComponent> fm;
    ComponentMapper<AnimationComponent> am;
    ComponentMapper<AttackComponent> attackMapper;
    Vector2 vector;

    public PlayerInputSystem() {
        super(Aspect.getAspectForAll(
                MovementComponent.class,
                PlayerComponent.class,
                FacingComponent.class,
                AnimationComponent.class,
                AttackComponent.class));
        vector = new Vector2();
    }

    @Override
    protected void process(Entity e) {
        MovementComponent movement = mm.get(e);
        PlayerComponent player = pm.get(e);
        FacingComponent facing = fm.get(e);
        AnimationComponent animation = am.get(e);
        AttackComponent attack = attackMapper.get(e);

        boolean up = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);


        boolean walk = up || down || left || right;
        if (walk) {
            float angle = 0;
            if (up) {
                angle = 90f;
                if (left) {
                    if (!(facing.facing == FacingComponent.UP || facing.facing == FacingComponent.LEFT)) {
                        facing.facing = FacingComponent.UP;
                    }
                    angle += 45f;
                } else if (right) {
                    if (!(facing.facing == FacingComponent.UP || facing.facing == FacingComponent.RIGHT)) {
                        facing.facing = FacingComponent.UP;
                    }
                    angle -= 45f;
                } else {
                    facing.facing = FacingComponent.UP;
                }
            } else if (down) {
                angle = -90f;
                if (left) {
                    if (!(facing.facing == FacingComponent.DOWN || facing.facing == FacingComponent.LEFT)) {
                        facing.facing = FacingComponent.DOWN;
                    }
                    angle -= 45f;
                } else if (right) {
                    if (!(facing.facing == FacingComponent.DOWN || facing.facing == FacingComponent.RIGHT)) {
                        facing.facing = FacingComponent.DOWN;
                    }
                    angle += 45f;
                } else {
                    facing.facing = FacingComponent.DOWN;
                }
            } else if (left) {
                facing.facing = FacingComponent.LEFT;
                angle = 180f;
            } else if (right) {
                facing.facing = FacingComponent.RIGHT;
                angle = 0;
            }

            vector.set(16f, 0);
            vector.rotate(angle);
            movement.velocityX = vector.x;
            movement.velocityY = vector.y;
        } else {
            movement.velocityX = 0;
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
