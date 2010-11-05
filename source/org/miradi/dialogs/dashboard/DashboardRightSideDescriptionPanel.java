/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.dashboard;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.StyleSheet;

import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.Translation;
import org.miradi.views.umbrella.ViewSwitchDoer;

public class DashboardRightSideDescriptionPanel extends JPanel implements ListSelectionListener
{
	public DashboardRightSideDescriptionPanel(MainWindow mainWindowToUse, String mainDescriptionFileName) throws Exception
	{
		mainWindow = mainWindowToUse;
		setLayout(new BorderLayout());
		viewer = new DashboardDescriptionHtmlViewer(mainWindow);
		add(new JScrollPane(viewer));
		
		setRightSideHtmlContent(mainDescriptionFileName);
	}
	
	public void valueChanged(ListSelectionEvent rawEvent)
	{
		try
		{
			RowSelectionEvent castedEvent = (RowSelectionEvent) rawEvent;
			String descriptionFileName = castedEvent.getDescriptionFileName();
			setRightSideHtmlContent(descriptionFileName);
			String wizardStepName = castedEvent.getWizardStepName();
			if (wizardStepName != null)
				ViewSwitchDoer.changeView(getMainWindow(), wizardStepName);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}

	protected void setRightSideHtmlContent(String descriptionFileName) throws Exception
	{
		String htmlText = Translation.getHtmlContent(descriptionFileName);
		viewer.setText(htmlText);
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public static class DashboardDescriptionHtmlViewer extends FlexibleWidthHtmlViewer
	{
		private DashboardDescriptionHtmlViewer(MainWindow mainWindow)
		{
			super(mainWindow);
		}
		
		@Override
		public void customizeStyleSheet(StyleSheet style)
		{
			super.customizeStyleSheet(style);
			
			style.addRule("body {background-color: " + AppPreferences.getWizardBackgroundColorForCss() + ";}");
		}	
	}
	
	private MainWindow mainWindow;
	private DashboardDescriptionHtmlViewer viewer;
}
