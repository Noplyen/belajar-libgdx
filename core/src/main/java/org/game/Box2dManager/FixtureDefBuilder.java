package org.game.Box2dManager;

import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static org.game.Constant.*;


public class FixtureDefBuilder {
    private FixtureDef fixtureDef;

    public FixtureDefBuilder (){
        this.fixtureDef = new FixtureDef();
    }

    /**
     * Massa/berat dari objek
     * efek :
     * - Mempengaruhi momentum jatuh dan tumbukan
     * - Mempengaruhi respon terhadap gaya
     * @param valueMass semakin besar semakin berat
     * @return FixtureDefBuilder
     */
    public FixtureDefBuilder density(float valueMass){
        // massa dari fixture yang nantinya akan menjadi value massa object
        // efek :
        //Mempengaruhi momentum dan tumbukan
        //Mempengaruhi respon terhadap gaya
        //Mempengaruhi stabilitas simulasi
        fixtureDef.density = valueMass;
        return this;
    }

    /**
     * apakah objek dapat bertubrukan/bersentuhan dengan objek lain
     *
     * @param isSensor true maka objek tidak akan memiliki collision atau bersentuhan,
     *                false adalah nilai default objek dapat bersentuhan seperti player dengan ground
     * @return FixtureDefBuilder
     */
    public FixtureDefBuilder isSensor(boolean isSensor){
            fixtureDef.isSensor = isSensor;
        return this;
    }

    public FixtureDefBuilder polygonShape(float width,float height){
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/PPM,height/PPM);
        fixtureDef.shape   = shape;

        return this;
    }

    public FixtureDefBuilder circleShape(float radius){
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        fixtureDef.shape  = shape;

        return this;
    }

    /**
     * Efek licin atau gesekan dengan objek lain.
     * Jika `true`, maka permukaan fixture akan licin (friction = 0).
     * Jika `false`, maka permukaan tidak licin (friction = 1).
     *
     * @param isfriction true untuk licin, false untuk tidak licin
     * @return FixtureDefBuilder
     */
    public FixtureDefBuilder friction(boolean isfriction){
        if(isfriction){
            fixtureDef.friction  = 0;
        }else {
            fixtureDef.friction = 1;
        }

        return this;
    }

    /**
     * efek mantul objek, apabila value 1 maka akan terus mantul tanpa berhenti
     * apabila 0 maka tidak ada efek pantulan dan dibawahnya (0.2f) maka
     * akan mantul dan perlahan berhenti
     * @param value 1 | 0 | <0
     * @return FixtureDefBuilder
     */
    public FixtureDefBuilder restitution(float value){
        fixtureDef.restitution = value;
        return this;
    }

    public FixtureDefBuilder filter(Filter filter){
        fixtureDef.filter.set(filter);
        return this;
    }

    public FixtureDef getFixtureDef(){
        return fixtureDef;
    }
}
