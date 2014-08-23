package com.chrizel.ld30.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Disposable;

public class MapComponent extends Component implements Disposable {
    public Pixmap pixmap;

    public MapComponent(String mapName) {
        pixmap = new Pixmap(Gdx.files.internal(mapName));
    }

    @Override
    public void dispose() {
        pixmap.dispose();
    }
}
