package com.artemis;

import java.util.HashMap;
import java.util.Map;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

/**
 * If you need to group your entities together, e.g. tanks going into "units" group or explosions into "effects",
 * then use this manager. You must retrieve it using world instance.
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
	 * Add the entity to the group.
	 * 
	 * @param group group add to.
	 * @param e entity to put into the group.
	 */
	public void add(String group, Entity e) {
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

}
