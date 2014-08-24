package com.chrizel.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuStage extends Stage {

    SpriteBatch batch;
    Texture texture;
    final Game game;

    public MenuStage(Game _game) {
        this.game = _game;
        batch = new SpriteBatch();
        texture = new Texture(Gdx.files.internal("menu.png"));

        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown (int keycode) {
                game.switchStage("game");
                return true;
            }
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.43f, .43f, .43f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(texture, 0, 0, 640, 480);
        batch.end();
    }

    @Override
    public void dispose() {
    }
}
