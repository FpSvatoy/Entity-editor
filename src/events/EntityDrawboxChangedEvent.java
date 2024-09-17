package events;

import model.Drawbox;
import model.Entity;

/*
 * Structure-like class no getters
 * */
public class EntityDrawboxChangedEvent {
	public Drawbox drawbox;
	//storing entity object instead of just entity name will allow to get entity data without extra calls
	public Entity owner; 
	
	public EntityDrawboxChangedEvent(Drawbox drawbox, Entity owner) {
		this.drawbox = drawbox;
		this.owner = owner;
	}
}
