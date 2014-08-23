package com.chrizel.ld30.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.chrizel.ld30.components.AnimationComponent;
import com.chrizel.ld30.components.AttackComponent;

public class AttackSystem extends IteratingSystem {
    ComponentMapper<AttackComponent> attackMapper = ComponentMapper.getFor(AttackComponent.class);
    ComponentMapper<AnimationComponent> animationMapper = ComponentMapper.getFor(AnimationComponent.class);

    public AttackSystem(int priority) {
        super(Family.getFor(AttackComponent.class), priority);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        AttackComponent attack = attackMapper.get(entity);
        AnimationComponent animation = animationMapper.get(entity);

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
