package com.chrizel.ld30.components;

import com.artemis.Component;

public class MovementComponent extends Component {
    public float velocityX;
    public float velocityY;
    public float speed;

    public MovementComponent(float velocityX, float velocityY, float speed) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.speed = speed;
    }

    public MovementComponent(float speed) {
        this.velocityX = 0;
        this.velocityY = 0;
        this.speed = speed;
    }
}
