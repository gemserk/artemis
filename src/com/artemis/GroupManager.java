package com.artemis;

import java.util.HashMap;
import java.util.Map;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

/**
 * If you need to group your entities together, e.g. tanks going into "units" group or explosions into "effects",
 * then use this manager. You must retrieve it using world instance.
 * 
 * A entity can only belong to one group at a time.
 * 
 * @author Arni Arent
 *
 */
public class GroupManager {
	private World world;
	private Map<String, Bag<Entity>> entitiesByGroup;
	private Bag<String> groupByEntity;

	public GroupManager(World world) {
		this.world = world;
		entitiesByGroup = new HashMap<String, Bag<Entity>>();
		groupByEntity = new Bag<String>();
	}
	
	/**
	 * Set the group of the entity.
	 * 
	 * @param group group to set the entity into.
	 * @param e entity to set into the group.
	 */
	public void set(String group, Entity e) {
		remove(e); // Entity can only belong to one group.
		
		Bag<Entity> entities = entitiesByGroup.get(group);
		if(entities == null) {
			entities = new Bag<Entity>();
			entitiesByGroup.put(group, entities);
		}
		entities.add(e);
		
		groupByEntity.set(e.getId(), group);
	}
	
	/**
	 * Get all entities that belong to the provided group.
	 * @param group name of the group.
	 * @return read-only bag of entities belonging to the group.
	 */
	public ImmutableBag<Entity> getEntities(String group) {
		return entitiesByGroup.get(group);
	}
	
	/**
	 * Removes the provided entity from the group it is assigned to, if any.
	 * @param e the entity.
	 */
	public void remove(Entity e) {
		if(e.getId() < groupByEntity.getCapacity()) {
			String group = groupByEntity.get(e.getId());
			if(group != null) {
				groupByEntity.set(e.getId(), null);
				
				Bag<Entity> entities = entitiesByGroup.get(group);
				if(entities != null) {
					entities.remove(e);
				}
			}
		}
	}
	
	/**
	 * @param e entity
	 * @return the name of the group that this entity belongs to, null if none.
	 */
	public String getGroupOf(Entity e) {
		if(e.getId() < groupByEntity.getCapacity()) {
			return groupByEntity.get(e.getId());
		}
		return null;
	}
	
	/**
	 * Checks if the entity belongs to any group.
	 * @param e the entity to check.
	 * @return true if it is in any group, false if none.
	 */
	public boolean isGrouped(Entity e) {
		return getGroupOf(e) != null;
	}

}
