package com.chrizel.ld30.components;

import com.artemis.Component;

public class FacingComponent extends Component {

    public int facing;

    public static final int RIGHT = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int UP = 3;

    public FacingComponent() {
        this.facing = 0;
    }

    public FacingComponent(int facing) {
        this.facing = facing;
    }

    public float getRotation() {
        switch (facing) {
            case RIGHT:
                return 0.0f;
            case DOWN:
                return 270.0f;
            case LEFT:
                return 180.0f;
            case UP:
                return 90.0f;
        }

        return 0;
    }

    public float getScaleX() {
        if (facing == LEFT) {
            return -1.0f;
        }

        return 1.0f;
    }

    public float getScaleY() {
        if (facing == DOWN) {
            return -1.0f;
        }

        return 1.0f;
    }

}
