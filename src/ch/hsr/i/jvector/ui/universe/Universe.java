package ch.hsr.i.jvector.ui.universe;

import java.awt.GraphicsConfigTemplate;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.HashMap;
import java.util.Map;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.GraphicsConfigTemplate3D;
import javax.media.j3d.Group;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import ch.hsr.i.jvector.logic.DrawComponents;
import ch.hsr.i.jvector.ui.main.JVector;
import ch.hsr.i.jvector.ui.universe.behavior.OrbitBehavior;

/**
 * An implementation of the scenegraph, providing methods to get a views of this scene, add and
 * remove elements at runtime from and to the scene.
 */
public class Universe {
	
	public static enum Views {
		PERSPECTIVE, TOP, LEFT, RIGHT
	}
	
	/*
	 * JVector needs to modify the objects on the scene side of the scenegraph
	 * at runtime, so we keep a reference to it in the sceneBranchGroup member
	 * variable below.
	 */
	private BranchGroup sceneBranchGroup = null;
	
	/**
	 * JVector has 4 main frames which can show the scene from different sides. Store the four
	 * canvas objects in this hash map.
	 */
	private Map<String, Canvas3D> perspectives = new HashMap<String, Canvas3D>();
	
	/**
	 * JVector has 4 main frames each one having its own behavior object.
	 */
	private Map<String, OrbitBehavior> behaviors = new HashMap<String, OrbitBehavior>();

	/**
	 * Creates a universe object with one single view platform.
	 */
	public Universe(String[] perspectives) {
		init(perspectives);
	}
	
	/**
	 * Callback to allow a Canvas3D to be added to a Panel. This method allows 
	 * the Canvas3D to be added to its parent GUI components. If the canvas at
	 * the specified index does not exist the first one is returned.
	 * 
	 * @param the index of the canvas you'd like to get
	 */
	public Canvas3D getCanvas3D(String perspective) {
		String s = perspective.toLowerCase();
		if(perspectives.containsKey(s)) {
			return perspectives.get(s);
		} else {
			return perspectives.get("left");
		}
	}
	
	public BranchGroup getObjectRoot() {
		return sceneBranchGroup;
	}
	
	/**
	 * Sets the view in the specified frame to the desired values.
	 * @param resource
	 */
	public void setView(String perspective, String view) {
		if(behaviors.containsKey(perspective)) {
			OrbitBehavior b = behaviors.get(perspective);
			setPersectiveView(b, view);
		}
	}
	
