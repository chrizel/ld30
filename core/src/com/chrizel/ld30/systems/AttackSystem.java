package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.chrizel.ld30.components.AnimationComponent;
import com.chrizel.ld30.components.AttackComponent;

@Wire
public class AttackSystem extends EntityProcessingSystem {
    ComponentMapper<AttackComponent> attackMapper;
    ComponentMapper<AnimationComponent> animationMapper;

    public AttackSystem() {
        super(Aspect.getAspectForAll(AttackComponent.class));
    }

    @Override
    protected void process(Entity e) {
        AttackComponent attack = attackMapper.get(e);
        AnimationComponent animation = animationMapper.get(e);

        if (attack.isAttacking) {
            if (animation.currentAnimation == attack.animation &&
                    animation.getStateTime() >= animation.getAnimation().getAnimationDuration())
            {
                animation.setStateTime(0);
                attack.isAttacking = false;
            }

            animation.setAnimation(attack.isAttacking ? attack.animation : "idle");
        }
    }
}
