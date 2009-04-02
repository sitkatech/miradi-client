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

import javax.swing.JPanel;

import org.miradi.dialogs.planning.PlanningViewObjectsOnlyDropDownPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ObjectsOnlyPlanningTreeTablePanel extends PlanningTreeTablePanel
{
	protected ObjectsOnlyPlanningTreeTablePanel(MainWindow mainWindowToUse,
												PlanningTreeTable treeToUse, 
												PlanningTreeTableModel modelToUse,
												Class[] buttonActions) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions);
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model, Class[] buttonActions) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);	
		
		return new ObjectsOnlyPlanningTreeTablePanel(mainWindowToUse, treeTable, model, buttonActions);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		customizationPanel.dispose();
	}
	
	protected void addCustomComponent(JPanel box)
	{
		try
		{
			customizationPanel = new PlanningViewObjectsOnlyDropDownPanel(getProject());
			box.add(customizationPanel);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private PlanningViewObjectsOnlyDropDownPanel customizationPanel; 
}
