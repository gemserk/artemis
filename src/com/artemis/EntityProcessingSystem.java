package com.artemis;

import com.artemis.utils.ImmutableBag;

public abstract class EntityProcessingSystem extends EntitySystem {

	public EntityProcessingSystem(Class<? extends Component>... types) {
		super(types);
	}
		
	protected abstract void process(Entity e);

	protected void begin() {
	};
	
	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {
		begin();
		for (int i = 0, s = entities.size(); s > i; i++) {
			Entity e = entities.get(i);
			if (e != null) {
				process(e);
			}
		}
		end();
	}
	
	protected void end() {
	};
}
