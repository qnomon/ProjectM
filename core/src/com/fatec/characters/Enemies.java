package com.fatec.characters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Enemies {
    private static  float x;
    private static float y;
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private Texture sprite;


    public Enemies(float x, float y, Texture sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void update(float delta){

    }

    public void draw(Batch batch){
        batch.draw(sprite, x, y);
    }

}
