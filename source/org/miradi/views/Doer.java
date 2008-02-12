/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views;

import java.awt.Point;
import java.util.EventObject;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.views.umbrella.ObjectPicker;
import org.miradi.views.umbrella.UmbrellaView;

abstract public class Doer
{
	abstract public boolean isAvailable();
	abstract public void doIt() throws CommandFailedException;
	
	public void doIt(EventObject event) throws CommandFailedException
	{
		doIt();
	}
	
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
