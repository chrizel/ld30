package com.chrizel.ld30.components;

import com.artemis.Component;

public class Hitable extends Component {
    public float hitTimeout;
    public float state;

    public Hitable(float hitTimeout) {
        this.hitTimeout = hitTimeout;
        this.state = -1;
    }

    public Hitable() {
        this(0);
    }
}
