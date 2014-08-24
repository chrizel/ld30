package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.chrizel.ld30.components.Collider;
import com.chrizel.ld30.components.Drawable;
import com.chrizel.ld30.components.Spike;

@Wire
public class SpikeSystem extends EntityProcessingSystem {
    protected MapSystem mapSystem;
    protected ComponentMapper<Collider> mCollider;
    protected ComponentMapper<Drawable> mDrawable;
    protected ComponentMapper<Spike> mSpike;

    public SpikeSystem() {
        super(Aspect.getAspectForAll(Spike.class, Collider.class, Drawable.class));
    }

    @Override
    protected void process(Entity e) {
        Spike spike = mSpike.get(e);

        if (spike.type == Spike.ORANGE) {
            mCollider.get(e).enabled = mapSystem.spikeState;
            mDrawable.get(e).region = mapSystem.spikeState ? spike.enabled : spike.disabled;
        } else if (spike.type == Spike.BLUE) {
            mCollider.get(e).enabled = !mapSystem.spikeState;
            mDrawable.get(e).region = !mapSystem.spikeState ? spike.enabled : spike.disabled;
        }
    }
}
