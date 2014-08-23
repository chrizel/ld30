package com.chrizel.ld30.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.AnimationComponent;
import com.chrizel.ld30.components.FacingComponent;
import com.chrizel.ld30.components.PositionComponent;

public class AnimationSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;

    private SpriteBatch batch;
    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<AnimationComponent> am = ComponentMapper.getFor(AnimationComponent.class);
    private ComponentMapper<FacingComponent> fm = ComponentMapper.getFor(FacingComponent.class);

    public AnimationSystem(OrthographicCamera camera, int priority) {
        super(priority);
        batch = new SpriteBatch();
        this.camera = camera;
    }

    public void dispose() {
        batch.dispose();
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.getFor(PositionComponent.class, AnimationComponent.class));
    }

    @Override
    public void removedFromEngine(Engine engine) {
    }

    @Override
    public void update(float deltaTime) {
        PositionComponent positionComponent;
        AnimationComponent animationComponent;

        camera.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);

            positionComponent = pm.get(e);
            animationComponent = am.get(e);

            Animation animation = animationComponent.getAnimation();

            if (animation != null) {
                animationComponent.setStateTime(animationComponent.getStateTime() + deltaTime);
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
}
