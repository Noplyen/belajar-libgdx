package org.game.Box2dManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.game.Entities.MyGameEntities;

public class Box2dBodyBuilder {
    private World world;
    private Body body;
    private BodyDef bodyDef;

    public Box2dBodyBuilder (World world){
        this.world = world;
        bodyDef    = new BodyDef();
    }


    /**
     * @param fixtureDef
     * @param bodyType
     * @param position
     * @param entities userData value
     * @return
     */
    public Body createBody(FixtureDef fixtureDef,
                           BodyDef.BodyType bodyType,
                           Vector2 position,
                           MyGameEntities entities)
    {
        bodyDef.type = bodyType;
        bodyDef.position.set(position);

        body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.setUserData(entities);

        return body;
    }
}
