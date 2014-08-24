package com.chrizel.ld30.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.chrizel.ld30.components.HealthComponent;
import com.chrizel.ld30.components.PlayerComponent;

@Wire
public class HUDSystem extends VoidEntitySystem {
    private MapSystem mapSystem;
    private ComponentMapper<HealthComponent> hm;

    private final BitmapFont font;
    private final SpriteBatch batch;
    private final Texture darkPixel;
    private final Pixmap darkPixelPixmap;

    public HUDSystem() {
        super();
        font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
        batch = new SpriteBatch();

        darkPixelPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        darkPixelPixmap.setColor(0.0f, 0.0f, 0.0f, 0.5f);
        darkPixelPixmap.fill();
        darkPixel = new Texture(darkPixelPixmap, false);
    }

    @Override
    protected void processSystem() {
        TagManager tagManager = world.getManager(TagManager.class);

        int health = 0;
        if (tagManager.isRegistered("player")) {
            Entity player = tagManager.getEntity("player");
            HealthComponent healthComponent = hm.get(player);
            health = Math.max(0, Math.round(healthComponent.health));
        }

        batch.begin();

        //batch.draw(darkPixel, 0, 480f - 32f, 128f, 32f);
        font.draw(batch, "Health: " + health, 5f, 480f - 4f);

        if (health <= 0) {
            String gameOver = "Game Over";
            batch.draw(darkPixel, 0, 0, 640f, 480f);
            font.setScale(2.0f);
            font.draw(batch, gameOver, 320f - font.getBounds(gameOver).width / 2, 260f);
            font.setScale(1.0f);
            mapSystem.gameOver = true;
        } else if (mapSystem.win) {
            String gameOver = "The End";
            batch.draw(darkPixel, 0, 0, 640f, 480f);
            font.setScale(2.0f);
            font.draw(batch, gameOver, 320f - font.getBounds(gameOver).width / 2, 260f);
            font.setScale(1.0f);
        }

        batch.end();
    }

    @Override
    protected void dispose() {
        super.dispose();
        font.dispose();
        darkPixel.dispose();
        darkPixelPixmap.dispose();
    }
}
