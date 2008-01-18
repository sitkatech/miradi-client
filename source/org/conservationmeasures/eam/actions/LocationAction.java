/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import java.awt.Point;
import java.util.EventObject;

import javax.swing.Icon;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.LocationHolder;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;


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
