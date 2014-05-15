package ch.hsr.i.jvector.interfaces;

import javax.media.j3d.Shape3D;

import ch.hsr.i.jvector.components.Component3D.ComponentType;
import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;

public interface Component {
	
	public ComponentType getType();
	
	/**
	 * Returns the Shape3D object of this component
	 * 
	 * @param the type of appearance you wish this shape to set to
	 * @return the shape object with the desired appearance
	 */
	public Shape3D getShape(AppearanceType type);
	
	/**
	 * Returns the current AppearanceType associated with this shape.
	 * @return the current AppearanceType
	 */
	public AppearanceType getAppearanceType();
	
	/**
	 * Sets the appearance of this component to the specified type.
	 * 
	 * @param the type of appearance you wish to set
	 */
	public void setAppearance(AppearanceType type);

}
