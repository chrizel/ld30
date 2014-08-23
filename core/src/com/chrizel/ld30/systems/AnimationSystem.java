package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.AnimationComponent;
import com.chrizel.ld30.components.FacingComponent;
import com.chrizel.ld30.components.PositionComponent;

@Wire
public class AnimationSystem extends EntitySystem {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<AnimationComponent> am;
    private ComponentMapper<FacingComponent> fm;

    public AnimationSystem(OrthographicCamera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, AnimationComponent.class));
        batch = new SpriteBatch();
        this.camera = camera;
    }

    public void dispose() {
        batch.dispose();
    }

    @Override
    protected final void processEntities(ImmutableBag<Entity> entities) {
        PositionComponent positionComponent;
        AnimationComponent animationComponent;

        camera.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0, s = entities.size(); s > i; i++) {
            Entity e = entities.get(i);

            positionComponent = pm.get(e);
            animationComponent = am.get(e);

            Animation animation = animationComponent.getAnimation();

            if (animation != null) {
                animationComponent.setStateTime(animationComponent.getStateTime() + Gdx.graphics.getDeltaTime());
                TextureRegion region = animation.getKeyFrame(animationComponent.getStateTime(), animationComponent.isLooping());
                float rotation = 0.0f;
                float x = positionComponent.x;
                float y = positionComponent.y;

                FacingComponent facing = fm.get(e);
                if (facing != null) {
                    rotation = facing.getRotation();
                }

                batch.draw(region, x, y, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 1.0f, 1.0f, rotation);
            }
        }

        batch.end();
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
