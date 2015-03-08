package com.mark.dal;

import com.mark.model.dal.ApplicationState;

public interface IApplicationDAL {

	public ApplicationState getApplicationState();

	public boolean saveApplicationState(ApplicationState state);
}
