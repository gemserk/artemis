package com.artemis;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsSame;
import org.junit.Test;

public class EntityDeletedWhenDisabledBugTest {
	
	@Test
	public void shouldRefreshOnRemoveComponentUsingType() {
		World world = new World();
		Entity entity = world.createEntity();
		
		world.loopStart();
		
		entity.disable();
		world.loopStart();
		
		entity.delete();
		world.loopStart();
		
		Entity newEntity = world.createEntity();
		
		assertThat(newEntity, IsSame.sameInstance(entity));
		
		// as Artemis returns the same instance we should re enable it on the createEntity() method.
		assertThat(newEntity.isEnabled(), IsEqual.equalTo(true));
	}
	
}
