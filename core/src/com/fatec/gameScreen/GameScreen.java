package com.fatec.gameScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fatec.characters.Enemies;
import com.fatec.characters.Player;
import com.fatec.game.Bullet;
import com.fatec.game.GravityGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreen extends ScreenAdapter {
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final float CELL_SIZE = 32f;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Camera camera;
    private SpriteBatch batch;
    private final GravityGame gravityGame;
    private TiledMap tiledMap;
    private Texture playerTexture;
    private Texture bulletTexture;
    private Texture enemyTexture;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Player player;
    public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    private float lastShot = 0;
    private float elapsedTime = 0;
    public GameScreen(GravityGame gravityGame){
        this.gravityGame = gravityGame;
    }
    private Enemies enemy;

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
        bulletTexture = gravityGame.getAssetManager().get("bullet.png");
        enemyTexture = gravityGame.getAssetManager().get("Enemy.png");
        player = new Player(playerTexture);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView((OrthographicCamera) camera);
        enemy = new Enemies(150, 128, enemyTexture);
    }


    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
        drawDebug();

    }
    private void update(float delta) {
        Input input = Gdx.input;
        elapsedTime += Gdx.graphics.getDeltaTime();
        player.update(delta);
        stopPlayerLeavingTheScreen();

        updateShoots(delta);
        if(input.isKeyPressed(Input.Keys.SPACE) && elapsedTime-lastShot > 0.5f){
            bullets.add(new Bullet(player.getX() + player.WIDTH/2, player.getY()+ player.HEIGHT/2, bulletTexture, player.lastDirection));
            lastShot = elapsedTime;
        }
        handlePlayerCollision();
        camera.update();
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
        drawShoots(batch);
        enemy.draw(batch);
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

    private Array<CollisionCell> whichCellsDoesPlayerCover(){
        float x = player.getX();
        float y = player.getY();
        Array<CollisionCell> cellsCovered = new Array<CollisionCell>();
        float cellX = x / CELL_SIZE;
        float cellY = y / CELL_SIZE;

        int bottomLeftCellX = MathUtils.floor(cellX);
        int bottomLeftCellY = MathUtils.floor(cellY);

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);

        cellsCovered.add(new CollisionCell (tiledMapTileLayer.getCell(bottomLeftCellX, bottomLeftCellY), bottomLeftCellX, bottomLeftCellY));

        if (cellX % 1 != 0 && cellY % 1 != 0) {
            int topRightCellX = bottomLeftCellX + 1;
            int topRightCellY = bottomLeftCellY + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topRightCellX, topRightCellY), topRightCellX, topRightCellY));
        }
        if (cellX %1 != 0){
            int bottomRightCellX = bottomLeftCellX + 1;
            int bottomRightCellY = bottomLeftCellY;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomRightCellX, bottomRightCellY), bottomRightCellX, bottomRightCellY));
        }
        if (cellY %1 != 0){
            int topLeftCellX = bottomLeftCellX;
            int topLeftCellY = bottomLeftCellY +1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topLeftCellX, topLeftCellY), topLeftCellX, topLeftCellY));
        }
        return cellsCovered;
    }

    private Array<CollisionCell> filterOutNonTiledCells(Array<CollisionCell> cells){
        for (Iterator<CollisionCell> iter = cells.iterator();
        iter.hasNext();){
            CollisionCell CollisionCell = iter.next() ;
            if (CollisionCell.isEmpty()){
                iter.remove();
            }
        }
        return cells;
    }
    private void handlePlayerCollision() {
        Array<CollisionCell> playerCells = whichCellsDoesPlayerCover();
        playerCells = filterOutNonTiledCells(playerCells);
        for (CollisionCell cell : playerCells) {
            float cellLevelX = cell.cellX * CELL_SIZE;
            float cellLevelY = cell.cellY * CELL_SIZE;
            if (player.getY() < cellLevelY + CELL_SIZE && cellLevelX <= player.getX() && cellLevelX + CELL_SIZE > player.getX()) {
                player.setPosition(player.getX(), cellLevelY + CELL_SIZE);
                player.landed();
            }
            if (player.getX() + player.WIDTH > cellLevelX && player.getY() + player.HEIGHT >= cellLevelY && player.getY()+1 < cellLevelY + CELL_SIZE && player.lastDirection == "Right") {
                player.setPosition(cellLevelX - player.WIDTH, player.getY());
            }else if (player.getX() < cellLevelX + CELL_SIZE && player.getY() + player.HEIGHT >= cellLevelY && player.getY()+1 < cellLevelY + CELL_SIZE && player.lastDirection == "Left") {
                player.setPosition(cellLevelX, player.getY());
            }
        }
    }

    private void updateShoots(float delta){
        for(int i =0; i < bullets.size(); i++){
            Bullet bullet = bullets.get(i);
            bullet.update(delta);
        }
    }

    private void drawShoots(SpriteBatch batch){
        for(int i =0; i < bullets.size(); i++){
            Bullet bullet = bullets.get(i);
            bullet.draw(batch);
        }
    }
}

class CollisionCell{
    final private TiledMapTileLayer.Cell cell;
    public final int cellX;
    public final int cellY;

    public CollisionCell(TiledMapTileLayer.Cell cell, int cellX, int cellY){
        this.cell = cell;
        this.cellX = cellX;
        this.cellY = cellY;
    }
    public boolean isEmpty() {
        return cell == null;
    }
}

