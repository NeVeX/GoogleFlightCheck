package com.mark.dal;

import java.util.List;

import com.mark.model.ApplicationState;

public interface IAdminDAL {

	public ApplicationState getApplicationState();

	public boolean saveApplicationState(ApplicationState state);

	public List<ApplicationState> getAllApplicationStates();
}
