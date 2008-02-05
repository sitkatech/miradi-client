/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.HyperlinkHandler;

public class DefaultHyperlinkHandler implements HyperlinkHandler
{
	public DefaultHyperlinkHandler(MainWindow mainWindowToUse, Class resourceBaseClassToUse)
	{
		mainWindow = mainWindowToUse;
		resourceBaseClass = resourceBaseClassToUse;
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Class getResourceBaseClass()
	{
		return resourceBaseClass;
	}
	
	public void linkClicked(String linkDescription)
	{
		if (getMainWindow().mainLinkFunction(linkDescription))
			return;
	}

	public void buttonPressed(String buttonName)
	{
		try
		{
			String title = "Information";
			new HtmlViewPanel(getMainWindow(), title, getResourceBaseClass(), buttonName).showAsOkDialog();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Internal error loading contents for " + buttonName);
		}
	}

	public JPopupMenu getRightClickMenu(String url)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void valueChanged(String widget, String newValue)
	{
	}
	
	private MainWindow mainWindow;
	private Class resourceBaseClass;
}
