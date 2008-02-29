/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.util.EventObject;

import javax.swing.JComponent;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.views.MainWindowDoer;

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
			
		new HtmlViewPanelWithMargins(getMainWindow(), data.title, defaultResouceClass, data.htmlFile).showAsOkDialog();
	}
	
	public void doIt() throws CommandFailedException
	{
		throw new CommandFailedException("Invalid call; use DoIt(event)");
	}
	
}
