package org.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Setters.cameraRight;

/***
 * INGAT Libgdx tidak menggunakan koordinat kartesius dimana titik 0,0 ditengah
 * tetapi titik 0,0 ada di pojok bawah kiri untuk libgdx
 * itu kenapa kita selalu membagi dengan 2 dari ukuran window
 * */
public class Main extends ApplicationAdapter {

    // merender map
    private OrthogonalTiledMapRenderer mapRenderer;
    // mengambil/load file map
    private TiledMap tiledMap;
    // camera layar, bagaimana melihat gambar pada layar
    private OrthographicCamera camera;
    // pengaturan proyeksi/perpekstif gambar pada ukuran layar tertentu (desktop, mobile dan lainnya)
    private Viewport viewport;
    private final float HEIGHT_TILE_MAP = 368;
    private final float WIDTH_TILE_MAP = 992;

    @Override
    public void create() {
        camera   = new OrthographicCamera();
        // inisialisasi viewport, mengatur ukuran tile map
        // yang akan digambar pada layar window
        // karena tile map biasanya lebih luas daripada window
        // disini window berukuran 640 x 360
        // sedangkan map berukuran 992 x 368
        viewport = new FitViewport(
            (float)Gdx.graphics.getWidth(),
            (float) Gdx.graphics.getHeight()
            ,camera);

       // apabila kamu ingin manual tanpa menggunakan viewport maka gunakan setToOrtho
//        camera.setToOrtho(false,
//            (float)Gdx.graphics.getWidth(),
//            (float) Gdx.graphics.getHeight());

        // posisi kamera pada viewport
        // kenapa dibagi 2, agar kamera mengambil gambar berada di tengah
        // dari ukuran layar 640 x 360, sehingga camera ada di 320 x 180
        // nilai z = 0 karena kita menggunakan 2d bukan 3d
        // titik 0,0 libgdx berada di pojok kiri bawah sehingga
        // agar kamera ditengah kita perlu membagi ukuran layar dengan 2
        camera.position.set(
            (float) Gdx.graphics.getWidth()/2,
            (float) Gdx.graphics.getHeight()/2,
            0);


        tiledMap = new TmxMapLoader().load("map/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

    @Override
    public void render() {
        // Clear the screen with a blue color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        scrollMapHandler(Gdx.graphics.getDeltaTime());

        // kamera berada ditengah sehingga menggunakan /2

        float startX = camera.viewportWidth/2;
        float startY = camera.viewportHeight/2;

        boundaryCameraPreviewMap(camera,startX,startY,WIDTH_TILE_MAP,HEIGHT_TILE_MAP);
    }

    public void scrollMapHandler(float deltaTime){
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.position.x += 100 * deltaTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.position.x -= 100 * deltaTime;
        }if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.position.y += 100 * deltaTime;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.position.y -= 100 * deltaTime;
        }

        camera.update();
    }

    // agar kamera hanya merender seukuran tile map
    public void boundaryCameraPreviewMap(Camera camera,
//                                         startX dan Y merupakan nilai setengah dari ukuran window
//                                         posisi kamera ditengah
                                         float startX,
                                         float startY,
                                         float widthMap,
                                         float heightMap) {
        // Batas posisi kamera pada peta
        // kenapa perlu dikurangi nilai startX atau 360 setengah dari ukuran layar 640
        // karena kamera berada ditengah sehingga ukuran map perlu dikurangi
        // map 900+ kamera berada ditengah layar yang ukurannya 640
        float maxX = widthMap - startX;
        float maxY = heightMap - startY;

        // MathUtils.clamp membatasi area kamera pada rentan posisi tertentu
        camera.position.x = MathUtils.clamp(camera.position.x, startX, maxX);
        camera.position.y = MathUtils.clamp(camera.position.y, startY, maxY);

        // Perbarui kamera
        camera.update();
    }


    @Override
    public void resize(int width, int height) {
        // akan update viewport layar ketika diperbesar maupaun diperkecil
        viewport.update(width,height);
    }

    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }
}
