package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.chrizel.ld30.Utils;
import com.chrizel.ld30.components.*;

@Wire
public class DamageSystem extends EntitySystem {
    ComponentMapper<HealthComponent> hm;
    ComponentMapper<AttackComponent> am;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<ColliderComponent> cm;
    ComponentMapper<FacingComponent> fm;
    ComponentMapper<AnimationComponent> animationMapper;

    private ColliderComponent tmpCollider = new ColliderComponent(0, 0);

    public DamageSystem() {
        super(Aspect.getAspectForAll(HealthComponent.class, PositionComponent.class, ColliderComponent.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        AttackComponent attack1;
        PositionComponent position1, position2;
        ColliderComponent collider2;
        HealthComponent health;
        FacingComponent facing;

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);

            attack1 = am.getSafe(e);
            if (attack1 != null && attack1.isAttacking) {
                position1 = pm.get(e);
                tmpCollider.width = attack1.attackWidth;
                tmpCollider.height = attack1.attackHeight;
                facing = fm.getSafe(e);

                float addX1 = 0;
                float addY1 = 0;

                if (facing != null) {
                    switch (facing.facing) {
                        case FacingComponent.RIGHT:
                            addX1 = 8f;
                            break;
                        case FacingComponent.DOWN:
                            addY1 = -8f;
                            break;
                        case FacingComponent.LEFT:
                            addX1 = -8f;
                            break;
                        case FacingComponent.UP:
                            addY1 = 8f;
                            break;
                    }
                }

                // Hit something?
                for (int j = 0; j < entities.size(); ++j) {
                    if (j == i) {
                        continue;
                    }

                    Entity e2 = entities.get(j);

                    position2 = pm.get(e2);
                    collider2 = cm.get(e2);

                    if (Utils.collide(position1, tmpCollider, position2, collider2, addX1, addY1)) {
                        float damage = attack1.dps * Gdx.graphics.getDeltaTime();
                        health = hm.get(e2);
                        health.health -= damage;

                        AnimationComponent ac = animationMapper.getSafe(e2);
                        if (ac != null && ac.hasAnimation("hit")) {
                            ac.setAnimation("hit");
                        }
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
