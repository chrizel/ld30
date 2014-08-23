package com.chrizel.ld30;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.chrizel.ld30.components.ColliderComponent;
import com.chrizel.ld30.components.PositionComponent;

public class Utils {

    public static boolean collide(PositionComponent position1,
                                   ColliderComponent collider1,
                                   PositionComponent position2,
                                   ColliderComponent collider2,
                                   float addX1,
                                   float addY1) {

        float padX = (16f - collider1.width) / 2;
        float padY = (16f - collider1.height) / 2;

        Rectangle.tmp.x = position1.x + padX + addX1;
        Rectangle.tmp.y = position1.y + padY + addY1;
        Rectangle.tmp.width = collider1.width;
        Rectangle.tmp.height = collider1.height;

        padX = (16f - collider2.width) / 2;
        padY = (16f - collider2.height) / 2;

        Rectangle.tmp2.x = position2.x + padX;
        Rectangle.tmp2.y = position2.y + padY;
        Rectangle.tmp2.width = collider2.width;
        Rectangle.tmp2.height = collider2.height;

        return Intersector.overlaps(Rectangle.tmp, Rectangle.tmp2);
    }

}
