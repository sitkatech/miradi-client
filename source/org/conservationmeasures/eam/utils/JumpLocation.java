package org.conservationmeasures.eam.utils;

public class JumpLocation
{
	public JumpLocation(String viewToUse, Class stepMarkerToUse)
	{
		view = viewToUse;
		stepMarker = stepMarkerToUse;
	}
	
	public String getView()
	{
		return view;
	}
	
	public Class getStepMarker()
	{
		return stepMarker;
	}
	
	String view;
	Class stepMarker;
}
