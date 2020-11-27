
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
    public static final int HEIGHT = 61;
    private final Rectangle collisionRectangle = new Rectangle(0,0 , WIDTH/2, HEIGHT);
    private float x = 50;
    private float y = 150;
    private float xSpeed = 0;
    private float ySpeed = 0;
    public String lastDirection = "Right";
    private boolean blockJump = false;
    private float jumpYDistance = 0;
    private static final float MAX_JUMP_DISTANCE = 2 * HEIGHT;
    private float gravity = 0;
    private float weight = -800;
    private float animationTimer = 0;
    private final Animation walking;
    private final TextureRegion standing;
    private final TextureRegion jumpUp;
    private final TextureRegion jumpDown;



    public Player(Texture texture){
        TextureRegion[] regions = TextureRegion.split(texture, WIDTH, HEIGHT)[0];
        walking = new Animation(0.18f,regions[4], regions[5],regions[6], regions[7]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[0];
        jumpUp = regions[7];
        jumpDown = regions[0];

    }


    public void update(float delta) {
        animationTimer += Gdx.graphics.getDeltaTime();
        Input input = Gdx.input;
        if (input.isKeyPressed(Input.Keys.RIGHT)) {
            xSpeed = MAX_X_SPEED;
        }else if (input.isKeyPressed(Input.Keys.LEFT)) {
            xSpeed = -MAX_X_SPEED;
        }else{
            xSpeed =0;
        }
        if (input.isKeyPressed(Input.Keys.UP) && !blockJump){
            //ySpeed = 400 * Gdx.graphics.getDeltaTime();
            //jumpYDistance += ySpeed;
            this.gravity =  500;
            blockJump = true;
        }else{
            ySpeed = -MAX_Y_SPEED;
        }
        x += xSpeed;
        //y += ySpeed;
        this.gravity = this.gravity + this.weight * Gdx.graphics.getDeltaTime();
        this.y = this.y + this.gravity * Gdx.graphics.getDeltaTime();
        updateCollisionRectangle();
    }
    public void drawDebug(ShapeRenderer shapeRenderer){
        shapeRenderer.rect(collisionRectangle.x, collisionRectangle.y, collisionRectangle.width, collisionRectangle.height);
    }

    public void draw(Batch batch){
        TextureRegion toDraw = standing;
        if (xSpeed != 0){
            toDraw = (TextureRegion) walking.getKeyFrame(animationTimer);
        }
        if (xSpeed < 0){
            if (!toDraw.isFlipX()) toDraw.flip(true, false);
            lastDirection = "Left";
        }else if (xSpeed > 0){
            if (toDraw.isFlipX()) toDraw.flip(true, false);
            lastDirection = "Right";
        }
        batch.draw(toDraw, x,y);
    }


    private void updateCollisionRectangle(){
        collisionRectangle.setPosition(x+WIDTH/4, y);
    }
    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public void landed(){
        blockJump = false;
        gravity = 0;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public Rectangle getCollisionRectangle(){
        return collisionRectangle;
    }

}