package org.game.Handler;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class HeroInputController extends InputAdapter{

    private Body body;
    private short counterJump = 0;

    public HeroInputController(Body body) {
        this.body = body;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT){
            // linear velocity adalah perpindahan objek linear (x)
            body.setLinearVelocity(-1f, body.getLinearVelocity().y);

            System.out.println("key left touched");
        }

        if (keycode == Input.Keys.RIGHT){
            body.setLinearVelocity(1f, body.getLinearVelocity().y);

            System.out.println("key right touched");
        }

        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.UP && counterJump<2){
            // maka akan diberikan linear impulse untuk arah y (lompat)
            body.applyLinearImpulse(new Vector2(0, 4), body.getWorldCenter(), false);
            counterJump++;

            System.out.println("key up touched");
        }

        // apabila player posisi y = 0 atau tepat diatas ground
        // maka reset jumpcounter
        if (body.getLinearVelocity().y==0){
            counterJump = 0;
        }

        if (keycode == Input.Keys.DOWN){
            System.out.println("key down touched");
        }
        return true;
    }
}
