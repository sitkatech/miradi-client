/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.util.EventObject;

import javax.swing.JComponent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class HelpButtonDoer extends MainWindowDoer
{
	public HelpButtonDoer()
	{
	}
	
	public boolean isAvailable()
	{
		return true;
	}
	
	public void doIt(EventObject event) throws CommandFailedException
	{
		JComponent sourceComponent = (JComponent)event.getSource();
		HelpButtonData data = (HelpButtonData)sourceComponent.getClientProperty(HelpButtonData.class);
		
		Class defaultResouceClass = getMainWindow().getCurrentView().getClass();
		if (data.resourceClass != null)
			defaultResouceClass = data.resourceClass;
			
		new HtmlViewPanel(data.title, defaultResouceClass, data.htmlFile).showOkDialog();
	}
	
	public void doIt() throws CommandFailedException
	{
		throw new CommandFailedException("Invalid call; use DoIt(event)");
	}
	
}
