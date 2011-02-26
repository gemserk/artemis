package com.artemis;


public class World {
	private SystemManager systemManager;
	private EntityManager entityManager;
	private TagManager tagManager;
	private GroupManager groupManager;
	
	private int delta;

	public World() {
		entityManager = new EntityManager(this);
		systemManager = new SystemManager(this);
		tagManager = new TagManager(this);
		groupManager = new GroupManager(this);
	}
	
	public GroupManager getGroupManager() {
		return groupManager;
	}
	
	public SystemManager getSystemManager() {
		return systemManager;
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public TagManager getTagManager() {
		return tagManager;
	}
	
	public int getDelta() {
		return delta;
	}
	
	public void setDelta(int delta) {
		this.delta = delta;
	}

	public void deleteEntity(Entity e) {
		groupManager.remove(e);
		entityManager.remove(e);
	}
	
	public Entity createEntity() {
		return entityManager.create();
	}
	
	public Entity getEntity(int entityId) {
		return entityManager.getEntity(entityId);
	}

}
