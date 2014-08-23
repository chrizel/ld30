package com.chrizel.ld30.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationComponent extends Component {
    private HashMap<String, Animation> animations;
    public String currentAnimation = "";

    public AnimationComponent() {
        animations = new HashMap<String, Animation>(1);
    }

    public AnimationComponent newAnimation(String name, Texture texture, float frameDuration, int frameWidth, int frameHeight, int[] frames) {
        TextureRegion[] regionFrames = new TextureRegion[frames.length];
        for (int i = 0; i < frames.length; i++) {
            regionFrames[i] = new TextureRegion(texture, frameWidth * frames[i], 0, frameWidth, frameHeight);
        }
        animations.put(name, new Animation(frameDuration, regionFrames));
        return this;
    }

    public Animation getAnimation() {
        if (animations.containsKey(currentAnimation)) {
            return animations.get(currentAnimation);
        }

        return null;
    }

    public AnimationComponent setAnimation(String name) {
        this.currentAnimation = name;
        return this;
    }
}
