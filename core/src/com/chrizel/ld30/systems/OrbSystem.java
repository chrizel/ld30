package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.chrizel.ld30.components.Drawable;
import com.chrizel.ld30.components.Orb;
import com.chrizel.ld30.components.Spike;

@Wire
public class OrbSystem extends EntityProcessingSystem {
    protected MapSystem mapSystem;
    protected ComponentMapper<Orb> mOrb;
    protected ComponentMapper<Drawable> mDrawable;

    public OrbSystem() {
        super(Aspect.getAspectForAll(Orb.class));
    }

    @Override
    protected void process(Entity e) {
        Orb orb = mOrb.get(e);
        mDrawable.get(e).region = mapSystem.spikeState ? orb.enabled : orb.disabled;        
    }
}
