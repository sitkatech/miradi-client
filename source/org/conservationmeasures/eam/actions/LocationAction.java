/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Point;

import javax.swing.Icon;

import org.conservationmeasures.eam.main.MainWindow;


public abstract class LocationAction extends MainWindowAction
{
	public LocationAction(MainWindow mainWindow, String label, String icon)
	{
		super(mainWindow, label, icon);
		createAt = new Point(0,0);
	}
	
	public LocationAction(MainWindow mainWindow, String label, Icon icon)
	{
		super(mainWindow, label, icon);
		createAt = new Point(0,0);
	}
	
	public void setInvocationPoint(Point location)
	{
		createAt = location;
	}


	Point createAt;
}
