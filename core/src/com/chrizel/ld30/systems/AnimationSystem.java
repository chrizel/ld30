package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.chrizel.ld30.components.AnimationComponent;
import com.chrizel.ld30.components.DrawableComponent;

@Wire
public class AnimationSystem extends EntityProcessingSystem {
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<DrawableComponent> dm;

    public AnimationSystem() {
        super(Aspect.getAspectForAll(AnimationComponent.class, DrawableComponent.class));
    }

    @Override
    protected void process(Entity e) {
        AnimationComponent animationComponent = am.get(e);
        Animation animation = animationComponent.getAnimation();

        if (animation != null) {
            DrawableComponent drawableComponent = dm.get(e);
            animationComponent.setStateTime(animationComponent.getStateTime() + Gdx.graphics.getDeltaTime());
            drawableComponent.region = animation.getKeyFrame(animationComponent.getStateTime(), animationComponent.isLooping());
        }
    }
}
