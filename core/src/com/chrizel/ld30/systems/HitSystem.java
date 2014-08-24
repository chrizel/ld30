package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.chrizel.ld30.Utils;
import com.chrizel.ld30.components.*;

@Wire
public class HitSystem extends EntitySystem {
    MapSystem mapSystem;

    ComponentMapper<HealthComponent> hm;
    ComponentMapper<Orb> om;
    ComponentMapper<AttackComponent> am;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<Collider> cm;
    ComponentMapper<FacingComponent> fm;
    ComponentMapper<AnimationComponent> animationMapper;
    ComponentMapper<Hitable> mHitable;

    private Collider tmpCollider = new Collider(0, 0);

    public HitSystem() {
        super(Aspect.getAspectForAll(Hitable.class, PositionComponent.class, Collider.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        AttackComponent attack1;
        PositionComponent position1, position2;
        Collider collider2;
        FacingComponent facing;
        Hitable hitable;

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);

            hitable = mHitable.get(e);
            if (hitable.state >= 0) {
                hitable.state += world.getDelta();
            }
            if (hitable.state > hitable.hitTimeout) {
                hitable.state = -1f;
            }

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
                    hitable = mHitable.get(e2);
                    if (hitable.state >= 0) {
                        continue;
                    }

                    position2 = pm.get(e2);
                    collider2 = cm.get(e2);

                    if (Utils.collide(position1, tmpCollider, position2, collider2, addX1, addY1)) {
                        hitable.state = 0;
                        HealthComponent health = hm.getSafe(e2);
                        if (health != null) {
                            float damage = attack1.dps * Gdx.graphics.getDeltaTime();
                            health = hm.get(e2);
                            health.health -= damage;
                        }

                        AnimationComponent ac = animationMapper.getSafe(e2);
                        if (ac != null && ac.hasAnimation("hit")) {
                            ac.setAnimation("hit");
                        }

                        Orb orb = om.getSafe(e2);
                        if (orb != null) {
                            mapSystem.spikeState = !mapSystem.spikeState;

                            if (mapSystem.screenX == 0 && mapSystem.screenY == -2) {
                                mapSystem.win();
                            }
                        }

                        if (e2.getComponent(Blink.class) == null) {
                            e2.addComponent(new Blink(Color.RED.toFloatBits(), 3, .25f));
                            e2.changedInWorld();
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
