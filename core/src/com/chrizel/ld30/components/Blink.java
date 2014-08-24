package com.chrizel.ld30.components;

import com.artemis.Component;

public class Blink extends Component {
    public float color;
    public int times;
    public float duration;

    public float wholeState;
    public float state;

    public Blink(float color, int times, float duration) {
        this.color = color;
        this.times = times;
        this.duration = duration;

        this.wholeState = 0;
        this.state = 0;
    }
}
