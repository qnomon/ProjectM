package com.fatec.characters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;


public class Enemies {
    private static  float x;
    private static float y;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static int life = 100;
    private Texture sprite;


    public Enemies(float x, float y, Texture sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
        life = 100;
    }

    public void update(float delta){
       x = x - 100 * Gdx.graphics.getDeltaTime();
    }

    public void draw(Batch batch){
        batch.draw(sprite, x, y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
