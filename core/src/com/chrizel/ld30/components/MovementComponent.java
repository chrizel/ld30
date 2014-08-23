package com.chrizel.ld30.components;

import com.artemis.Component;

public class MovementComponent extends Component {
    public float velocityX;
    public float velocityY;

    public MovementComponent(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public MovementComponent() {
        this.velocityX = 0;
        this.velocityY = 0;
    }
}
