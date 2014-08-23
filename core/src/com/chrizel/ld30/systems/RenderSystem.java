package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.chrizel.ld30.components.DrawableComponent;
import com.chrizel.ld30.components.PositionComponent;

@Wire
public class RenderSystem extends EntitySystem implements Disposable {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<DrawableComponent> dm;

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

            batch.draw(drawableComponent.region, positionComponent.x, positionComponent.y);
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
