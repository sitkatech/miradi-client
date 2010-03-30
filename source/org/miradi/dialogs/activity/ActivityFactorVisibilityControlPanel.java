/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.activity;

import org.miradi.actions.ActionHideActivityBubble;
import org.miradi.actions.ActionShowActivityBubble;
import org.miradi.dialogs.base.AbstractFactorBubbleVisibilityPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;

public class ActivityFactorVisibilityControlPanel extends AbstractFactorBubbleVisibilityPanel
{
	public ActivityFactorVisibilityControlPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow, ObjectType.TASK);
	}

	@Override
	protected Class getHideButtonClass()
	{
		return ActionHideActivityBubble.class;
	}

	@Override
	protected Class getShowButtonClass()
	{
		return ActionShowActivityBubble.class;
	}

	@Override
	protected String getExplanationMessage()
	{
		return EAM.text("Activity bubble visibility can only be changed in Results Chain Diagrams");
	}
	
	@Override
	protected boolean shouldShowButtonPanel()
	{
		return getMainWindow().getCurrentDiagramComponent().getDiagramObject().isResultsChain();
	}	
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Activity Visibility");
	}
}
