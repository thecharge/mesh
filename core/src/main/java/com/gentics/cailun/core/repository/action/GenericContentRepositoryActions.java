package com.gentics.cailun.core.repository.action;

import java.util.List;

import com.gentics.cailun.core.data.model.generic.GenericContent;

public interface GenericContentRepositoryActions<T extends GenericContent> {
	List<T> findCustomerNodeBySomeStrangeCriteria(Object strangeCriteria);
}
