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

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.miradi.main.MainWindow;

public class ExportablePlanningTreeTablePanel extends PlanningTreeTablePanel
{
	protected ExportablePlanningTreeTablePanel(MainWindow mainWindowToUse,
											   PlanningTreeTable treeToUse, 
											   PlanningTreeTableModel modelToUse,
											   Class[] buttonActions) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions);
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanelWithoutButtons(MainWindow mainWindowToUse) throws Exception
	{
		PlanningTreeTableModel model = new ConfigurablePlanningTreeTableModel(mainWindowToUse.getProject());
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);	
	
		return new PlanningTreeTablePanel(mainWindowToUse, treeTable, model, new Class[0]);
	}
	
	public static JComponent createPrintablePlanningTreeTablePanel(MainWindow mainWindow) throws Exception
	{
		PlanningTreeTablePanel wholePanel = createPlanningTreeTablePanelWithoutButtons(mainWindow);

		JPanel reformatted = new JPanel(new BorderLayout());
		
		reformatted.add(wholePanel.getTree(), BorderLayout.BEFORE_LINE_BEGINS);
		reformatted.add(wholePanel.getMainTable(), BorderLayout.CENTER);
		
		wholePanel.dispose();
		return reformatted;
	}
}
