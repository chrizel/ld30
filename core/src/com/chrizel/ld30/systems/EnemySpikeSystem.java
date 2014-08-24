package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.chrizel.ld30.components.Collider;
import com.chrizel.ld30.components.Drawable;
import com.chrizel.ld30.components.Spike;

@Wire
public class EnemySpikeSystem extends EntitySystem {
    protected ComponentMapper<Collider> mCollider;
    protected ComponentMapper<Drawable> mDrawable;
    protected ComponentMapper<Spike> mSpike;

    public EnemySpikeSystem() {
        super(Aspect.getAspectForAll(Spike.class, Collider.class, Drawable.class));
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        boolean state = !world.getManager(GroupManager.class).getEntities("enemy").isEmpty();
        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);
            Spike spike = mSpike.get(e);

            if (spike.type == Spike.GREEN) {
                mCollider.get(e).enabled = state;
                mDrawable.get(e).region = state ? spike.enabled : spike.disabled;
            }
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
