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

public class HotButtonDoer extends MainWindowDoer
{
	public HotButtonDoer()
	{
	}
	
	public boolean isAvailable()
	{
		return true;
	}
	
	public void doIt(EventObject event) throws CommandFailedException
	{
		HelpButtonData data = (HelpButtonData)((JComponent)event.getSource()).getClientProperty(HelpButtonData.class);
		new HtmlViewPanel(data.title, getMainWindow().getCurrentView().getClass(), data.htmlFile).showOkDialog();
	}
	
	public void doIt() throws CommandFailedException
	{
		throw new CommandFailedException("Invalid call: ; use DoIt(event)");
	}
	
}
