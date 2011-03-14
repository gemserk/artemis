package com.artemis;

import com.artemis.utils.ImmutableBag;

public abstract class DelayedEntityProcessingSystem extends DelayedEntitySystem {
	
	public DelayedEntityProcessingSystem(Class<? extends Component>... types) {
		super(types);
	}
	
	/**
	 * Process a entity this system is interested in.
	 * @param e the entity to process.
	 */
	protected abstract void process(Entity e, int accumulatedDelta);

	@Override
	protected final void processEntities(ImmutableBag<Entity> entities, int accumulatedDelta) {
		for (int i = 0, s = entities.size(); s > i; i++) {
			Entity entity = entities.get(i);
			if(entity != null)
				process(entity, accumulatedDelta);
		}
	}

}
