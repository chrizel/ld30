package com.chrizel.ld30;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.chrizel.ld30.systems.*;

public class GameStage extends Stage {
    private final Game game;
    private final World world;
    private final MapSystem mapSystem;

    public GameStage(Game _game) {
        this.game = _game;
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

        mapSystem = new MapSystem(camera, "tiles1.png", "map1.png", "map2.png");
        world.setSystem(mapSystem);
        world.setSystem(new OrbSystem());
        world.setSystem(new SpikeSystem());
        world.setSystem(new EnemySpikeSystem());
        world.setSystem(new BlinkSystem());
        world.setSystem(new HeartSystem());

        world.setSystem(new AnimationSystem());
        world.setSystem(new RenderSystem(camera));
        world.setSystem(new HelpSystem());
        world.setSystem(new HUDSystem());

        world.initialize();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (mapSystem.win || mapSystem.gameOver) {
                    game.switchStage("menu");
                    return true;
                }

                return false;
            }
        });
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
