package ch.hsr.i.jvector.components;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

import ch.hsr.i.jvector.components.Component3D.ComponentType;

public class ComponentAppearance {

	public static enum AppearanceType {
		NORMAL, INTERSECTION, HIGHLIGHTED1, HIGHLIGHTED2 
	}
	
	public static Appearance getAppearance(ComponentType cType, AppearanceType aType) {
		
		switch (aType) {
		
		case NORMAL:
			return getNormalAppearance(cType);

		case INTERSECTION:
			return getIntersectionAppearance(cType);

		case HIGHLIGHTED1:
			return getHighlightedAppearance1(cType);
			
		case HIGHLIGHTED2:
			return getHighlightedAppearance2(cType);
		
		default:
			return getNormalAppearance(cType);
		}
	}
	
	private static Appearance getNormalAppearance(ComponentType cType) {
		Appearance normal = new Appearance();
		ColoringAttributes color;
		TransparencyAttributes transparency;
			
		switch(cType) {
		
		case POINT:
			PointAttributes pointAttr = new PointAttributes(10.0f, true);
			normal.setPointAttributes(pointAttr);
			
			color = new ColoringAttributes(new Color3f(0.05f, 0.05f, 0.05f), ColoringAttributes.NICEST);
			normal.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.1f);
			normal.setTransparencyAttributes(transparency);
			break;
			
		case LINE:
			LineAttributes lineAttr = new LineAttributes(2.0f, LineAttributes.PATTERN_SOLID , true);
			normal.setLineAttributes(lineAttr);
			
			color = new ColoringAttributes(new Color3f(0.05f, 0.05f, 0.05f), ColoringAttributes.NICEST);
			normal.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.1f);
			normal.setTransparencyAttributes(transparency);
			break;
			
		case PLANE:
			PolygonAttributes polygonAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1000.0f);
			normal.setPolygonAttributes(polygonAttr);
			
			color = new ColoringAttributes(new Color3f(0.05f, 0.05f, 0.05f), ColoringAttributes.NICEST);
			normal.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.9f);
			normal.setTransparencyAttributes(transparency);
			break;
		}
		
		return normal;
	}
	
	private static Appearance getIntersectionAppearance(ComponentType cType) {
		
		Appearance intersection = new Appearance();
		ColoringAttributes color;
		TransparencyAttributes transparency;
		
		switch(cType) {
		
		case POINT:
			PointAttributes pointAttr = new PointAttributes(12.0f, true);
			intersection.setPointAttributes(pointAttr);
			
			color = new ColoringAttributes(new Color3f(1f, 0.0f, 0.0f), ColoringAttributes.NICEST);
			intersection.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.2f);
			intersection.setTransparencyAttributes(transparency);
			break;
			
		case LINE:
			LineAttributes lineAttr = new LineAttributes(2.0f, LineAttributes.PATTERN_DASH , true);
			intersection.setLineAttributes(lineAttr);
			
			color = new ColoringAttributes(new Color3f(1f, 0.0f, 0.0f), ColoringAttributes.NICEST);
			intersection.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.3f);
			intersection.setTransparencyAttributes(transparency);
			break;
			
		case PLANE:
			PolygonAttributes polygonAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1000.0f);
			intersection.setPolygonAttributes(polygonAttr);
			
			color = new ColoringAttributes(new Color3f(1f, 0.0f, 0.0f), ColoringAttributes.NICEST);
			intersection.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.9f);
			intersection.setTransparencyAttributes(transparency);
			break;
		}
		
		return intersection;
	}
	
	private static Appearance getHighlightedAppearance1(ComponentType cType) {
		
		Appearance highlighted = new Appearance();
		ColoringAttributes color;
		TransparencyAttributes transparency;
		
		switch(cType) {
		
		case POINT:
			PointAttributes pointAttr = new PointAttributes(15.0f, true);
			highlighted.setPointAttributes(pointAttr);
			
			color = new ColoringAttributes(new Color3f(0f, 0f, 1f), ColoringAttributes.NICEST);
			highlighted.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0f);
			highlighted.setTransparencyAttributes(transparency);
			break;
			
		case LINE:
			LineAttributes lineAttr = new LineAttributes(4.0f, LineAttributes.PATTERN_SOLID , true);
			highlighted.setLineAttributes(lineAttr);
			
			color = new ColoringAttributes(new Color3f(0f, 0f, 1f), ColoringAttributes.NICEST);
			highlighted.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0f);
			highlighted.setTransparencyAttributes(transparency);
			break;
			
		case PLANE:
			PolygonAttributes polygonAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1000.0f);
			highlighted.setPolygonAttributes(polygonAttr);
			
			color = new ColoringAttributes(new Color3f(0f, 0f, 1f), ColoringAttributes.NICEST);
			highlighted.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0f);
			highlighted.setTransparencyAttributes(transparency);
			break;
		}
		
		return highlighted;
	}	
	
	private static Appearance getHighlightedAppearance2(ComponentType cType) {
		
		Appearance highlighted = new Appearance();
		ColoringAttributes color;
		TransparencyAttributes transparency;
		
		switch(cType) {
		
		case POINT:
			PointAttributes pointAttr = new PointAttributes(15.0f, true);
			highlighted.setPointAttributes(pointAttr);
			
			color = new ColoringAttributes(new Color3f(0f, 1f, 0f), ColoringAttributes.NICEST);
			highlighted.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0f);
			highlighted.setTransparencyAttributes(transparency);
			break;
			
		case LINE:
			LineAttributes lineAttr = new LineAttributes(4.0f, LineAttributes.PATTERN_SOLID , true);
			highlighted.setLineAttributes(lineAttr);
			
			color = new ColoringAttributes(new Color3f(0f, 1f, 0f), ColoringAttributes.NICEST);
			highlighted.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0f);
			highlighted.setTransparencyAttributes(transparency);
			break;
			
		case PLANE:
			PolygonAttributes polygonAttr = new PolygonAttributes(PolygonAttributes.POLYGON_FILL, PolygonAttributes.CULL_NONE, 1000.0f);
			highlighted.setPolygonAttributes(polygonAttr);
			
			color = new ColoringAttributes(new Color3f(0f, 1f, 0f), ColoringAttributes.NICEST);
			highlighted.setColoringAttributes(color);
			
			transparency = new TransparencyAttributes(TransparencyAttributes.NICEST, 0f);
			highlighted.setTransparencyAttributes(transparency);
			break;
		}
		
		return highlighted;
	}	
}
