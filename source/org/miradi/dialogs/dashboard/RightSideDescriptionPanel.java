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
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.StyleSheet;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FastScrollPane;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.views.umbrella.ViewSwitchDoer;
import org.miradi.wizard.SkeletonWizardStep;

public class RightSideDescriptionPanel extends JPanel implements ListSelectionListener
{
	public RightSideDescriptionPanel(MainWindow mainWindowToUse, AbstractLongDescriptionProvider mainDescriptionProvider, DisposablePanel activeParentPanelToUse, Color backgroundColor) throws Exception
	{
		setLayout(new BorderLayout());
		
		activeParentPanel = activeParentPanelToUse;
		mainWindow = mainWindowToUse;
		bg = backgroundColor;
		
		viewer = new RightSideDescriptionHtmlViewer(mainWindow);
		add(new FastScrollPane(viewer));
		
		setRightSideHtmlContent(mainDescriptionProvider);
	}
	
	public void valueChanged(ListSelectionEvent rawEvent)
	{
		try
		{
			AbstractRowSelectionEvent castedEvent = (AbstractRowSelectionEvent) rawEvent;
			AbstractLongDescriptionProvider descriptionProvider = castedEvent.getDescriptionProvider();
			setRightSideHtmlContent(descriptionProvider);
			if (castedEvent.isViewChangeEvent())
				changeView(descriptionProvider);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}

	private void changeView(AbstractLongDescriptionProvider descriptionProvider) throws Exception
	{
		String wizardStepName = descriptionProvider.getWizardStepName();
		SkeletonWizardStep wizardStep = getMainWindow().getWizardManager().findStep(wizardStepName);
		if (wizardStep == null)
		{
			EAM.logVerbose("There is no wizard step for:" + wizardStepName);
			return;
		}
		
		if (descriptionProvider.hasWizardStepName())
		{
			activeParentPanel.becomeInactive();
			ViewSwitchDoer.changeView(getMainWindow(), wizardStepName);
			activeParentPanel.becomeActive();
		}
	}

	private void setRightSideHtmlContent(AbstractLongDescriptionProvider descriptionProvider) throws Exception
	{
		viewer.setText(descriptionProvider.getDescription());
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public class RightSideDescriptionHtmlViewer extends FlexibleWidthHtmlViewer
	{
		private RightSideDescriptionHtmlViewer(MainWindow mainWindow)
		{
			super(mainWindow);
			setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		}
		
		@Override
		public void customizeStyleSheet(StyleSheet style)
		{
			super.customizeStyleSheet(style);
			
			style.addRule("body {background-color: " + AppPreferences.convertToHexString(bg) + ";}");
		}
		
	}
	
	private MainWindow mainWindow;
	private RightSideDescriptionHtmlViewer viewer;
	private DisposablePanel activeParentPanel;
	private Color bg;
}
