package com.artemis;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

public abstract class EntitySystem {
	private static int SYSID = 0;

	private long systemBit = (long) Math.pow(2, SYSID++);

	private long typeFlags;

	protected World world;

	private Bag<Entity> actives;

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
	
	protected abstract void processEntities(ImmutableBag<Entity> entities);

	public abstract void initialize();

	protected void added(Entity e) {};

	protected void removed(Entity e) {};

	protected final void change(Entity e) {
		boolean contains = (systemBit & e.getSystemBits()) == systemBit;
		boolean interest = (typeFlags & e.getTypeBits()) == typeFlags;

		if (interest && !contains) {
			actives.add(e);
			e.addSystemBit(systemBit);
			added(e);
		} else if (!interest && contains) {
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
