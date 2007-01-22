/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
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
