package com.chrizel.ld30.components;

import com.artemis.Component;

public class PositionComponent extends Component {
    public float x;
    public float y;
    public float z;

    public PositionComponent(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public PositionComponent(float x, float y) {
        this(x, y, 0.0f);
    }

    public PositionComponent() {
        this(0.0f, 0.0f, 0.0f);
    }
}
