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

import java.awt.Color;

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.RowSelectionListener;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;

abstract public class SplitterPanelWithRightSideTextPanel extends DisposablePanelWithDescription
{
	public SplitterPanelWithRightSideTextPanel(MainWindow mainWindowToUse, AbstractObjectDataInputPanel leftPanelToUse) throws Exception
	{
		super();
		
		mainWindow = mainWindowToUse;
		leftPanel = leftPanelToUse;

		add(createSplitPane(mainWindowToUse));
	}

	private PersistentHorizontalSplitPane createSplitPane(MainWindow mainWindowToUse) throws Exception
	{
		splitPane = new DashboardTabSplitPane(this, mainWindowToUse, getSplitterIdentifier());
		rightPanel = createRightPanel(getDefaultDescriptionProvider());
		setupCommunicationBetweenLeftAndRightPanels(rightPanel);
		
		splitPane.setLeftComponent(new MiradiScrollPane(leftPanel));
		splitPane.setRightComponent(rightPanel);
		return splitPane;
	}

	protected abstract String getSplitterIdentifier();

	protected void setupCommunicationBetweenLeftAndRightPanels(RightSideDescriptionPanel rightPanel)
	{
		((RowSelectionListener) leftPanel).addRowSelectionListener(rightPanel);	
	}
	
	@Override
	public void dispose()
	{
		leftPanel.dispose();
		
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
	
	abstract protected Color getRightPanelBackgroundColor();

	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	abstract protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception;
	
	private MainWindow mainWindow;
	protected AbstractObjectDataInputPanel leftPanel;
	protected RightSideDescriptionPanel rightPanel;
	private PersistentHorizontalSplitPane splitPane;
}
