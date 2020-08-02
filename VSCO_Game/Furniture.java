/**
 * This extends the Component class to create a class
 * of furniture objects that can be explored to find
 * collectible goodies.
 * 
 * @author ljmack, csantoma
 */
package team66.VSCO_Game;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.*;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.image.*;
import javafx.scene.*;

public class Furniture extends Component {
	private String furnitureName;
	private boolean explorable;
	
	private Node node;
	
	public Furniture(String name, String fileName, boolean explore) {
		this.furnitureName = name;
		this.explorable = explore;
		
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
	 * @return the furnitureName
	 */
	public String getFurnitureName() {
		return furnitureName;
	}

	/**
	 * @return the explorable status
	 */
	public boolean isExplorable() {
		return explorable;
	}

	/**
	 * @param explorable the explorable status to set
	 */
	public void setExplorable(boolean explorable) {
		this.explorable = explorable;
	}
}
