package com.artemis;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

/**
 * The most raw entity system. It should not typically be used, but you can create your own
 * entity system handling by extending this. It is recommended that you use the other provided
 * entity system implementations.
 * 
 * @author Arni Arent
 *
 */
public abstract class EntitySystem {
	private static int SYSID = 0;

	private long systemBit = (long) Math.pow(2, SYSID++);

	private long typeFlags;

	protected World world;

	private Bag<Entity> actives;
	
	public EntitySystem() {
	}

	public EntitySystem(Class<? extends Component>... types) {
		actives = new Bag<Entity>();

		for (Class<? extends Component> type : types) {
			ComponentType ct = ComponentTypeManager.getTypeFor(type);
			typeFlags |= ct.getBit();
		}
	}

	public final void process() {
		processEntities(actives);
	}
	
	/**
	 * Any implementing entity system must implement this method and the logic
	 * to process the given entities of the system.
	 * 
	 * @param entities the entities this system contains.
	 */
	protected abstract void processEntities(ImmutableBag<Entity> entities);

	public abstract void initialize();

	/**
	 * Called if the system has received a entity it is interested in, e.g. created or a component was added to it.
	 * @param e the entity that was added to this system.
	 */
	protected void added(Entity e) {};

	/**
	 * Called if a entity was removed from this system, e.g. deleted or had one of it's components removed.
	 * @param e the entity that was removed from this system.
	 */
	protected void removed(Entity e) {};

	protected final void change(Entity e) {
		boolean contains = (systemBit & e.getSystemBits()) == systemBit;
		boolean interest = (typeFlags & e.getTypeBits()) == typeFlags;

		if (interest && !contains && typeFlags > 0) {
			actives.add(e);
			e.addSystemBit(systemBit);
			added(e);
		} else if (!interest && contains && typeFlags > 0) {
			remove(e);
		}
	}

	private void remove(Entity e) {
		actives.remove(e);
		e.removeSystemBit(systemBit);
		removed(e);
	}

	protected final void setWorld(World world) {
		this.world = world;
	}

}
