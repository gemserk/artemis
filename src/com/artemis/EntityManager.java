package com.artemis;

import com.artemis.utils.Bag;

public class EntityManager {
	private World world;
	private Bag<Entity> activeEntities;
	private Bag<Entity> removedAndAvailable;
	private int nextAvailableId;
	private int count;
	private long totalCreated;
	private long totalRemoved;
	
	private Bag<Bag<Component>> componentsByType;

	public EntityManager(World world) {
		this.world = world;
		
		activeEntities = new Bag<Entity>();
		removedAndAvailable = new Bag<Entity>();
		
		componentsByType = new Bag<Bag<Component>>();
	}

	protected Entity create() {
		Entity e = removedAndAvailable.removeLast();
		if (e == null) {
			e = new Entity(world, nextAvailableId++);
		} else {
			e.reset();
		}
		activeEntities.set(e.getId(),e);
		count++;
		totalCreated++;
		return e;
	}

	protected void remove(Entity e) {
		activeEntities.set(e.getId(), null);
		
		e.setTypeBits(0);
		
		refresh(e);
		
		count--;
		totalRemoved++;

		removedAndAvailable.add(e);
	}
	
	public boolean isActive(int entityId) {
		return activeEntities.get(entityId) != null;
	}
	
	protected void addComponent(Entity e, Component component) {
		ComponentType type = ComponentTypeManager.getTypeFor(component.getClass());
		if(type.getId() >= componentsByType.size()) {
			for(int i = componentsByType.size(); type.getId() >= i; i++) {
				componentsByType.add(new Bag<Component>());
			}
		}
		
		Bag<Component> components = componentsByType.get(type.getId());
		components.set(e.getId(), component);

		e.addTypeBit(type.getBit());
	}
	
	protected void refresh(Entity e) {
		SystemManager systemManager = world.getSystemManager();
		Bag<EntitySystem> systems = systemManager.getSystems();
		for(int i = 0, s=systems.size(); s > i; i++) {
			systems.get(i).change(e);
		}
	}
	
	protected void removeComponent(Entity e, Component component) {
		ComponentType type = ComponentTypeManager.getTypeFor(component.getClass());
		Bag<Component> components = componentsByType.get(type.getId());
		components.set(e.getId(), null);
		e.removeTypeBit(type.getBit());
	}
	
	protected Component getComponent(Entity e, ComponentType type) {
		return componentsByType.get(type.getId()).get(e.getId());
	}
	
	protected Entity getEntity(int entityId) {
		return activeEntities.get(entityId);
	}
	
	public int getEntityCount() {
		return count;
	}
	
	public long getTotalCreated() {
		return totalCreated;
	}
	
	public long getTotalRemoved() {
		return totalRemoved;
	}

}
