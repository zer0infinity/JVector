package ch.hsr.i.jvector.logic;

import java.util.ArrayList;
import java.util.Observable;

import ch.hsr.i.jvector.components.Component3D.ComponentType;
import ch.hsr.i.jvector.interfaces.Component;

public class ComponentsList extends Observable {
	
	private ArrayList<Component> compList;

	public ComponentsList() {
		compList = new ArrayList<Component>();
		
		setChanged();
		notifyObservers();
	}
	
	public void add(Component comp) {
		compList.add(comp);
		
		setChanged();
		notifyObservers();
	}
	
	public void addAll(ArrayList<Component> compList) {
		this.compList = compList;
		
		System.gc();
		
		setChanged();
		notifyObservers();
	}
	
	public void remove(int index) {
		compList.remove(index);
		
		setChanged();
		notifyObservers();
	}
	
	public int size() {
		return compList.size();
	}
	
	public Component get(int index) {
		return compList.get(index);
	}
	
	public boolean isEmpty() {
		return compList.isEmpty();
	}
	
	public void clear() {
		compList.clear();
		
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<Component> deepClone() {
		ArrayList<Component> cloneList = new ArrayList<Component>();
		for (int i = 0; i < compList.size(); i++) {
			cloneList.add(compList.get(i));
		}
		return cloneList;
	}
	
	public ArrayList<Component> deepCloneFilter(ComponentType[] filter) {
		ArrayList<Component> cloneList = deepClone();
		for (int i = 0; i < cloneList.size(); i++) {
			for (int j = 0; j < filter.length; j++) {
				if(filter[j] == cloneList.get(i).getType()) {
					cloneList.remove(i);
					i--;
					break;
				}
			}
		}
		return cloneList;
	}
	
	public void removeAll() {
		for(int i = 0; i < compList.size(); i++) {
			compList.remove(i);
			i--;
		}
		
		setChanged();
		notifyObservers();
	}
}
