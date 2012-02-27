package com.artemis;

/**
 * Tag class for Manager. (may evolve into something more later)
 * 
 * @author Arni Arent
 *
 */
public interface Manager {

	/**
	 * Event that an entity has been removed from the world.
	 * @param e The Entity
	 */
	public void removeEntity(Entity e);

	/**
	 * Event that an entity has changed.
	 * @param e The Entity
	 */
	public void refresh(Entity e);

	/**
	 * Event that an entity has been created.
	 * @param e The new entity.
	 */
	public void addEntity(Entity e);
}
