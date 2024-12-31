package org.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Filter;

public class Constant {
    // pixel per meter, satuan Box2d
    public static final float PPM = 100;
    public static final short PLAYER_BITS  = 2;
    public static final short ENEMY_BITS   = 4;
    public static final short GROUND_BITS  = 3;

    /**
     * untuk mendefinisikan data dan collision objek
     *
     * @return
     */
    public static Filter getPlayerFilter(){
        Filter filter = new Filter();
        // init category data dari body, digunakan untuk collision
        // ini mendefinisikan value dari objek body
        filter.categoryBits = PLAYER_BITS;
        // membuat objek mana saja yang dapat berbenturan
        // apabila objek A memiliki categoryBits = 4
        // lalu objek B memiliki filter data = 4
        // maka kedua objek itu bisa melakukan collision, lalu
        // objek lain yang tidak memiliki categoryBits = 4 maka akan
        // tembus begitu saja
        filter.maskBits = GROUND_BITS | ENEMY_BITS;

        return filter;
    }

    public static Filter getEnemyFilter() {
        Filter filter = new Filter();
        filter.categoryBits = ENEMY_BITS;
        filter.maskBits = GROUND_BITS | PLAYER_BITS;

        return filter;

    }

    public static Filter getGroundFilter() {
        Filter filter = new Filter();
        filter.categoryBits = GROUND_BITS;
        filter.maskBits = ENEMY_BITS | PLAYER_BITS;

        return filter;

    }
}
