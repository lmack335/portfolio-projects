/**
 * This class defines the movement and animation sequences for the 
 * player's VSCO Girl character
 * 
 * @author ljmack, csantoma
 */

package team66.VSCO_Game;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

//revised to match what is called in VSCO_Game
public class Player extends Component {
    private int speed = 0;
    private Inventory inventory;
    
    private AnimatedTexture texture;
	private AnimationChannel animIdle, animWalkDown, animWalkLeft, animWalkRight, animWalkUp;
    
    protected Player() {
        this.inventory = new Inventory();
        
        //sets up animation channels -- each row is an animation sequence for directional walking
        animIdle = new AnimationChannel("vsco_walking.png", 4, 41, 43, Duration.seconds(1), 0, 0);
        animWalkDown = new AnimationChannel("vsco_walking.png", 4, 41, 43, Duration.seconds(1), 0, 3);
        animWalkLeft = new AnimationChannel("vsco_walking.png", 4, 41, 43, Duration.seconds(1), 4, 7);
        animWalkRight = new AnimationChannel("vsco_walking.png", 4, 41, 43, Duration.seconds(1), 8, 11);
        animWalkUp = new AnimationChannel("vsco_walking.png", 4, 41, 43, Duration.seconds(1), 12, 15);
		
        texture = new AnimatedTexture(animIdle);
    }
    
    @Override
	public void onAdded() {
		entity.setView(texture);
	}
    
    @Override
    public void onUpdate(double tpf) {
        entity.translateX(speed * tpf);

        if (isMoving()) {

            if (texture.getAnimationChannel() == animIdle) {
                texture.loopAnimationChannel(animWalkRight);
            }

            speed = (int) (speed * 0.9);

            if (FXGLMath.abs(speed) < 1) {
                speed = 0;
                texture.loopAnimationChannel(animIdle);
            }
        }
    }
    
    //return whether player is moving
    private boolean isMoving() {
	return speed != 0;
    }
    
    protected void moveRight() {
	if (canMove(new Point2D(40, 0))) {
	    texture.loopAnimationChannel(animWalkRight);
	    getEntity().translateX(5); // move right 5 pixels
	}
    }
    
    protected void moveLeft() {
	if (canMove(new Point2D(-10, 0))) {
	    texture.loopAnimationChannel(animWalkLeft);
	    getEntity().translateX(-5); // move right 5 pixels
	}
    }
    
    protected void moveUp() {
	if (canMove(new Point2D(0, -10))) {
	    texture.loopAnimationChannel(animWalkUp);
	    getEntity().translateY(-5); // move right 5 pixels
	}
    }
    
    protected void moveDown() {
	if (canMove(new Point2D(0, 100))) {
	texture.loopAnimationChannel(animWalkDown);
        getEntity().translateY(5); // move right 5 pixels
	}
    }
    
    private boolean canMove(Point2D direction) {
        Point2D newPosition = getEntity().getPosition().add(direction);
        
        return FXGL.getGameScene()
                .getViewport()
                .getVisibleArea()
                .contains(newPosition);
    }
    
    protected void addToInventory(Goodie g) {
    	inventory.addToInventory(g);
    }
    
    
    /**
	 * @return the inventory
	 */
	public Inventory getInventory() {
		return inventory;
	}

	protected void takePhoto() {
	//end game by taking photo with goodies.  Present photo as image to end game
        Input input = FXGL.getInput();

	input.addAction(new UserAction("Take Photo") {
            @Override
            protected void onAction() { 
        	//Open an object in the room
            	
        	}
    	}, KeyCode.ENTER);
    }

    
  //when player finds a goodie, make a sound
    protected void found() {
    	FXGL.getAudioPlayer().playSound("drop.wav");
    }

}
