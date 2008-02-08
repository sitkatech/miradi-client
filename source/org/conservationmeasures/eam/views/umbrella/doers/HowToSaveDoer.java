/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.resources.ResourcesHandler;
import org.conservationmeasures.eam.utils.HtmlViewPanel;
import org.conservationmeasures.eam.views.MainWindowDoer;

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
