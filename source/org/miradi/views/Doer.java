/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