	/**
	 * set view
	 * 
	 * @param behavior orbitbehavior
	 * @param view desired view
	 */
	private void setPersectiveView(OrbitBehavior behavior, String view) {
		if(view.equalsIgnoreCase(JVector.PERSPECTIVE)) {
			behavior.setViewingTransform(new Point3d(60, 100, 80), new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 0.0, 1.0), true);
		}
		else if(view.equalsIgnoreCase(JVector.TOP)) {
			behavior.setViewingTransform(new Point3d(1, 0, 100), new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 0.0, 1.0), true);
		}
		else if(view.equalsIgnoreCase(JVector.LEFT)) {
			behavior.setViewingTransform(new Point3d(100, 1, 1), new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 0.0, 1.0), true);
		}
		else if(view.equalsIgnoreCase(JVector.RIGHT)) {
			behavior.setViewingTransform(new Point3d(1, 100, 1), new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 0.0, 1.0), true);
		} else {
			behavior.setViewingTransform(new Point3d(60, 100, 80), new Point3d(0.0, 0.0, 0.0), new Vector3d(0.0, 0.0, 1.0), true);
		}
	}
	
	/*
	 * The init method does all of the work of setting up and populating the scenegraph.
	 */
	private void init(String[] perspectives) {
		
		// the root for this tree
		VirtualUniverse universe = new VirtualUniverse();
		
		// directly placed under universe, multiples allowed
		Locale locale = new Locale(universe);
		
		/*
		 * sceneBranchGroup created below contains the graphical objects,
		 * lights, and behaviors that will compose the rendered scene
		 */
		BranchGroup sceneBranchGroup = createSceneBranchGroup();
		
		// the background for this scene
		Background background = createBackground();
		if (background != null)
			sceneBranchGroup.addChild(background);
		
		// We then have to add the scene branch to the Locale
		// we added previously to the VirtualUniverse.
		locale.addBranchGraph(sceneBranchGroup);
		
		// create view brach for each view
		for(String s : perspectives) {
			
			/*
			 * We must now define the view side of the scenegraph. First we create a
			 * ViewPlatform. The ViewPlatform defines a location in the scene from
			 * which the scene can be viewed. The scene can contain multiple
			 * ViewPlatforms, and View objects can be moved between them at runtime.
			 * 
			 * To contain the ViewPlatform we create a scenegraph branch. We create
			 * a BranchGroup that is the top of the view branch. Underneath it we
			 * create a series of TransformGroup, and then finally we attach the
			 * ViewPlatform to the lowest TransformGroup. The TransformGroups (which
			 * contain a 4 × 4 transformation matrix) allow the ViewPlatform to be
			 * rotate, scaled, and translated within the scene.
			 */
			ViewPlatform vp = createViewPlatform();
			
			/*
			 * To contain the ViewPlatform we create a scenegraph branch. We create
			 * a BranchGroup that is the top of the view branch. Underneath it we
			 * create a series of TransformGroup, and then finally we attach the
			 * ViewPlatform to the lowest TransformGroup. The TransformGroups (which
			 * contain a 4 × 4 transformation matrix) allow the ViewPlatform to be
			 * rotate, scaled, and translated within the scene.
			 */
			
			TransformGroup viewTransformGroup = getViewTransformGroup();
			
			viewTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
			viewTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			viewTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
			viewTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
			viewTransformGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
			
			BranchGroup viewBranchGroup = createViewBranchGroup(viewTransformGroup, vp);
			
			/*
			 * Finally, create the View object and attach it to the ViewPlatform.
			 * The View object has an associated PhysicalEnvironment and
			 * PhysicalBody that defines the 293 characteristics of the viewer and
			 * their display hardware. A Canvas3D rendering component is attached to
			 * the View which is used to display the frames rendered.
			 */
			View view = createView(vp);
			
			// add orbit behavior for this view
			OrbitBehavior behavior = new OrbitBehavior(view.getCanvas3D(0), viewTransformGroup, view);
			behavior.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000));
			behavior.setRotationCenter(new Point3d(0.0, 0.0, 0.0));
			behavior.setZoomFactor(10);
			behavior.setTransFactors(10.0, 10.0);
			behavior.setCapability(OrbitBehavior.REVERSE_ALL);
			
			setPersectiveView(behavior, s);
			
			behaviors.put(s, behavior);
			
			viewTransformGroup.addChild(behavior);
			
			// Add the view branch to the Locale
			locale.addBranchGraph(viewBranchGroup);
			
			// put the view for this label to the perspectives list
			this.perspectives.put(s.toLowerCase(), view.getCanvas3D(0));
		}
	}
	
	// Helper method to create a Java 3D View and
	// attach it to a ViewPlatform .
	private View createView(ViewPlatform vp) {
		View view = new View();
		
		// We create a default PhysicalBody and PhysicalEnvironment and
		// associate them with the View.
		PhysicalBody pb = new PhysicalBody();
		PhysicalEnvironment pe = new PhysicalEnvironment();
		view.setPhysicalEnvironment(pe);
		view.setPhysicalBody(pb);
		
		// Add the View to the ViewPlatform
		if (vp != null)
			view.attachViewPlatform(vp);
		/*
		 * Set the locations of the clipping planes for the View. Java 3D uses a
		 * finite number of bits (in a depth-buffer) to track the relative
		 * distances of objects from the viewer. These depth-buffer bits are
		 * used to track objects between the front clipping plane and the rear
		 * clipping plane. Only objects that fall between the two clipping
		 * planes will be rendered. As the depth-buffer bits have a finite
		 * length (usually 16 or 24 bits) the ratio between the front clipping
		 * plane and the rear clipping plane should be less than about 1000, or
		 * the depth-buffer will be very coarsely quantized and accuracy will be
		 * lost. In this example we use 1.0 for the front clipping plane and
		 * 1000.0 for the rear clipping plane.
		 */
		view.setBackClipDistance(1000.0);
		view.setFrontClipDistance(1.0);
		
		// Create the Canvas3D used to display the rendered scene
		Canvas3D c3d = createCanvas3D(false);
		
		// Add the Canvas3D to the View so that the View has a component
		// to render into.
		view.addCanvas3D(c3d);
			
		return view;
	}
	
	// Utility method to create a Canvas3D GUI component. The Canvas3D
	// is used by Java 3D to output rendered frames.
	private Canvas3D createCanvas3D(boolean offscreen) {
		/*
		 * First we query Java 3D for the available device information. We set
		 * up a GraphicsConfigTemplate3D and specify that we would prefer a
		 * device configuration that supports antialiased output.
		 */
		GraphicsConfigTemplate3D gc3D = new GraphicsConfigTemplate3D();
		gc3D.setSceneAntialiasing(GraphicsConfigTemplate.PREFERRED);
		
		// We then get a list of all the screen devices for the
		// local graphics environment
		GraphicsDevice gd[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getScreenDevices();
		
		// We select the best configuration supported by the first screen
		// device, and specify whether we are creating an onscreen or
		// an offscreen Canvas3D.
		Canvas3D c3d = new Canvas3D(gd[0].getBestConfiguration(gc3D), offscreen);
		
		/*
		 * Here we have hard-coded the initial size of the Canvas3D. However,
		 * because we have used a BorderLayout layout algorithm, this will be
		 * automatically resized to fit—-as the parent JFrame is resized.
		 */
		c3d.setSize(500, 500);
		return c3d;
	}
	
	// Simple utility method to create a solid colored background for
	// the Canvas3D.
	private Background createBackground() {
		
		Background bg = new Background(new Color3f(1f, 1f, 1f));
		
		// We need to set the volume within the scene within which the
		// Background is active
		bg.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), DrawComponents.RADIUS));
		return bg;
	}
	
	/*
	 * Get a TransformGroup array for the View side of the scenegraph. We create
	 * a single TransformGroup (which wraps a 4 × 4 transformation matrix) and
	 * modify the transformation matrix to apply a scale to the view of the
	 * scene, as well as move the ViewPlatform back by 20 meters so that we can
	 * see the origin (0,0,0). The objects that we create in the scene will be
	 * centered at the origin, so if we are going to be able to see them, we
	 * need to move the ViewPlatform backward.
	 */
	private TransformGroup getViewTransformGroup() {
		TransformGroup tg = new TransformGroup();
		return tg;
	}
	
	// Create the View Platform for the View.
	private ViewPlatform createViewPlatform() {
		ViewPlatform vp = new ViewPlatform();
		vp.setViewAttachPolicy(View.RELATIVE_TO_FIELD_OF_VIEW);
		vp.setActivationRadius(1000f);
		
		return vp;
	}
	
	// Create the View side BranchGroup. The ViewPlatform is wired in
	// beneath the TransformGroups.
	private BranchGroup createViewBranchGroup(TransformGroup tg, ViewPlatform vp) {
		
		tg.addChild(vp);
		
		BranchGroup viewBranchGroup = new BranchGroup();
		viewBranchGroup.addChild(tg);
		
		return viewBranchGroup;
	}
	
	// Create the scene side of the scenegraph. This method does
	// all the work of creating the scene branch—containing graphical
	// objects, lights, and rotation behaviors to rotate the objects.
	private BranchGroup createSceneBranchGroup() {
		
		// First we create the root of the scene side scenegraph. We will
		// add other Nodes as children of this root BranchGroup.
		BranchGroup objRoot = new BranchGroup();
		
		/*
		 * Create the BranchGroup which contains the objects we add/remove to
		 * the scenegraph. We store a reference to this subbranch of
		 * the scene side of the scenegraph in a member variable as we need to
		 * modify the contents of the branch at runtime.
		 */
		sceneBranchGroup = new BranchGroup();
		
		// Allow the BranchGroup to have children added and removed
		// at runtime
		sceneBranchGroup.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		sceneBranchGroup.setCapability(Group.ALLOW_CHILDREN_READ);
		sceneBranchGroup.setCapability(Group.ALLOW_CHILDREN_WRITE);
								
		/*
		 * Wire the scenegraph together. It is useful to do this in the reverse
		 * order that the branches were created— rather like closing
		 * parentheses, that way you will not forget to add a child branch to
		 * its parent. If you forget to add a branch that you have created and
		 * populated then it will just not show up in the scene!
		 */
		objRoot.addChild(sceneBranchGroup);
		
		// Return the root of the scene side of the scenegraph
		return objRoot;
	}
}
