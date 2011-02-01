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

import org.miradi.dialogs.planning.PlanningViewConfigurableControlPanel;
import org.miradi.main.MainWindow;
import org.miradi.objects.PlanningTreeConfiguration;

public class ConfigurablePlanningTreeTablePanel extends PlanningTreeTablePanel
{
	protected ConfigurablePlanningTreeTablePanel(MainWindow mainWindowToUse,
												 PlanningTreeTable treeToUse,
												 PlanningTreeTableModel modelToUse,
												 Class[] buttonActions,
												 PlanningTreeConfiguration rowColumnProvider) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProvider);

		customizationPanel = new PlanningViewConfigurableControlPanel(getProject());
		addComponentAsFirst(customizationPanel);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();

		customizationPanel.dispose();
	}

	private PlanningViewConfigurableControlPanel customizationPanel; 
}
