package com.artemis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EntityShouldBeDisabledWhenDeletedTest {
	
	@Test
	public void shouldBeDisabledIfDeleted() {
		World world = new World();
		Entity entity = world.createEntity();
		
		world.loopStart();
		
		assertTrue(entity.isEnabled());
		
		entity.delete();
		world.loopStart();
		
		assertFalse(entity.isAlive());
		assertFalse(entity.isEnabled());
	}
	
}
