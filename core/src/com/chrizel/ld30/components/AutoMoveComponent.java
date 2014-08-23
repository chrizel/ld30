package com.chrizel.ld30.components;

import com.artemis.Component;

public class AutoMoveComponent extends Component {
    public float width;
    public float height;
    public float resetTime;

    public boolean inProgress = false;
    public float nextX = 0;
    public float nextY = 0;
    public float resetState = 0;

    public AutoMoveComponent(float width, float height, float resetTime) {
        this.width = width;
        this.height = height;
        this.resetTime = resetTime;
        this.resetState = resetTime;
    }
}
