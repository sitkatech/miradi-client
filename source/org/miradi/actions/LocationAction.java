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
package org.miradi.actions;

import java.awt.Point;
import java.util.EventObject;

import javax.swing.Icon;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.MainWindow;
import org.miradi.utils.LocationHolder;
import org.miradi.views.Doer;
import org.miradi.views.umbrella.UmbrellaView;


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

	public void doAction(EventObject event) throws CommandFailedException
	{
		LocationHolder invoker = (LocationHolder)event.getSource();
		if(!invoker.hasLocation())
			setInvocationPoint(null);	
		
		super.doAction(event);
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		Doer doer = view.getDoer(getClass());
		doer.setLocation(createAt);
		return doer.isAvailable();
	}

	public Doer getDoer()
	{
		Doer doer = super.getDoer();
		if(doer != null)
			doer.setLocation(createAt);
		return doer;
	}

	Point createAt;
}
