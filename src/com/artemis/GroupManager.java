package com.artemis;

import java.util.HashMap;
import java.util.Map;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

public class GroupManager {
	private World world;
	private Map<String, Bag<Entity>> entitiesByGroup;
	private Bag<String> groupByEntity;

	public GroupManager(World world) {
		this.world = world;
		entitiesByGroup = new HashMap<String, Bag<Entity>>();
		groupByEntity = new Bag<String>();
	}
	
	public void add(String group, Entity e) {
		Bag<Entity> entities = entitiesByGroup.get(group);
		if(entities == null) {
			entities = new Bag<Entity>();
			entitiesByGroup.put(group, entities);
		}
		entities.add(e);
		
		groupByEntity.set(e.getId(), group);
	}
	
	public ImmutableBag<Entity> getEntities(String group) {
		return entitiesByGroup.get(group);
	}
	
	public void remove(Entity e) {
		String group = groupByEntity.get(e.getId());
		groupByEntity.set(e.getId(), null);
		
		Bag<Entity> entities = entitiesByGroup.get(group);
		if(entities != null) {
			entities.remove(e);
		}
	}

}
