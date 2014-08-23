package com.chrizel.ld30.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class AnimationComponent extends Component {
    private HashMap<String, AnimationInfos> animations;
    public String currentAnimation = "";

    private class AnimationInfos {
        public float stateTime = 0.0f;
        public Animation animation = null;
        public boolean looping = false;

        public AnimationInfos(Animation animation, boolean looping) {
            this.animation = animation;
            this.looping = looping;
        }
    }

    public AnimationComponent() {
        animations = new HashMap<String, AnimationInfos>(1);
    }

    public AnimationComponent newAnimation(String name, Texture texture, float frameDuration, boolean looping, int frameWidth, int frameHeight, int[] frames) {
        TextureRegion[] regionFrames = new TextureRegion[frames.length];
        for (int i = 0; i < frames.length; i++) {
            regionFrames[i] = new TextureRegion(texture, frameWidth * frames[i], 0, frameWidth, frameHeight);
        }

        animations.put(name, new AnimationInfos(new Animation(frameDuration, regionFrames), looping));

        return this;
    }

    private AnimationInfos getAnimationInfos() {
        if (animations.containsKey(currentAnimation)) {
            return animations.get(currentAnimation);
        }

        return null;
    }

    public Animation getAnimation() {
        AnimationInfos infos = getAnimationInfos();
        return infos != null ? infos.animation : null;
    }

    public float getStateTime() {
        AnimationInfos infos = getAnimationInfos();
        return infos != null ? infos.stateTime : 0;
    }

    public void setStateTime(float value) {
        AnimationInfos infos = getAnimationInfos();
        if (infos != null) {
            infos.stateTime = value;
        }
    }

    public AnimationComponent setAnimation(String name) {
        this.currentAnimation = name;
        return this;
    }

    public boolean isLooping() {
        AnimationInfos infos = getAnimationInfos();
        return infos != null ? infos.looping : false;
    }
}
