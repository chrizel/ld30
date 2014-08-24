package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Color;
import com.chrizel.ld30.Utils;
import com.chrizel.ld30.components.Collider;
import com.chrizel.ld30.components.PortalComponent;
import com.chrizel.ld30.components.PositionComponent;

import java.awt.*;

@Wire
public class PortalSystem extends EntityProcessingSystem {
    MapSystem mapSystem;
    ComponentMapper<PositionComponent> pm;
    ComponentMapper<Collider> cm;
    protected ComponentMapper<PortalComponent> mPortalComponent;

    Collider portalCollider = new Collider(16f, 16f);

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
        Collider playerCollider = cm.get(player);
        PortalComponent portalComponent = mPortalComponent.get(e);
        PositionComponent portalPosition = pm.get(e);

        if (Utils.collide(playerPosition, playerCollider, portalPosition, portalCollider, 0, 0)) {
            if (portalComponent.state < 0) {
                portalComponent.state = 0;
                mapSystem.globalTintActive = true;
                mapSystem.globalTintState = 0;
                mapSystem.globalTint = Color.WHITE.toFloatBits();
            }
            portalComponent.state += world.getDelta();

            if (portalComponent.state >= portalComponent.portTime) {
                mapSystem.globalTintActive = false;
                portalComponent.state = -1;
                mapSystem.activeMap = mapSystem.activeMap == 0 ? 1 : 0;
                mapSystem.loadScreen();
            }
        } else if (portalComponent.state >= 0) {
            mapSystem.globalTintActive = false;
            portalComponent.state = -1f;
        }
    }
}
