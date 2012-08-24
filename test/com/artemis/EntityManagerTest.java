package com.artemis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.artemis.utils.ImmutableBag;

public class EntityManagerTest {
	
	static class MockEntitySystem extends EntitySystem {
		
		int changeCount = 0;
		
		@Override
		protected void change(Entity e) {
			changeCount++;
		}

		@Override
		protected void processEntities() {
			
		}

		@Override
		protected boolean checkProcessing() {
			return true;
		}
		
	}

	@Test
	public void shouldCallAddedWhenCreated() {
		World world = new World();
		
		MockEntitySystem mockSystemA = new MockEntitySystem();
		
		world.getSystemManager().setSystem(mockSystemA);
		world.getSystemManager().initializeAll();
		
		Entity entity = world.createEntity();
		entity.refresh();
		entity.refresh();
		entity.refresh();

		world.loopStart();
		
		assertEquals(1, mockSystemA.changeCount);
	}

}
