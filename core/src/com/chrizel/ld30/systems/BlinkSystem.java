package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.chrizel.ld30.components.Blink;
import com.chrizel.ld30.components.Drawable;

@Wire
public class BlinkSystem extends EntityProcessingSystem {
    private ComponentMapper<Drawable> cm;
    private ComponentMapper<Blink> bm;
    private float white;

    public BlinkSystem() {
        super(Aspect.getAspectForAll(Drawable.class, Blink.class));
        white = Color.WHITE.toFloatBits();
    }

    @Override
    protected void process(Entity e) {
        Drawable drawable = cm.get(e);
        Blink blink = bm.get(e);

        if (blink.wholeState >= blink.duration) {
            drawable.tint = white;
            e.removeComponent(blink);
            e.changedInWorld();
        } else if (blink.state == 0) {
            drawable.tint = blink.color;
        } else if (blink.state >= (blink.duration / (blink.times * 2))) {
            blink.state = 0;
            drawable.tint = drawable.tint == white ? blink.color : white;
        }

        blink.wholeState += world.getDelta();
        blink.state += world.getDelta();
    }
}
