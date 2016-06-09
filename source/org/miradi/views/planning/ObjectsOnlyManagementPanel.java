/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.planning;

import org.miradi.dialogs.planning.ObjectsOnlyRowColumnProvider;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNodeAlwaysExpanded;
import org.miradi.dialogs.planning.upperPanel.ObjectsOnlyPlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.ObjectsOnlyPlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class ObjectsOnlyManagementPanel extends PlanningTreeManagementPanel
{
	public ObjectsOnlyManagementPanel(MainWindow mainWindowToUse,
								PlanningTreeTablePanel planningTreeTablePanel,
								PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)
								throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Lists");
	}
	
	public static PlanningTreeManagementPanel createObjectsOnlyPanel(MainWindow mainWindowToUse) throws Exception
	{
		ObjectsOnlyRowColumnProvider provider = new ObjectsOnlyRowColumnProvider(mainWindowToUse.getProject());
		PlanningTreeRootNodeAlwaysExpanded rootNode = new PlanningTreeRootNodeAlwaysExpanded(mainWindowToUse.getProject());
		PlanningTreeTableModel objectsOnlyTreeTableModel = new ObjectsOnlyPlanningTreeTableModel(mainWindowToUse.getProject(), rootNode, provider);
		PlanningTreeTablePanel objectsOnlyPlanTreeTablePanel = ObjectsOnlyPlanningTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse, objectsOnlyTreeTableModel);
		PlanningTreeMultiPropertiesPanel objectsOnlyPlanPropertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);
		
		return new ObjectsOnlyManagementPanel(mainWindowToUse, objectsOnlyPlanTreeTablePanel, objectsOnlyPlanPropertiesPanel);
	}
}
