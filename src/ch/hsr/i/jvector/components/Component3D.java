package ch.hsr.i.jvector.components;

import javax.media.j3d.Shape3D;

import ch.hsr.i.jvector.components.ComponentAppearance.AppearanceType;
import ch.hsr.i.jvector.interfaces.Component;

public abstract class Component3D implements Component {
	
	public static enum ComponentType {
		POINT, LINE, PLANE
	}
	
	private ComponentType cType;
	protected AppearanceType aType = AppearanceType.NORMAL;
	
	public Component3D(ComponentType type) {
		this.cType = type;
	}
	
	@Override
	public ComponentType getType() {
		return cType;
	}
	
	@Override
	public AppearanceType getAppearanceType() {
		return aType;
	}
	
	@Override
	public abstract Shape3D getShape(AppearanceType type); 
		
	
	@Override
	public abstract void setAppearance(AppearanceType type);
	
	

}
