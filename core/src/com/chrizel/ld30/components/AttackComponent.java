package com.chrizel.ld30.components;

import com.badlogic.ashley.core.Component;

public class AttackComponent extends Component {
    public String animation;
    public boolean isAttacking = false;

    public AttackComponent(String animation) {
        this.animation = animation;
    }
}
