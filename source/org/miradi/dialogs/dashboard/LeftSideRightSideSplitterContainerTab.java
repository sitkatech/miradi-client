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

import org.miradi.dialogfields.QuestionEditorWithHierarchichalRows;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.main.MainWindow;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;

abstract public class LeftSideRightSideSplitterContainerTab extends DisposablePanelWithDescription
{
	public LeftSideRightSideSplitterContainerTab(MainWindow mainWindowToUse, OneFieldObjectDataInputPanel leftPanelToUse) throws Exception
	{
		setLayout(new BorderLayout());
		mainWindow = mainWindowToUse;
		leftPanel = leftPanelToUse;

		if (getCastedLeftComponent().getQuestion().hasLongDescriptionProvider())
		{
			createSplitPane(mainWindowToUse);
			add(splitPane);
		}
		else
		{
			add(leftPanel);
		}
	}

	private void createSplitPane(MainWindow mainWindowToUse) throws Exception
	{
		splitPane = new PersistentHorizontalSplitPane(mainWindowToUse, mainWindowToUse, getPanelDescription());
		RightSideDescriptionPanel rightPanel = createRightPanel(getMainDescriptionFileName());
		//FIXME urgent - need to come up with better way to communicate a row selection change to the right panel
		getCastedLeftComponent().addSelectionListener(rightPanel);
		
		splitPane.setLeftComponent(new JScrollPane(leftPanel));
		splitPane.setRightComponent(rightPanel);
	}

	private QuestionEditorWithHierarchichalRows getCastedLeftComponent()
	{
		return (QuestionEditorWithHierarchichalRows) leftPanel.getSingleField().getComponent();
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
	}
	
	@Override
	public void becomeInactive()
	{
		leftPanel.becomeInactive();
		
		super.becomeInactive();
	}
	
	private RightSideDescriptionPanel createRightPanel(AbstractLongDescriptionProvider mainDescriptionProvider) throws Exception
	{
		return new RightSideDescriptionPanel(getMainWindow(), mainDescriptionProvider);
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	abstract protected AbstractLongDescriptionProvider getMainDescriptionFileName() throws Exception;
	
	private MainWindow mainWindow;
	private PersistentHorizontalSplitPane splitPane;
	private OneFieldObjectDataInputPanel leftPanel;
}
