/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import java.awt.Point;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.strategicplan.ObjectPicker;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

abstract public class Doer
{
	abstract public boolean isAvailable();
	abstract public void doIt() throws CommandFailedException;
	
	public void setView(UmbrellaView view)
	{
		// overridden by any subclass that cares about projects
	}
	
	public void setMainWindow(MainWindow mainWindow)
	{
		// overridden by any subclass that cares about the main window
	}

	public void setProject(Project project)
	{
		// overridden by any subclass that cares about projects
	}
	
	public void setLocation(Point location)
	{
		// overridden by any subclass that knows about diagram locations
	}
	
	public void setPicker(ObjectPicker picker)
	{
		// overridden by any subclass that knows about selected objects
	}
}
