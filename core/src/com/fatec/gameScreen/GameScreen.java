package com.fatec.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fatec.characters.Player;
import com.fatec.game.GravityGame;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 640;
    private static final float WORLD_HEIGHT = 480;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private final GravityGame gravityGame;
    private TiledMap tiledMap;
    private Texture playerTexture;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Player player;


    public GameScreen(GravityGame gravityGame){
        this.gravityGame = gravityGame;
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera() ;
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        tiledMap = gravityGame.getAssetManager().get("level.tmx");
        playerTexture = gravityGame.getAssetManager().get("player.png");
        player = new Player(playerTexture);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView((OrthographicCamera) camera);
    }


    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
        drawDebug();
    }
    private void update(float delta) {
        player.update(delta);
        stopPlayerLeavingTheScreen();
    }
    private void clearScreen() {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw(){
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();
        batch.begin();
        player.draw(batch);
        batch.end();
    }
    private void drawDebug(){
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        player.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }
    private void stopPlayerLeavingTheScreen() {
        if (player.getY() < 0){
            player.setPosition(player.getX(), 0);
            player.landed();
        }
        if (player.getX() < 0){
            player.setPosition(0, player.getY());
        }
        if(player.getX() + player.WIDTH > WORLD_WIDTH){
            player.setPosition(WORLD_WIDTH - player.WIDTH, player.getY());
        }
    }
}
