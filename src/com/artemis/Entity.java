package com.artemis;

public final class Entity {
	private int id;
	private long typeBits;
	private long systemBits;
	private World world;
	private EntityManager entityManager;
	
	protected Entity(World world, int id) {
		this.world = world;
		this.entityManager = world.getEntityManager();
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	protected long getTypeBits() {
		return typeBits;
	}
	
	protected void addTypeBit(long bit) {
		typeBits |= bit;
	}
	
	protected void removeTypeBit(long bit) {
		typeBits &= ~bit;
	}
	
	protected long getSystemBits() {
		return systemBits;
	}
	
	protected void addSystemBit(long bit) {
		systemBits |= bit;
	}
	
	protected void removeSystemBit(long bit) {
		systemBits &= ~bit;
	}
	
	protected void setSystemBits(long systemBits) {
		this.systemBits = systemBits;
	}
	
	protected void setTypeBits(long typeBits) {
		this.typeBits = typeBits;
	}
	
	protected void reset() {
		systemBits = 0;
		typeBits = 0;
	}
	
	@Override
	public String toString() {
		return "Entity["+id+"]";
	}
	
	public void addComponent(Component component){
		entityManager.addComponent(this, component);
	}
	
	public void removeComponent(Component component){
		entityManager.removeComponent(this, component);
	}
	
	public boolean isActive(){
		return entityManager.isActive(id);
	}

	public Component getComponent(ComponentType type) {
		return entityManager.getComponent(this, type);
	}
	
	public <T extends Component> T getComponent(Class<T> type) {
		return type.cast(getComponent(ComponentTypeManager.getTypeFor(type)));
	}
	
	public void refresh() {
		entityManager.refresh(this);
	}

}
