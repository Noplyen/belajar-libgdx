package org.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


/**
 * ketika anda menggunakan box2d maka anda perlu menggunakan
 * konversi PPM (pixel per meter), kilograms dan second untuk satuan
 * bahkan camera anda perlu diatur ulang mengggunakan PPM
 * **/
public class Main extends ApplicationAdapter {

    private Box2DDebugRenderer box2d;
    private float WIDTH_WINDOW ;
    private float HEIGHT_WINDOW ;
    private World world;
    private final float PPM = 100;
    private OrthographicCamera camera;
    private Body player;

    private final short BIT_A = 2;
    private final short BIT_B = 4;

    short jumpCounter = 0;

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


        // define box2d
        // world didalamnya terdapat body
        // didalam body ada banyak definisi objek seperti posisi dan sebagainya
        // pelengkap daripada objek adalah fixture yakni bentuk fisik objek
        // sehingga urutannya World->Body->Fixture
        world = new World(new Vector2(0,-9.8f),false);
        box2d = new Box2DDebugRenderer();

        groundObjek(Gdx.graphics.getWidth(),10);

        playerObjek(10,10);

        boxObjek(10,10);
    }


    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // render world
        box2d.render(world,camera.combined);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
//        world.setContactListener(new MyContactListner());

        // membuat kamera selalu follow keberadaan player
        camera.position.set(player.getPosition().x,player.getPosition().y,0);
        camera.update();

        playerMove();
    }

    public void playerMove() {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // linear velocity adalah perpindahan objek linear (x)
            player.setLinearVelocity(1.5f, player.getLinearVelocity().y);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.setLinearVelocity(-1.5f, player.getLinearVelocity().y);
        }

        // ketika key up dipencet 1x dan tidak dipencet lebih dari 2x
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && jumpCounter < 2) {

            // maka akan diberikan linear impulse untuk arah y (lompat)
            player.applyLinearImpulse(new Vector2(0, 4), player.getWorldCenter(), false);
            jumpCounter++;
        }

        // apabila player posisi y = 0 atau tepat diatas ground
        // maka reset jumpcounter
        if (player.getLinearVelocity().y==0){
            jumpCounter = 0;
        }

    }

    @Override
    public void dispose() {
        world.dispose();
        box2d.dispose();


        super.dispose();
    }

    public void groundObjek(int widthShape, int heightShape){
        BodyDef bodyDef       = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(widthShape/PPM,heightShape/PPM);

        fixtureDef.shape = polygonShape;
        // permukaan/gesekan dengan objek lain
        // untuk efek licin 0.0f
        fixtureDef.friction =1f;

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(WIDTH_WINDOW/2,HEIGHT_WINDOW/2);

        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        // digunakan untuk menberikan inisialisasi nama pada body
        // ini berguna saat collision berlangsung semisalnya player attack enemy
        fixture.setUserData("ground");

        Filter filter = new Filter();
        filter.categoryBits = BIT_B;
        filter.maskBits = BIT_B|BIT_A;


        fixture.setFilterData(filter);
    }

    public void playerObjek(int widthShape, int heightShape){
        BodyDef bodyDef       = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(widthShape/PPM,heightShape/PPM);

        fixtureDef.shape = polygonShape;

        // efek mantul
        // jika diberikan nilai 1 maka akan terus mantul
        // nilai 0 tidak terjadi pantulan
        // niali <0 ex = 0.2 maka pantulan akan terus berkurang dan berhenti
        fixtureDef.restitution=0.2f;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(WIDTH_WINDOW/2,HEIGHT_WINDOW/2+2f);

        player = world.createBody(bodyDef);

        Fixture fixture = player.createFixture(fixtureDef);
        fixture.setUserData("player");

        // untuk mendefinisikan data dan collision objek
        Filter filter = new Filter();
        // init category data dari body, digunakan untuk collision
        filter.categoryBits = BIT_B;
        // membuat objek mana saja yang dapat berbenturan
        // apabila objek A memiliki categoryBits = 4
        // lalu objek B memiliki filter data = 4
        // maka kedua objek itu bisa melakukan collision, lalu
        // objek lain yang tidak memiliki categoryBits = 4 maka akan
        // tembus begitu saja
        fixture.setFilterData(filter);
    }

    public void boxObjek(int widthShape, int heightShape){
        BodyDef bodyDef       = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(widthShape/PPM,heightShape/PPM);

        fixtureDef.shape = polygonShape;
        // massa dari fixture yang nantinya akan menjadi value massa object
        // berpengaruh pada gravitasi world
        // efek :
        //Mempengaruhi momentum dan tumbukan
        //Mempengaruhi respon terhadap gaya
        //Mempengaruhi stabilitas simulasi
        fixtureDef.density = 1f;
        // jika true maka objek tidak terjadi tubrukan atau bersentuhan
        // dengan objek lain, ini memungkinkan falldown/jatuh
        fixtureDef.isSensor= false;

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(WIDTH_WINDOW/2+0.4f,HEIGHT_WINDOW/2+2f);

        Body box = world.createBody(bodyDef);
        Fixture fixture = box.createFixture(fixtureDef);
        fixture.setUserData("box");

        Filter filter = new Filter();
        filter.categoryBits = BIT_A;
        fixture.setFilterData(filter);
    }
}
