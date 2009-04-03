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

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.PlanningViewObjectsOnlyDropDownPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.ColumnManager;

public class ObjectsOnlyPlanningTreeTablePanel extends PlanningTreeTablePanel
{
	protected ObjectsOnlyPlanningTreeTablePanel(MainWindow mainWindowToUse,
												PlanningTreeTable treeToUse, 
												PlanningTreeTableModel modelToUse
												) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, new Class[0]);
		
		customizationPanel = new PlanningViewObjectsOnlyDropDownPanel(getProject());
		addToButtonBox(new PanelTitleLabel(EAM.text("Show: ")));
		addToButtonBox(customizationPanel);
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);	
		
		return new ObjectsOnlyPlanningTreeTablePanel(mainWindowToUse, treeTable, model);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		customizationPanel.dispose();
	}
	
	protected CodeList getColumnsToShow() throws Exception
	{
		return new CodeList(ColumnManager.getVisibleColumnsForSingleType(getProject().getCurrentViewData()));
	}

	private PlanningViewObjectsOnlyDropDownPanel customizationPanel; 
}
