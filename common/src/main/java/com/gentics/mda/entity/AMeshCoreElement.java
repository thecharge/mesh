package com.gentics.mda.entity;

import com.gentics.mesh.context.InternalActionContext;
import com.gentics.mesh.core.TypeInfo;
import com.gentics.mesh.core.data.Role;
import com.gentics.mesh.core.rest.common.GenericRestResponse;
import com.gentics.mesh.core.rest.common.RestModel;
import com.gentics.mesh.core.rest.event.MeshElementEventModel;
import com.gentics.mesh.core.rest.event.role.PermissionChangedEventModelImpl;
import com.gentics.mesh.event.EventQueueBatch;
import com.gentics.mesh.parameter.value.FieldsSet;

public interface AMeshCoreElement<R extends RestModel> extends AMeshElement, ATransformableElement<R> {

	/**
	 * Update the vertex using the action context information.
	 *
	 * @param ac
	 * @param batch
	 *            Batch to which entries will be added in order to update the search index.
	 * @return true if the element was updated. Otherwise false
	 */
	boolean update(InternalActionContext ac, EventQueueBatch batch);

	/**
	 * Return the type info of the element.
	 *
	 * @return
	 */
	TypeInfo getTypeInfo();

	/**
	 * Add common fields to the given rest model object. The method will add common files like creator, editor, uuid, permissions, edited, created.
	 *
	 * @param model
	 * @param fields
	 *            Set of fields which should be included. All fields will be included if no selective fields have been specified.
	 * @param ac
	 */
	void fillCommonRestFields(InternalActionContext ac, FieldsSet fields, GenericRestResponse model);

	/**
	 * Method which is being invoked once the element has been created.
	 */
	MeshElementEventModel onCreated();

	/**
	 * Method which is being invoked once the element has been updated.
	 *
	 * @return Created event
	 */
	MeshElementEventModel onUpdated();

	/**
	 * Method which is being invoked once the element has been deleted.
	 *
	 * @return Created event
	 */
	MeshElementEventModel onDeleted();

	/**
	 * Method which is being invoked once the permissions on the element have been updated.
	 *
	 * @param role
	 * @return
	 */
	PermissionChangedEventModelImpl onPermissionChanged(Role role);

	/**
	 * Add the common permission information to the model.
	 *
	 * @param model
	 * @param role
	 */
	void fillPermissionChanged(PermissionChangedEventModelImpl model, Role role);
}
