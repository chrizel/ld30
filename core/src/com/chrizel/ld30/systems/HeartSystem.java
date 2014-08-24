package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.chrizel.ld30.Utils;
import com.chrizel.ld30.components.*;

@Wire
public class HeartSystem extends EntityProcessingSystem {
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<Collider> cm;
    protected ComponentMapper<Heart> mHeart;
    protected ComponentMapper<HealthComponent> mHealthComponent;

    Collider heartCollider = new Collider(16f, 16f);

    public HeartSystem() {
        super(Aspect.getAspectForAll(Heart.class, PositionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        TagManager tagManager = world.getManager(TagManager.class);
        if (!tagManager.isRegistered("player")) {
            return;
        }

        Entity player = tagManager.getEntity("player");
        PositionComponent playerPosition = pm.get(player);
        Collider playerCollider = cm.get(player);
        PositionComponent heartPosition = pm.get(e);

        if (Utils.collide(playerPosition, playerCollider, heartPosition, heartCollider, 0, 0)) {
            HealthComponent health = mHealthComponent.get(player);
            health.health = Math.min(health.health + mHeart.get(e).health, 100f);
            e.deleteFromWorld();
        }
    }
}
