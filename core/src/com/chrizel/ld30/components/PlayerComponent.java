package com.chrizel.ld30.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent extends Component {
    public float speed;

    public PlayerComponent(float speed) {
        this.speed = speed;
    }
}
