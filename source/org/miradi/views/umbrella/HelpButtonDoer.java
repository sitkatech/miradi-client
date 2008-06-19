/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.umbrella;

import java.util.EventObject;

import javax.swing.JComponent;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.resources.ResourcesHandler;
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
		try
		{
			JComponent sourceComponent = (JComponent)event.getSource();
			HelpButtonData data = (HelpButtonData)sourceComponent.getClientProperty(HelpButtonData.class);
			
			if (data.resourceClass == null)
			{
				String prefix = "views/" + getMainWindow().getCurrentView().getClass().getSimpleName();
				String resourceFileName = prefix + "/" + data.htmlFile;
				String html = EAM.loadResourceFile(ResourcesHandler.class, resourceFileName);
				new HtmlViewPanelWithMargins(getMainWindow(), data.title, html).showAsOkDialog();
			}
			else
			{
				new HtmlViewPanelWithMargins(getMainWindow(), data.title, data.resourceClass, data.htmlFile).showAsOkDialog();
			}
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	public void doIt() throws CommandFailedException
	{
		throw new CommandFailedException("Invalid call; use DoIt(event)");
	}
	
}
