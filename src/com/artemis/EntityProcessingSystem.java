package com.artemis;

import com.artemis.utils.ImmutableBag;

/**
 * A typical entity system. Use this when you need to process entities possessing the
 * provided component types.
 * 
 * @author Arni Arent
 *
 */
public abstract class EntityProcessingSystem extends EntitySystem {

	public EntityProcessingSystem(Class<? extends Component>... types) {
		super(types);
	}
	
	/**
	 * Process a entity this system is interested in.
	 * @param e the entity to process.
	 */
	protected abstract void process(Entity e);

	@Override
	protected final void processEntities(ImmutableBag<Entity> entities) {
		begin();
		for (int i = 0, s = entities.size(); s > i; i++) {
			Entity e = entities.get(i);
			if (e != null) {
				process(e);
			}
		}
		end();
	}
	
}
