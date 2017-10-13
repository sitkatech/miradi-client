/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogs.dashboard;

import java.awt.Color;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.RowSelectionListener;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;
import org.miradi.views.umbrella.PersistentNonPercentageHorizontalSplitPane;

abstract public class SplitterPanelWithRightSideTextPanel extends DisposablePanelWithDescription
{
	public SplitterPanelWithRightSideTextPanel(MainWindow mainWindowToUse, DisposablePanel leftPanelToUse) throws Exception
	{
		super();
		
		mainWindow = mainWindowToUse;
		leftPanel = leftPanelToUse;

		add(createSplitPane(mainWindowToUse));
	}

	private PersistentHorizontalSplitPane createSplitPane(MainWindow mainWindowToUse) throws Exception
	{
		splitPane = createRawSplitPaneComponent(mainWindowToUse);
		rightPanel = createRightPanel(getDefaultDescriptionProvider());
		setupCommunicationBetweenLeftAndRightPanels(rightPanel);
		
		splitPane.setLeftComponent(new MiradiScrollPane(leftPanel));
		splitPane.setRightComponent(rightPanel);
		return splitPane;
	}

	private void setupCommunicationBetweenLeftAndRightPanels(RightSideDescriptionPanel rightPanel)
	{
		((RowSelectionListener) leftPanel).addRowSelectionListener(rightPanel);	
	}
	
	@Override
	public void dispose()
	{
		disposePanel(leftPanel);
		leftPanel = null;
		
		super.dispose();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		leftPanel.becomeActive();
		splitPane.restoreSavedLocation();
	}
	
	@Override
	public void becomeInactive()
	{
		splitPane.saveCurrentLocation();
		leftPanel.becomeInactive();
		
		super.becomeInactive();
	}
	
	private RightSideDescriptionPanel createRightPanel(AbstractLongDescriptionProvider mainDescriptionProvider) throws Exception
	{
		return new RightSideDescriptionPanel(getMainWindow(), mainDescriptionProvider, this, getRightPanelBackgroundColor());
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}

	abstract protected String getSplitterIdentifier();
	
	abstract protected Color getRightPanelBackgroundColor();
	
	abstract protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception;
	
	abstract protected PersistentNonPercentageHorizontalSplitPane createRawSplitPaneComponent(MainWindow mainWindowToUse);
	
	private MainWindow mainWindow;
	protected DisposablePanel leftPanel;
	protected RightSideDescriptionPanel rightPanel;
	private PersistentHorizontalSplitPane splitPane;
}
