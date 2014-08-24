package com.chrizel.ld30.components;

import com.artemis.Component;

public class Collider extends Component {
    public float width;
    public float height;
    public boolean enabled;

    public Collider(float width, float height) {
        this.width = width;
        this.height = height;
        this.enabled = true;
    }
}
