package com.artemis;

import com.artemis.utils.Bag;

/**
 * The primary instance for the framework. It contains all the managers.
 * 
 * You must use this to create, delete and retrieve entities.
 * 
 * It is also important to set the delta each game loop iteration.
 * 
 * @author Arni Arent
 *
 */
public class World {
	private SystemManager systemManager;
	private EntityManager entityManager;
	private TagManager tagManager;
	private GroupManager groupManager;
	
	private int delta;
	private Bag<Entity> refreshed;
	private Bag<Entity> deleted;

	public World() {
		entityManager = new EntityManager(this);
		systemManager = new SystemManager(this);
		tagManager = new TagManager(this);
		groupManager = new GroupManager(this);
		
		refreshed = new Bag<Entity>();
		deleted = new Bag<Entity>();
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
	
	/**
	 * Time since last game loop.
	 * @return delta in milliseconds.
	 */
	public int getDelta() {
		return delta;
	}
	
	/**
	 * You must specify the delta for the game here.
	 * 
	 * @param delta time since last game loop.
	 */
	public void setDelta(int delta) {
		this.delta = delta;
	}

	/**
	 * Delete the provided entity from the world.
	 * @param e entity
	 */
	public void deleteEntity(Entity e) {
		e.disable();
		if(!deleted.contains(e)) {
			deleted.add(e);
		}
	}
	
	/**
	 * Ensure all systems are notified of changes to this entity.
	 * @param e entity
	 */
	public void refreshEntity(Entity e) {
		refreshed.add(e);
	}
	
	/**
	 * Create and return a new or reused entity instance.
	 * @return entity
	 */
	public Entity createEntity() {
		return entityManager.create();
	}
	
	/**
	 * Get a entity having the specified id.
	 * @param entityId
	 * @return entity
	 */
	public Entity getEntity(int entityId) {
		return entityManager.getEntity(entityId);
	}
	
	/**
	 * Let framework take care of internal business.
	 */
	public void loopStart() {
		if(!refreshed.isEmpty()) {
			for(int i = 0, s = refreshed.size(); s > i; i++) {
				Entity e = refreshed.get(i);
				entityManager.refresh(e);
				e.refreshPending = false;
			}
			refreshed.clear();
		} 
		
		if(!deleted.isEmpty()) {
			for(int i = 0, s = deleted.size(); s > i; i++) {
				Entity e = deleted.get(i);
				groupManager.remove(e);
				entityManager.remove(e);
			}
			deleted.clear();
		}
	}

}
