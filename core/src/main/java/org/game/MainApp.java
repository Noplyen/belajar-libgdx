package org.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.game.Box2dManager.Box2dBodyBuilder;
import org.game.Box2dManager.FixtureDefBuilder;
import org.game.Entities.Enemy;
import org.game.Entities.Player;

import org.game.Handler.Box2dContactListener;
import org.game.Handler.HeroInputController;

import static org.game.Constant.*;

public class MainApp extends ApplicationAdapter {
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Body playerBody;

    private OrthographicCamera camera;

    private float WIDTH_WINDOW ;
    private float HEIGHT_WINDOW ;


    @Override
    public void create() {
        // ukuran diubah menjadi satuan meter
        WIDTH_WINDOW = (float) Gdx.graphics.getWidth()/PPM;
        HEIGHT_WINDOW = (float) Gdx.graphics.getHeight()/PPM;

        // setting kamera
        camera = new OrthographicCamera();
        // ukuran layar window satuan meter
        camera.setToOrtho(false,WIDTH_WINDOW,HEIGHT_WINDOW);
        // posisi kamera di tengah
        camera.position.set(
            WIDTH_WINDOW/2,
            HEIGHT_WINDOW/2,
            0);

        // init world
        world = new World(new Vector2(0,-9.8f),false);
        // init kontak listener, benturan antar objek
        world.setContactListener(new Box2dContactListener());
        box2DDebugRenderer = new Box2DDebugRenderer();

        Box2dBodyBuilder box2d= new Box2dBodyBuilder(world);

        Player player = new Player("General Osama Ben Laden",(short)10);
        Enemy  enemy  = new Enemy("General Frankfrut",(short) 10);

        // setting maskbit dan categorybits
        Filter playerFilter  = Constant.getPlayerFilter();
        Filter enemyFilter   = Constant.getEnemyFilter();
        Filter groundFilter  = Constant.getGroundFilter();

        // membuat fixturedef
        FixtureDef fixtureDefPlayer =
            new FixtureDefBuilder()
                .filter(playerFilter)
                .polygonShape(10,10)
                .restitution(0.3f)
                .isSensor(false)
                .getFixtureDef();

        FixtureDef fixtureDefEnemy =
            new FixtureDefBuilder()
                .density(5f)
                .filter(enemyFilter)
                .polygonShape(15,15)
                .restitution(0.3f)
                .isSensor(false)
                .getFixtureDef();

        FixtureDef fixtureGround =
            new FixtureDefBuilder()
                .filter(groundFilter)
                .polygonShape(100f,10f)
                .isSensor(false)
                .getFixtureDef();

        box2d.createBody(
            fixtureGround,
            BodyDef.BodyType.StaticBody,
            new Vector2(WIDTH_WINDOW/2,HEIGHT_WINDOW/2),
            null
        );

        playerBody = box2d.createBody(
            fixtureDefPlayer,
            BodyDef.BodyType.DynamicBody,
            new Vector2(WIDTH_WINDOW/2,HEIGHT_WINDOW/2+0.5f),
            player
        );

        Body bodyEnemy = box2d.createBody(
            fixtureDefEnemy,
            BodyDef.BodyType.DynamicBody,
            new Vector2(WIDTH_WINDOW/2+0.5f,HEIGHT_WINDOW/2+0.5f),
            enemy
        );

        // set input handler
        Gdx.input.setInputProcessor(new HeroInputController(playerBody));
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render world
        box2DDebugRenderer.render(world,camera.combined);
        // ini harus ada ketika membuat box2d fungsinya sama seperti render clear
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

    }


    @Override
    public void dispose() {
        world.dispose();
        box2DDebugRenderer.dispose();
    }
}
