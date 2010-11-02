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

import javax.swing.JScrollPane;

import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.MainWindow;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;

abstract public class AbstractDashboardTab extends DisposablePanelWithDescription
{
	public AbstractDashboardTab(MainWindow mainWindowToUse) throws Exception
	{
		mainWindow = mainWindowToUse;
		
		setLayout(new BorderLayout());
		splitPane = new PersistentHorizontalSplitPane(mainWindowToUse, mainWindowToUse, getPanelDescription());
		
		leftPanel = createLeftPanel();
		
		addLeftPanel(leftPanel);

		DashboardRightSideDescriptionPanel rightPanel = createRightPanel(leftPanel.getMainDescriptionFileName());
		leftPanel.addSelectionListener(rightPanel);
		addRightPanel(rightPanel);
		
		add(splitPane);
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		leftPanel.becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		leftPanel.becomeInactive();
		
		super.becomeInactive();
	}
	
	private void addRightPanel(DashboardRightSideDescriptionPanel rightPanel) throws Exception
	{
		splitPane.setRightComponent(rightPanel);
	}
	
	protected void addLeftPanel(TwoColumnPanel leftMainPanel)
	{
		splitPane.setLeftComponent(new JScrollPane(leftMainPanel));
	}
	
	private DashboardRightSideDescriptionPanel createRightPanel(String mainDescriptionFileName) throws Exception
	{
		return new DashboardRightSideDescriptionPanel(getMainWindow(), mainDescriptionFileName);
	}
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	abstract protected String getMainDescriptionFileName();

	abstract protected LeftSidePanelWithSelectableRows createLeftPanel();
	
	private MainWindow mainWindow;
	private PersistentHorizontalSplitPane splitPane;
	private LeftSidePanelWithSelectableRows leftPanel;
}
