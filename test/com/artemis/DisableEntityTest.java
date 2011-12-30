package com.artemis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.artemis.utils.Bag;

public class DisableEntityTest {

	static class ComponentA extends Component {

	}

	static class ComponentB extends Component {

	}

	static class MockEntityProcessingSystem extends EntityProcessingSystem {

		public Set<Entity> addedEntities = new HashSet<Entity>();
		public Set<Entity> removedEntities = new HashSet<Entity>();
		public Set<Entity> enabledEntities = new HashSet<Entity>();
		public Set<Entity> disabledEntities = new HashSet<Entity>();
		
		public MockEntityProcessingSystem(Class<? extends Component> requiredType) {
			super(requiredType);
		}

		@Override
		protected void begin() {
			super.begin();
			reset();
		}

		public void reset() {
			addedEntities.clear();
			removedEntities.clear();
			enabledEntities.clear();
			disabledEntities.clear();
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
		protected void disabled(Entity e) {
			super.disabled(e);
			disabledEntities.add(e);
		}

		@Override
		protected void enabled(Entity e) {
			super.enabled(e);
			enabledEntities.add(e);
		}

		@Override
		protected void process(Entity e) {

		}

	}

	public void updateSystems(World world) {
		Bag<EntitySystem> systems = world.getSystemManager().getSystems();
		for (int i = 0; i < systems.size(); i++) {
			EntitySystem entitySystem = systems.get(i);
			entitySystem.process();
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
		// updateSystems(world);

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
		// updateSystems(world);

		entity.delete();
		
		mockSystemA.reset();
		// entity.refresh();

		world.loopStart();
		// updateSystems(world);

		assertTrue(mockSystemA.removedEntities.contains(entity));
		assertFalse(mockSystemA.addedEntities.contains(entity));
	}

	@Test
	public void shouldCallEntityEnabledWhenAdded() {
		World world = new World();

		MockEntityProcessingSystem mockSystemA = new MockEntityProcessingSystem(ComponentA.class);

		world.getSystemManager().setSystem(mockSystemA);
		world.getSystemManager().initializeAll();

		Entity entity = world.createEntity();
		entity.addComponent(new ComponentA());
		entity.refresh();

		world.loopStart();

		assertTrue(mockSystemA.enabledEntities.contains(entity));
		assertFalse(mockSystemA.disabledEntities.contains(entity));
	}
	
	@Test
	public void shouldCallEntityDisabledWhenRemoved() {
		World world = new World();

		MockEntityProcessingSystem mockSystemA = new MockEntityProcessingSystem(ComponentA.class);

		world.getSystemManager().setSystem(mockSystemA);
		world.getSystemManager().initializeAll();

		Entity entity = world.createEntity();
		entity.addComponent(new ComponentA());
		entity.refresh();

		world.loopStart();
		
		entity.delete();
		
		mockSystemA.reset();

		world.loopStart();

		assertFalse(mockSystemA.enabledEntities.contains(entity));
		assertTrue(mockSystemA.disabledEntities.contains(entity));
	}
}
