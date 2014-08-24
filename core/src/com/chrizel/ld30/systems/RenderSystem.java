package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.DrawableComponent;
import com.chrizel.ld30.components.FacingComponent;
import com.chrizel.ld30.components.PositionComponent;

@Wire
public class RenderSystem extends EntitySystem {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<DrawableComponent> dm;
    private ComponentMapper<FacingComponent> fm;

    public RenderSystem(OrthographicCamera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, DrawableComponent.class));
        batch = new SpriteBatch();
        this.camera = camera;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        PositionComponent positionComponent;
        DrawableComponent drawableComponent;

        camera.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < entities.size(); ++i) {
            Entity e = entities.get(i);

            positionComponent = pm.get(e);
            drawableComponent = dm.get(e);

            TextureRegion region = drawableComponent.region;
            float rotation = 0.0f;
            float x = positionComponent.x;
            float y = positionComponent.y;

            FacingComponent facing = fm.getSafe(e);
            if (facing != null) {
                rotation = facing.getRotation();
            }

            batch.draw(region, x, y, region.getRegionWidth() / 2, region.getRegionHeight() / 2, region.getRegionWidth(), region.getRegionHeight(), 1.0f, 1.0f, rotation);
        }

        batch.end();
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    public void dispose() {
        batch.dispose();
    }
}
