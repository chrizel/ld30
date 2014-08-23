package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.EntityBuilder;
import com.chrizel.ld30.components.CorpseComponent;
import com.chrizel.ld30.components.DrawableComponent;
import com.chrizel.ld30.components.HealthComponent;
import com.chrizel.ld30.components.PositionComponent;

@Wire
public class DeathSystem extends EntityProcessingSystem {
    ComponentMapper<HealthComponent> hm;
    ComponentMapper<CorpseComponent> cm;
    ComponentMapper<PositionComponent> pm;

    public DeathSystem() {
        super(Aspect.getAspectForAll(HealthComponent.class));
    }

    @Override
    protected void process(Entity e) {
        HealthComponent health = hm.get(e);

        if (health.health <= 0) {
            CorpseComponent corpse = cm.getSafe(e);
            PositionComponent position = pm.getSafe(e);
            if (corpse != null && position != null) {
                // Create corpse entity on the position the entity died...
                new EntityBuilder(world)
                        .with(
                                new PositionComponent(position.x, position.y),
                                new DrawableComponent(corpse.corpseTexture)
                        )
                        .group("mapObject")
                        .build();
            }

            e.deleteFromWorld();
        }
    }
}
