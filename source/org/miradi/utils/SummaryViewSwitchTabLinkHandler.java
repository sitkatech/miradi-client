/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.utils;

import javax.swing.JPopupMenu;

import org.martus.swing.HyperlinkHandler;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;

public class SummaryViewSwitchTabLinkHandler implements HyperlinkHandler
{
	public SummaryViewSwitchTabLinkHandler(MainWindow mainWindowToUse, String tabIdentifierToUse)
	{
		mainWindow = mainWindowToUse;
		tabIdentifier = tabIdentifierToUse;
	}
	
	public void linkClicked(String linkDescription)
	{
		try
		{
			final int index = getMainWindow().getSummaryView().getTabIndex(tabIdentifier);
			final String indexAsString = Integer.toString(index);
			final CommandSetObjectData changeTabCommand = new CommandSetObjectData(getProject().getCurrentViewData().getRef(), ViewData.TAG_CURRENT_TAB, indexAsString);
			getProject().executeCommand(changeTabCommand);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	public JPopupMenu getRightClickMenu(String url)
	{
		return null;
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void buttonPressed(String buttonName)
	{
	}
	
	private Project getProject()
	{
		return mainWindow.getProject();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
	private String tabIdentifier;
}
