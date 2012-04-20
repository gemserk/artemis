package com.artemis;

import com.artemis.utils.ImmutableBag;

/**
 * The entity class. Cannot be instantiated outside the framework, you must create new entities using World.
 * 
 * @author Arni Arent
 * 
 */
public final class Entity {
	private int id;
	private long uniqueId;
	private long typeBits;
	private long systemBits;

	private World world;
	private EntityManager entityManager;

	private boolean enabled;

	boolean refreshPending;

	/**
	 * Returns true if the Entity is enabled, false otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Enables the Entity to let it be processed by each EntitySystem having it.
	 */
	public void enable() {
		if (!enabled)
			refresh();
		enabled = true;
	}

	/**
	 * Disables the Entity to avoid it to be processed by each EntitySystem having it.
	 */
	public void disable() {
		if (enabled)
			refresh();
		enabled = false;
	}

	protected Entity(World world, int id) {
		this.world = world;
		this.entityManager = world.getEntityManager();
		this.id = id;
		this.enabled = true;
		this.refreshPending = false;
		refresh();
	}

	/**
	 * The internal id for this entity within the framework. No other entity will have the same ID, but ID's are however reused so another entity may acquire this ID if the previous entity was deleted.
	 * 
	 * @return id of the entity.
	 */
	public int getId() {
		return id;
	}

	protected void setUniqueId(long uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * Get the unique ID of this entity. Because entity instances are reused internally use this to identify between different instances.
	 * 
	 * @return the unique id of this entity.
	 */
	public long getUniqueId() {
		return uniqueId;
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
		enable();
	}

	@Override
	public String toString() {
		return "Entity[" + id + "]";
	}

	/**
	 * Add a component to this entity.
	 * 
	 * @param component
	 *            to add to this entity
	 */
	public void addComponent(Component component) {
		entityManager.addComponent(this, component);
		refresh();
	}

	/**
	 * Removes the component from this entity.
	 * 
	 * @param component
	 *            to remove from this entity.
	 */
	public void removeComponent(Component component) {
		entityManager.removeComponent(this, component);
		refresh();
	}

	/**
	 * Faster removal of components from a entity.
	 * 
	 * @param component
	 *            to remove from this entity.
	 */
	public void removeComponent(ComponentType type) {
		entityManager.removeComponent(this, type);
		refresh();
	}

	/**
	 * Checks if the entity has been deleted from somewhere.
	 * 
	 * @return if it's active.
	 */
	public boolean isActive() {
		return entityManager.isActive(id);
	}

	public boolean isAlive() {
		return (world.getEntity(id) != null);
	}

	/**
	 * This is the preferred method to use when retrieving a component from a entity. It will provide good performance.
	 * 
	 * @param type
	 *            in order to retrieve the component fast you must provide a ComponentType instance for the expected component.
	 * @return
	 */
	public Component getComponent(ComponentType type) {
		return entityManager.getComponent(this, type);
	}

	/**
	 * Slower retrieval of components from this entity. Minimize usage of this, but is fine to use e.g. when creating new entities and setting data in components.
	 * 
	 * @param <T>
	 *            the expected return component type.
	 * @param type
	 *            the expected return component type.
	 * @return component that matches, or null if none is found.
	 */
	public <T extends Component> T getComponent(Class<T> type) {
		return type.cast(getComponent(ComponentTypeManager.getTypeFor(type)));
	}

	/**
	 * Get all components belonging to this entity. WARNING. Use only for debugging purposes, it is dead slow. WARNING. The returned bag is only valid until this method is called again, then it is overwritten.
	 * 
	 * @return all components of this entity.
	 */
	public ImmutableBag<Component> getComponents() {
		return entityManager.getComponents(this);
	}

	/**
	 * Refresh all changes to components for this entity. After adding or removing components, you must call this method. It will update all relevant systems. It is typical to call this after adding components to a newly created entity.
	 */
	public void refresh() {
		if (refreshPending)
			return;
		world.refreshEntity(this);
		refreshPending = true;
	}

	/**
	 * Delete this entity from the world.
	 */
	public void delete() {
		world.deleteEntity(this);
	}

	/**
	 * Set the group of the entity. Same as World.setGroup().
	 * 
	 * @param group
	 *            of the entity.
	 */
	public void setGroup(String group) {
		world.getGroupManager().set(group, this);
	}

	/**
	 * Assign a tag to this entity. Same as World.setTag().
	 * 
	 * @param tag
	 *            of the entity.
	 */
	public void setTag(String tag) {
		world.getTagManager().register(tag, this);
	}

}
