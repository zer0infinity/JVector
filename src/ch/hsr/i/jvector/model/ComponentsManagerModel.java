package ch.hsr.i.jvector.model;

import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractListModel;

import ch.hsr.i.jvector.logic.ComponentsList;

public class ComponentsManagerModel extends AbstractListModel implements Observer {

	private static final long serialVersionUID = -5171066017454783142L;
	
	private ComponentsList compList;
	
	public ComponentsManagerModel(ComponentsList compList) {
		this.compList = compList;
		compList.addObserver(this);
	}

	@Override
	public Object getElementAt(int index) {
		return compList.get(index).getType();
	}

	@Override
	public int getSize() {
		return compList.size();
	}

	@Override
	public void update(Observable o, Object arg) {
		fireContentsChanged(this, 0, compList.size());
	}
}
