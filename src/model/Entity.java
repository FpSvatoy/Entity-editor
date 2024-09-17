package model;

public class Entity {
	private String thisName;
	private Drawbox thisDrawbox;
	private Hitbox thisHitbox;
	private String type;
	
	public Entity(String name,String drawbox,String hitbox) {
		thisName = new String(name);
		thisDrawbox = new Drawbox(drawbox);
		thisHitbox = new Hitbox(hitbox);
	}
	
	public Entity(String name, Hitbox hitbox, Drawbox drawbox) {
		this.thisName = name;
		this.thisHitbox = hitbox;
		this.thisDrawbox = drawbox;
	}
	
	public void setName(String outName) {
		thisName = outName;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public void setDrawbox(Drawbox outDrawbox) {
		thisDrawbox = outDrawbox;
	};
	
	public void setHitbox(Hitbox outHitbox) {
		thisHitbox = outHitbox;
	};
	
	public String getName() {
		return thisName;
	};
	
	public Drawbox getDrawbox() {
		return thisDrawbox;
	};
	
	public Hitbox getHitbox() {
		return thisHitbox;
	};
	
	public void PrintEntity() {
		System.out.println("---------------------");
		System.out.println("Name: "+thisName);
		this.thisDrawbox.printToConsole();
		this.thisHitbox.printToConsole();
		System.out.println("---------------------");
	}
}
