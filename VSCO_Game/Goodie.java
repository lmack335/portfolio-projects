/**
 * This extends the Component class to create a class
 * of goodie objects that can be added to the player's
 * inventory.
 * 
 * @author ljmack, csantoma
 */
package team66.VSCO_Game;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.component.Component;

import javafx.scene.Node;

public class Goodie extends Component {
	private String goodieName;
	private boolean collectible;
	
	private Node node;
	

    public Goodie(String name, String fileName, boolean collect) {
	    this.goodieName = name;
	    this.collectible = collect;
	    
	    node = FXGL.getAssetLoader().loadTexture(fileName);
    
    }
    
    /**
     * Makes texture attached to node visible
     */
    @Override
	public void onAdded() {
		entity.setViewWithBBox(node);
	}
    
    /**
	 * @return the goodieName
	 */
    public String getGoodieName() {
	    return goodieName;
	}
    
	/**
	 * @return the collectible status
	 */
	public boolean isCollectible() {
	    return collectible;   
    }

}
