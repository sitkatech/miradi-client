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
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.ObjectsOnlyRowColumnProvider;
import org.miradi.dialogs.planning.PlanningViewObjectsOnlyDropDownPanel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.ViewData;

public class ObjectsOnlyPlanningTreeTablePanel extends PlanningTreeTablePanel
{
	protected ObjectsOnlyPlanningTreeTablePanel(MainWindow mainWindowToUse,
												PlanningTreeTable treeToUse, 
												PlanningTreeTableModel modelToUse, 
												RowColumnProvider rowColumnProvider, Class[] buttonActions
												) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProvider);
		
		customizationPanel = new PlanningViewObjectsOnlyDropDownPanel(getProject());
		addToButtonBox(new PanelTitleLabel(EAM.text("Show: ")));
		addToButtonBox(customizationPanel);
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);
		
		ObjectsOnlyRowColumnProvider rowColumnProvider = new ObjectsOnlyRowColumnProvider(mainWindowToUse.getProject());
		
		return new ObjectsOnlyPlanningTreeTablePanel(mainWindowToUse, treeTable, model, rowColumnProvider, getButtonActions());
	}
	
	@Override
	protected boolean doesCommandForceRebuild(CommandExecutedEvent event) throws Exception
	{
		if (super.doesCommandForceRebuild(event))
			return true;
			
		return isSingleLevelChoice(event);
	}
	
	public boolean isSingleLevelChoice(CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE);
	}
		
	@Override
	public void dispose()
	{
		super.dispose();
		
		customizationPanel.dispose();
	}
	
	private static Class[] getButtonActions()
	{
		return new Class[] {
			ActionDeletePlanningViewTreeNode.class,			
		};
	}
	
	private PlanningViewObjectsOnlyDropDownPanel customizationPanel; 
}
