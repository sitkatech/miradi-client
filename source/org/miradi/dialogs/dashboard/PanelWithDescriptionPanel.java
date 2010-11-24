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

import javax.swing.JScrollPane;

import org.miradi.dialogfields.QuestionEditorWithHierarchichalRows;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.main.MainWindow;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;

abstract public class PanelWithDescriptionPanel extends DisposablePanelWithDescription
{
	public PanelWithDescriptionPanel(MainWindow mainWindowToUse, OneFieldObjectDataInputPanel leftPanelToUse) throws Exception
	{
		super();
		
		mainWindow = mainWindowToUse;
		leftPanel = leftPanelToUse;

		add(createSplitPane(mainWindowToUse));
	}

	private PersistentHorizontalSplitPane createSplitPane(MainWindow mainWindowToUse) throws Exception
	{
		PersistentHorizontalSplitPane splitPane = new PersistentHorizontalSplitPane(mainWindowToUse, mainWindowToUse, getPanelDescription());
		RightSideDescriptionPanel rightPanel = createRightPanel(getDefaultDescriptionProvider());
		//FIXME urgent - need to come up with better way to communicate a row selection change to the right panel
		((QuestionEditorWithHierarchichalRows) leftPanel.getSingleField().getComponent()).addSelectionListener(rightPanel);
		
		splitPane.setLeftComponent(new JScrollPane(leftPanel));
		splitPane.setRightComponent(rightPanel);
		return splitPane;
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
		return new RightSideDescriptionPanel(getMainWindow(), mainDescriptionProvider, this);
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	abstract protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception;
	
	private MainWindow mainWindow;
	private OneFieldObjectDataInputPanel leftPanel;
}
