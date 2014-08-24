package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.chrizel.ld30.Utils;
import com.chrizel.ld30.components.ColliderComponent;
import com.chrizel.ld30.components.PortalComponent;
import com.chrizel.ld30.components.PositionComponent;

@Wire
public class PortalSystem extends EntityProcessingSystem {
    MapSystem mapSystem;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<ColliderComponent> cm;

    ColliderComponent portalCollider = new ColliderComponent(16f, 16f);

    public PortalSystem() {
        super(Aspect.getAspectForAll(PortalComponent.class, PositionComponent.class));
    }

    @Override
    protected void process(Entity e) {
        TagManager tagManager = world.getManager(TagManager.class);
        if (!tagManager.isRegistered("player")) {
            return;
        }

        Entity player = tagManager.getEntity("player");
        PositionComponent playerPosition = pm.get(player);
        ColliderComponent playerCollider = cm.get(player);

        PositionComponent portalPosition = pm.get(e);

        if (Utils.collide(playerPosition, playerCollider, portalPosition, portalCollider, 0, 0)) {
            mapSystem.activeMap = mapSystem.activeMap == 0 ? 1 : 0;
            mapSystem.loadScreen();
        }
    }
}
