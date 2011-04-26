package org.miradi.dialogs.dashboard;
import java.awt.Color;

import javax.swing.BorderFactory;

import org.miradi.dialogs.base.AbstractOpenStandardsQuestionPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Dashboard;

/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

abstract public class OpenStandardsDashboardTab extends SplitterPanelWithRightSideTextPanel
{
	public OpenStandardsDashboardTab(MainWindow mainWindowToUse, AbstractOpenStandardsQuestionPanel leftPanelToUse) throws Exception
	{
		super(mainWindowToUse, leftPanelToUse);
		
		rightPanel.setBackground(AbstractOpenStandardsQuestionPanel.DASHBOARD_BACKGROUND_COLOR);
		Color color = AppPreferences.getWizardTitleBackground();
		rightPanel.setBorder(BorderFactory.createLineBorder(color, BORDER_THICKNESS));
	}

	@Override
	protected Color getRightPanelBackgroundColor()
	{
		return AbstractOpenStandardsQuestionPanel.DASHBOARD_BACKGROUND_COLOR;
	}
	
	abstract public String getTabCode();
	
	@Override
	protected String getSplitterIdentifier()
	{
		return OpenStandardsDashboardTab.SAME_SPLITTER_IDENTIFIER_FOR_ALL_TABS;
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if(eventForcesRebuild(event))
				rebuild();
		}
		catch(Exception e)
		{
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	private void rebuild() throws Exception
	{
		((AbstractOpenStandardsQuestionPanel)leftPanel).rebuild();
	}

	private boolean eventForcesRebuild(CommandExecutedEvent event)
	{
		if(event.isSetDataCommandWithThisTypeAndTag(Dashboard.getObjectType(), Dashboard.TAG_PROGRESS_CHOICE_MAP))
			return true;
		if(event.isSetDataCommandWithThisTypeAndTag(Dashboard.getObjectType(), Dashboard.TAG_COMMENTS_MAP))
			return true;
		if(event.isSetDataCommandWithThisTypeAndTag(Dashboard.getObjectType(), Dashboard.TAG_FLAGS_MAP))
			return true;

		return false;
	}
	
	private static final String SAME_SPLITTER_IDENTIFIER_FOR_ALL_TABS = "PanelWithDescriptionPanel";
	private static final int BORDER_THICKNESS = 10;
}
