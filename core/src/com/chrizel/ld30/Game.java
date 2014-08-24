package com.chrizel.ld30;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.chrizel.ld30.systems.*;

public class Game extends ApplicationAdapter {

    private Stage stage;

    @Override
    public void create() {
        switchStage("menu");
    }

    @Override
    public void resize(int width, int height) {
        stage.resize(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void render() {
        stage.render();
    }

    public void switchStage(String stageName) {
        if (stage != null) {
            stage.dispose();
            Gdx.input.setInputProcessor(null);
        }

        if (stageName == "menu") {
            stage = new MenuStage(this);
        } else if (stageName == "game") {
            stage = new GameStage(this);
        }
    }
}
