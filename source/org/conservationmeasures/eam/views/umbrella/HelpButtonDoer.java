/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.util.EventObject;

import javax.swing.JComponent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.MainWindowDoer;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.BrowserLauncherRunner;

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
			
		if (!data.htmlFile.startsWith("X"))
			new HtmlViewPanel(data.title, defaultResouceClass, data.htmlFile).showOkDialog();
		else
		{
	        try 
	        {
	            BrowserLauncherRunner runner = new BrowserLauncherRunner(
	            		new BrowserLauncher(null),
	                    "",
	                    "file:" +defaultResouceClass.getResource(data.htmlFile).getPath(),
	                    null);
	            new Thread(runner).start();
	        }
	        catch (Exception e) 
	        {
	        	EAM.logException(e);
	        }
		}
	}
	
	public void doIt() throws CommandFailedException
	{
		throw new CommandFailedException("Invalid call; use DoIt(event)");
	}
	
}
