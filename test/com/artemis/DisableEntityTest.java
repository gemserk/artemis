package com.artemis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DisableEntityTest {
	
	static class ComponentA extends Component {
		
	}

	static class ComponentB extends Component {
		
	}
	
	static class MockEntityProcessingSystem extends EntityProcessingSystem {
		
		public Set<Entity> addedEntities = new HashSet<Entity>(); 
		public Set<Entity> removedEntities = new HashSet<Entity>();
		
		public MockEntityProcessingSystem(Class<? extends Component> requiredType) {
			super(requiredType);
		}
		
		@Override
		protected void begin() {
			super.begin();
			addedEntities.clear();
			removedEntities.clear();
		}
		
		@Override
		protected void added(Entity e) {
			super.added(e);
			addedEntities.add(e);
		}
		
		@Override
		protected void removed(Entity e) {
			super.removed(e);
			removedEntities.add(e);
		}

		@Override
		protected void process(Entity e) {
			
		}
		
	}

	@Test
	public void shouldCallAddedWhenCreated() {
		World world = new World();
		
		MockEntityProcessingSystem mockSystemA = new MockEntityProcessingSystem(ComponentA.class);
		
		world.getSystemManager().setSystem(mockSystemA);
		world.getSystemManager().initializeAll();
		
		Entity entity = world.createEntity();
		entity.addComponent(new ComponentA());
		entity.refresh();

		world.loopStart();
		
		assertTrue(mockSystemA.addedEntities.contains(entity));
		assertFalse(mockSystemA.removedEntities.contains(entity));
	}
	
	@Test
	public void shouldCallRemovedWhenEntityDeleted() {
		World world = new World();
		
		MockEntityProcessingSystem mockSystemA = new MockEntityProcessingSystem(ComponentA.class);
		
		world.getSystemManager().setSystem(mockSystemA);
		world.getSystemManager().initializeAll();
		
		Entity entity = world.createEntity();
		entity.addComponent(new ComponentA());
		entity.refresh();

		world.loopStart();
		
		entity.delete();

		world.loopStart();

		assertTrue(mockSystemA.removedEntities.contains(entity));
		assertFalse(mockSystemA.addedEntities.contains(entity));
	}

}
