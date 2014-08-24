package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.chrizel.ld30.components.Drawable;
import com.chrizel.ld30.components.FacingComponent;
import com.chrizel.ld30.components.PositionComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

@Wire
public class RenderSystem extends EntitySystem {
    protected MapSystem mapSystem;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private boolean dirty = true;

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<Drawable> dm;
    private ComponentMapper<FacingComponent> fm;
    private ArrayList<Entity> sortedEntities;

    public RenderSystem(OrthographicCamera camera) {
        super(Aspect.getAspectForAll(PositionComponent.class, Drawable.class));
        batch = new SpriteBatch();
        this.camera = camera;
        sortedEntities = new ArrayList<Entity>(128);
    }

    @Override
    protected void removed(Entity e) {
        super.removed(e);
        dirty = true;
    }

    @Override
    protected void inserted(Entity e) {
        super.inserted(e);
        dirty = true;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        PositionComponent positionComponent;
        Drawable drawable;

        if (dirty) {
            dirty = false;
            sortedEntities.clear();
            for (int i = 0; i < entities.size(); ++i) {
                sortedEntities.add(entities.get(i));
            }
            sortedEntities.sort(new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    float z1 = pm.get(e1).z, z2 = pm.get(e2).z;
                    return (z1 == z2) ? 0 : (z1 < z2 ? -1 : +1);
                }
            });
        }

        camera.update();

        batch.begin();
        batch.setProjectionMatrix(camera.combined);

        for (int i = 0; i < sortedEntities.size(); ++i) {
            Entity e = sortedEntities.get(i);

            positionComponent = pm.get(e);
            drawable = dm.get(e);

            TextureRegion region = drawable.region;
            if (region == null) {
                continue;
            }

            float rotation = 0.0f;
            float x = positionComponent.x;
            float y = positionComponent.y;

            FacingComponent facing = fm.getSafe(e);
            if (facing != null) {
                rotation = facing.getRotation();
            }

            if (mapSystem.globalTintActive) {
                batch.setColor(mapSystem.globalTint);
            } else {
                batch.setColor(drawable.tint);
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
