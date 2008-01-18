/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.util.EventObject;

import javax.swing.JComponent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.utils.HtmlViewPanel;
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
			
		new HtmlViewPanel(getMainWindow(), data.title, defaultResouceClass, data.htmlFile).showAsOkDialog();
	}
	
	public void doIt() throws CommandFailedException
	{
		throw new CommandFailedException("Invalid call; use DoIt(event)");
	}
	
}
