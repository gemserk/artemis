package com.artemis;

import com.artemis.utils.ImmutableBag;

public abstract class IntervalEntityProcessingSystem extends IntervalEntitySystem {

	public IntervalEntityProcessingSystem(int interval, Class<? extends Component>... types) {
		super(interval, types);
	}

	@Override
	public void initialize() {
	}

	protected final void intervalProcessAll(ImmutableBag<Entity> entities) {
		for (int i = 0, s = entities.size(); s > i; i++) {
			process(entities.get(i));
		}
	}

	protected abstract void process(Entity e);
}
