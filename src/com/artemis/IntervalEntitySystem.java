package com.artemis;

import com.artemis.utils.ImmutableBag;

public abstract class IntervalEntitySystem extends EntitySystem {
	private int acc;
	private int interval;

	public IntervalEntitySystem(int interval, Class<? extends Component>... types) {
		super(types);
		this.interval = interval;
	}

	@Override
	public void initialize() {
	}

	@Override
	final protected void processEntities(ImmutableBag<Entity> entities) {
		acc += world.getDelta();
		
		if(acc >= interval) {
			acc -= interval;
			intervalProcessAll(entities);
		}
	}
	
	protected abstract void intervalProcessAll(ImmutableBag<Entity> entities);
	

}
