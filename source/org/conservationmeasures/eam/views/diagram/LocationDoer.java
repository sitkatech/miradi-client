/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.views.ProjectDoer;

abstract public class LocationDoer extends ProjectDoer
{
	public LocationDoer(BaseProject project, Point locationToUse)
	{
		super(project);
		location = locationToUse;
	}
	
	public Point getLocation()
	{
		return location;
	}

	Point location;
}
