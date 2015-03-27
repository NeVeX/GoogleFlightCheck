package com.mark.controller.view;

import javax.validation.Valid;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.mark.model.FlightInputSearch;

public interface IHomeViewController {
	
	public String getMainPage(ModelMap model);
	public String postDataToMainPage(FlightInputSearch flightInputSearch, BindingResult bindingResult, ModelMap model);

}
