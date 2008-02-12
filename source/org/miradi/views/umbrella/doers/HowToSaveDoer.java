/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.resources.ResourcesHandler;
import org.miradi.utils.HtmlViewPanel;
import org.miradi.views.MainWindowDoer;

public class HowToSaveDoer extends MainWindowDoer
{
	@Override
	public boolean isAvailable()
	{
		return true;
	}
		
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			String html = EAM.loadResourceFile(ResourcesHandler.class, "HowToSave.html");
			new HtmlViewPanel(getMainWindow(), EAM.text("Help"), html).showAsOkDialog();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}	
	}
}
