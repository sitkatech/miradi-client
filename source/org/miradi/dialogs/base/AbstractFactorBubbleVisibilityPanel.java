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

package org.miradi.dialogs.base;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.ObjectsActionButton;

abstract public class AbstractFactorBubbleVisibilityPanel extends ObjectDataInputPanel
{
	public AbstractFactorBubbleVisibilityPanel(MainWindow mainWindow, int objectType)
	{
		super(mainWindow.getProject(), objectType);
		
		explanationLabel = new PanelTitleLabel(getExplanationMessage());
		bubbleButtonPanel = new TwoColumnPanel();
		
		ObjectsActionButton showBubble = createObjectsActionButton(mainWindow.getActions().getObjectsAction(getShowButtonClass()), getPicker());
		bubbleButtonPanel.add(showBubble);
		
		ObjectsActionButton hideBubble = createObjectsActionButton(mainWindow.getActions().getObjectsAction(getHideButtonClass()), getPicker());
		bubbleButtonPanel.add(hideBubble);
		add(explanationLabel);
		add(bubbleButtonPanel);
		
		updateFieldsFromProject();
	}
	
	@Override
	public void setObjectRefs(ORef[] orefsToUse)
	{
		super.setObjectRefs(orefsToUse);
		
		updateButtonsPanel();
	}
	
	private void updateButtonsPanel()
	{
		boolean shouldButtonsPanel = shouldShowButtonPanel();
		explanationLabel.setVisible(!shouldButtonsPanel);
		bubbleButtonPanel.setVisible(shouldButtonsPanel);
	}

	abstract protected boolean shouldShowButtonPanel();

	abstract protected Class getHideButtonClass();

	abstract protected Class getShowButtonClass();

	abstract protected String getExplanationMessage();
	
	private PanelTitleLabel explanationLabel;
	private TwoColumnPanel bubbleButtonPanel;
}
