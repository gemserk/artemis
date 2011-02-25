package com.artemis;

public class ComponentMapper<T extends Component> {
	private ComponentType type;
	private EntityManager em;
	private Class<T> classType;

	public ComponentMapper(Class<T> type, EntityManager em) {
		this.em = em;
		this.type = ComponentTypeManager.getTypeFor(type);
		this.classType = type;
	}

	public T get(Entity e) {
		return classType.cast(em.getComponent(e, type));
	}

}
