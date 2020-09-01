package com.fatec.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    private static final float MAX_X_SPEED = 2;
    private static final float MAX_Y_SPEED = 2;
    public static final int WIDTH = 42;
    public static final int HEIGHT = 62;
    private final Rectangle collisionRectangle = new Rectangle(0,0 , WIDTH, HEIGHT);
    private float x = 0;
    private float y = 0;
    private float xSpeed = 0;
    private float ySpeed = 0;
    private boolean blockJump = false;
    private float jumpYDistance = 0;
    private static final float MAX_JUMP_DISTANCE = 3 * HEIGHT;
    private float animationTimer = 0;
    private final Animation walking;
    private final TextureRegion standing;
    private final TextureRegion jumpUp;
    private final TextureRegion jumpDown;

    public Player(Texture texture){
        TextureRegion[] regions = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
        walking = new Animation(0.25f, regions[0], regions[1]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[0];
        jumpUp = regions[2];
        jumpDown = regions[3];
    }


    public void update(float delta) {
        animationTimer += delta;
        Input input = Gdx.input;
        if (input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = MAX_X_SPEED;
        }else if (input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -MAX_X_SPEED;
        }else{
            xSpeed =0;
        }
        if (input.isKeyPressed(Input.Keys.UP) && !blockJump){
            ySpeed = MAX_Y_SPEED;
            jumpYDistance += ySpeed;
            blockJump = jumpYDistance > MAX_JUMP_DISTANCE;
        }else{
            ySpeed = -MAX_Y_SPEED;
            blockJump = jumpYDistance > 0;
        }
        x += xSpeed;
        y += ySpeed;
        updateCollisionRectangle();
    }
    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
    }

    public void draw(Batch batch){
        TextureRegion toDraw = standing;
        if (xSpeed != 0){
            toDraw = standing;
        }
        if (ySpeed > 0){
            toDraw = jumpUp;
        }else if (ySpeed < 0){
            toDraw = jumpDown;
        }
        if (xSpeed < 0){
            if (!toDraw.isFlipX()) toDraw.flip(true, false);
        }else if (xSpeed > 0){
            if (toDraw.isFlipX()) toDraw.flip(true, false);
        }
        batch.draw(toDraw, x,y);
    }


    private void updateCollisionRectangle(){
        collisionRectangle.setPosition(x, y);
    }
    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public void landed(){
        blockJump = false;
        jumpYDistance = 0;
        ySpeed = 0;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

}
