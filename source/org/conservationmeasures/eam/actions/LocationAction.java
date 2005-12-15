/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;

import javax.swing.Icon;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.Doer;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.Utilities;


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

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		if(((Component)event.getSource()).getName() == CENTER_LOCATION)
		{
			DiagramComponent diagramComponent = getMainWindow().getDiagramComponent();
			Rectangle rect = diagramComponent.getVisibleRect();
			int centeredWidth = rect.width / 2;
			int centeredHeight = rect.height / 2;
			Point centeredLocation = new Point(rect.x + centeredWidth, rect.y + centeredHeight);
			Point2D screenCenteredLocation2D = diagramComponent.fromScreen(centeredLocation);
			setInvocationPoint(Utilities.createPointFromPoint2D(screenCenteredLocation2D));
		}
		super.doAction(event);
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		Doer doer = view.getDoer(getClass());
		doer.setLocation(createAt);
		return doer.isAvailable();
	}

	Doer getDoer()
	{
		Doer doer = super.getDoer();
		doer.setLocation(createAt);
		return doer;
	}

	Point createAt;
	static public final String CENTER_LOCATION = "Center Location";
}
