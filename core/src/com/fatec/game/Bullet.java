package com.fatec.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bullet {
    private static final float speed = 300;
    private float x = 0;
    private float y = 0;
    public int WIDTH = 32;
    public int HEIGHT = 9;
    private Texture sprite;
    private String direction = "Right";

    public Bullet(float x, float y, Texture texture, String direction) {
        this.x = x;
        this.y = y;
        sprite = texture;
        this.direction = direction;
    }

    public void update(float delta){
        if(direction == "Right") {
            x += speed * Gdx.graphics.getDeltaTime();
        }
        if(direction == "Left") {
            x -= speed * Gdx.graphics.getDeltaTime();
        }
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
