package com.mark.dal;

import java.util.List;

import com.mark.model.dal.ApplicationState;

public interface IApplicationDAL {

	public ApplicationState getApplicationState();

	public boolean saveApplicationState(ApplicationState state);

	public List<ApplicationState> getAllApplicationStates();
}
