/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;

import javax.swing.Icon;

import org.conservationmeasures.eam.main.BaseProject;


public abstract class LocationAction extends ProjectAction
{
	public LocationAction(BaseProject projectToUse, String label, String icon)
	{
		super(projectToUse, label, icon);
		createAt = new Point(0,0);
	}
	
	public LocationAction(BaseProject projectToUse, String label, Icon icon)
	{
		super(projectToUse, label, icon);
		createAt = new Point(0,0);
	}
	
	public void setInvocationPoint(Point location)
	{
		createAt = location;
	}


	Point createAt;
}
