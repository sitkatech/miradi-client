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

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.resources.ResourcesHandler;

public class ViewSpecificHelpButtonData extends HelpButtonData
{
	public ViewSpecificHelpButtonData(MainWindow mainWindowToUse, String titleToUse, String htmlFileToUse)
	{
		super(null, titleToUse, htmlFileToUse);
		mainWindow = mainWindowToUse;
	}
	
	@Override
	public String getHelpContents() throws Exception
	{
		String prefix = "views/" + getMainWindow().getCurrentView().getClass().getSimpleName();
		String resourceFileName = prefix + "/" + htmlFile;
		String html = EAM.loadResourceFile(ResourcesHandler.class, resourceFileName);
		return html;
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
}
