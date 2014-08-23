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
import com.sun.xml.internal.ws.api.pipe.Engine;

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

            float velX = movement1 == null ? 0 : movement1.velocityX * Gdx.graphics.getDeltaTime();
            float velY = movement1 == null ? 0 : movement1.velocityY * Gdx.graphics.getDeltaTime();

            float padX = (16f - collider1.width) / 2;
            float padY = (16f - collider1.height) / 2;

            v1.set(position1.x + padX + velX, position1.y + padY + velY);
            v2.set(position1.x + padX + velX + collider1.width, position1.y + padY + velY);
            v3.set(position1.x + padX + velX + collider1.width, position1.y + padY + velY + collider1.height);
            v4.set(position1.x + padX + velX, position1.y + padY + velY + collider1.height);

            for (int j = 0; j < entities.size(); ++j) {
                if (i == j) {
                    continue;
                }

                Entity e2 = entities.get(j);

                position2 = pm.get(e2);
                collider2 = cm.get(e2);

                padX = (16f - collider2.width);
                padY = (16f - collider2.height);

                // Do the two colliders collide?
                boolean collide =
                    (v1.x >= position2.x + padX && v1.x <= position2.x + padX + collider2.width && v1.y >= position2.y + padY && v1.y <= position2.y + padY + collider2.height) ||
                    (v2.x >= position2.x + padX && v2.x <= position2.x + padX + collider2.width && v2.y >= position2.y + padY && v2.y <= position2.y + padY + collider2.height) ||
                    (v3.x >= position2.x + padX && v3.x <= position2.x + padX + collider2.width && v3.y >= position2.y + padY && v3.y <= position2.y + padY + collider2.height) ||
                    (v4.x >= position2.x + padX && v4.x <= position2.x + padX + collider2.width && v4.y >= position2.y + padY && v4.y <= position2.y + padY + collider2.height);

                if (collide) {
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
