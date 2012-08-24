package com.artemis;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

/**
 * A typical entity system. Use this when you need to process entities possessing the
 * provided component types.
 * 
 * @author Arni Arent
 *
 */
public abstract class EntityProcessingSystem extends EntitySystem {
	
	Bag<Entity> actives = new Bag<Entity>();
	
	/**
	 * Create a new EntityProcessingSystem. It requires at least one component.
	 * @param requiredType the required component type.
	 * @param otherTypes other component types.
	 */
	public EntityProcessingSystem(Class<? extends Component> requiredType, Class<? extends Component>... otherTypes) {
		super(getMergedTypes(requiredType, otherTypes));
	}
	
	/**
	 * Process a entity this system is interested in.
	 * @param e the entity to process.
	 */
	protected abstract void process(Entity e);

	@Override
	protected final void processEntities() {
		for (int i = 0, s = actives.size(); s > i; i++) {
			process(actives.get(i));
		}
	}
	
	@Override
	protected void enabled(Entity e) {
		actives.add(e);
	}
	
	@Override
	protected void disabled(Entity e) {
		actives.remove(e);
	}
	
	
	
	@Override
	protected boolean checkProcessing() {
		return true;
	}
	
}
