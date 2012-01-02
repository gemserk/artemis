package com.artemis;

import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

public class EntityTest {
	
	@Test
	public void shouldHaveRefreshPendingOnCreation() {
		World world = new World();
		Entity entity = world.createEntity();
		assertThat(entity.refreshPending, IsEqual.equalTo(true));
	}

	@Test
	public void shouldRefreshOnAddComponent() {
		World world = new World();
		Entity entity = world.createEntity();
		
		entity.refreshPending = false;
		
		entity.addComponent(new Component() {});
		
		assertThat(entity.refreshPending, IsEqual.equalTo(true));
	}
	
	@Test
	public void shouldRefreshOnRemoveComponent() {
		World world = new World();
		Entity entity = world.createEntity();
		
		entity.refreshPending = false;
		
		Component component = new Component() {};
		
		entity.addComponent(component);
		
		entity.refreshPending = false;
		
		entity.removeComponent(component);
		
		assertThat(entity.refreshPending, IsEqual.equalTo(true));
	}
	
	@Test
	public void shouldRefreshOnRemoveComponentUsingType() {
		World world = new World();
		Entity entity = world.createEntity();
		
		entity.refreshPending = false;
		
		Component component = new Component() {};
		
		
		entity.addComponent(component);
		
		entity.refreshPending = false;
		
		ComponentType componentType = ComponentTypeManager.getTypeFor(component.getClass());
		entity.removeComponent(componentType);
		
		assertThat(entity.refreshPending, IsEqual.equalTo(true));
	}
	
}
