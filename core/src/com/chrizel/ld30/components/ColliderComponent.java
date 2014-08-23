package com.chrizel.ld30.components;

import com.artemis.Component;

public class ColliderComponent extends Component {
    public float width;
    public float height;

    public ColliderComponent(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
