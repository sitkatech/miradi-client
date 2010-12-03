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

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.AbstractOpenStandardsQuestionPanel;
import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.main.MainWindow;
import org.miradi.utils.FastScrollPane;
import org.miradi.views.umbrella.PersistentHorizontalSplitPane;

abstract public class PanelWithDescriptionPanel extends DisposablePanelWithDescription
{
	public PanelWithDescriptionPanel(MainWindow mainWindowToUse, AbstractObjectDataInputPanel leftPanelToUse) throws Exception
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
		setupCommunicationBetweenLeftAndRightPanels(rightPanel);
		
		splitPane.setLeftComponent(new FastScrollPane(leftPanel));
		splitPane.setRightComponent(rightPanel);
		return splitPane;
	}

	private void setupCommunicationBetweenLeftAndRightPanels(RightSideDescriptionPanel rightPanel)
	{
		((AbstractOpenStandardsQuestionPanel) leftPanel).addRowSelectionListener(rightPanel);	
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
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	abstract protected AbstractLongDescriptionProvider getDefaultDescriptionProvider() throws Exception;
	
	private MainWindow mainWindow;
	protected AbstractObjectDataInputPanel leftPanel;
}
