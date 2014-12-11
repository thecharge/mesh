package com.gentics.vertx.cailun.model;

import java.io.Serializable;

import org.springframework.data.neo4j.annotation.GraphId;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractPersistable implements Serializable {
	private static final long serialVersionUID = -3244769429406745303L;

	@GraphId
	private Long id;

	public Long getId() {
		return id;
	}

	/**
	 * Sets the id of the entity.
	 * 
	 * @param id
	 *            the id to set
	 */
	protected void setId(final Long id) {
		this.id = id;
	}

	@JsonIgnore
	public boolean isNew() {
		return null == getId();
	}

	@Override
	public int hashCode() {
		int hashCode = 17;

		hashCode += null == getId() ? 0 : getId().hashCode() * 31;

		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		AbstractPersistable that = (AbstractPersistable) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());
	}
}
