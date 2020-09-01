package com.fatec.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fatec.gameScreen.GameScreen;

public class LoadingScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;
    private static final float PROGRESSION_BAR_WIDTH = 100;
    private static final float PROGRESSION_BAR_HEIGHT = 25;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private float progress = 0;
    private final GravityGame gravityGame;

    public LoadingScreen(GravityGame gravityGame){
        this.gravityGame = gravityGame;
    }



    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show(){
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH/2, WORLD_HEIGHT /2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        gravityGame.getAssetManager().load("level.tmx", TiledMap.class);
        gravityGame.getAssetManager().load("player.png", Texture.class);
    }

    @Override
    public void render(float delta){
        update();
        clearScreen();
        draw();
    }

    @Override
    public void dispose(){
        shapeRenderer.dispose();
    }

    private void update() {
        if (gravityGame.getAssetManager().update()){
            gravityGame.setScreen(new GameScreen(gravityGame));
        }else{
            progress = gravityGame.getAssetManager().getProgress();
        }
    }

    private void clearScreen(){
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw(){
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(WORLD_WIDTH - PROGRESSION_BAR_WIDTH /2, WORLD_HEIGHT /2 - PROGRESSION_BAR_HEIGHT /2, progress * PROGRESSION_BAR_WIDTH, PROGRESSION_BAR_HEIGHT);
        shapeRenderer.end();
    }

}
