package com.chrizel.ld30;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.chrizel.ld30.systems.*;

public class Game extends ApplicationAdapter {

    private World world;

    @Override
    public void create() {
        OrthographicCamera camera = new OrthographicCamera(640, 480);
        camera.position.set(160, 120, 0);
        camera.zoom = 0.5f;
        camera.update();

        world = new World();
        world.setManager(new TagManager());
        world.setManager(new GroupManager());

        world.setSystem(new PlayerInputSystem());
        world.setSystem(new CollisionSystem());
        world.setSystem(new MovementSystem());
        world.setSystem(new AttackAnimationSystem());
        world.setSystem(new EnemyAISystem());
        world.setSystem(new HitSystem());
        world.setSystem(new DeathSystem());
        world.setSystem(new PortalSystem());
        world.setSystem(new MapSystem(camera, "tiles1.png", "map1.png", "map2.png"));
        world.setSystem(new OrbSystem());
        world.setSystem(new SpikeSystem());
        world.setSystem(new EnemySpikeSystem());
        world.setSystem(new BlinkSystem());

        world.setSystem(new AnimationSystem());
        world.setSystem(new RenderSystem(camera));
        world.setSystem(new HUDSystem());
        world.initialize();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        world.dispose();
    }

    @Override
    public void render() {
        world.setDelta(Gdx.graphics.getDeltaTime());
        world.process();
    }
}
