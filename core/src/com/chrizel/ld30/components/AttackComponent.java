package com.chrizel.ld30.components;

import com.artemis.Component;

public class AttackComponent extends Component {
    public String animation;
    public float duration;
    public float dps;
    public boolean isAttacking = false;
    public float attackWidth;
    public float attackHeight;

    public AttackComponent(String animation, float duration, float dps, float attackWidth, float attackHeight) {
        this.animation = animation;
        this.duration = duration;
        this.dps = dps;
        this.attackWidth = attackWidth;
        this.attackHeight = attackHeight;
    }
}
