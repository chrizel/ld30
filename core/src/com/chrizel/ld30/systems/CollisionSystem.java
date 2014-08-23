package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.chrizel.ld30.components.ColliderComponent;
import com.chrizel.ld30.components.MovementComponent;
import com.chrizel.ld30.components.PositionComponent;
import com.chrizel.ld30.Utils;

@Wire
public class CollisionSystem extends EntitySystem {

    ComponentMapper<PositionComponent> pm;
    ComponentMapper<ColliderComponent> cm;
    ComponentMapper<MovementComponent> mm;

    private Vector2 v1 = new Vector2();
    private Vector2 v2 = new Vector2();
    private Vector2 v3 = new Vector2();
    private Vector2 v4 = new Vector2();

    public CollisionSystem() {
        super(Aspect.getAspectForAll(PositionComponent.class, ColliderComponent.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        PositionComponent position1, position2;
        ColliderComponent collider1, collider2;
        MovementComponent movement1;

        for (int i = 0; i < entities.size(); ++i) {
            Entity e1 = entities.get(i);

            position1 = pm.get(e1);
            collider1 = cm.get(e1);
            movement1 = mm.getSafe(e1);

            float velX = movement1 == null ? 0 : movement1.velocityX * movement1.speed * Gdx.graphics.getDeltaTime();
            float velY = movement1 == null ? 0 : movement1.velocityY * movement1.speed * Gdx.graphics.getDeltaTime();

            for (int j = 0; j < entities.size(); ++j) {
                if (i == j) {
                    continue;
                }

                Entity e2 = entities.get(j);

                position2 = pm.get(e2);
                collider2 = cm.get(e2);

                // Do the two colliders collide?
                if (Utils.collide(position1, collider1, position2, collider2, velX, velY)) {
                    if (movement1 != null) {
                        movement1.velocityX = 0;
                        movement1.velocityY = 0;
                    }
                }
            }
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
