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
package org.miradi.dialogs.planning.legend;

import javax.swing.JComponent;

import org.miradi.actions.Actions;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.ColumnManager;

public class PlanningViewColumsLegendPanel extends AbstractPlanningViewLegendPanel
{
	public PlanningViewColumsLegendPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, null);
		updateCheckBoxesFromProjectSettings();

	}
	
	public String getBorderTitle()
	{
		return EAM.text("Columns");
	}
	
	protected CodeList getMasterListToCreateCheckBoxesFrom()
	{
		return ColumnManager.getMasterColumnList();
	}
		
	protected JComponent createLegendButtonPanel(Actions actions)
	{
		TwoColumnPanel panel = new TwoColumnPanel();
		panel.disableFill();
		panel.setBackground(AppPreferences.getControlPanelBackgroundColor());

		CodeList masterList = ColumnManager.getMasterColumnList();
		for (int i = 0; i < masterList.size(); ++i)
		{
			addCheckBoxLine(panel, masterList.get(i));
		}
		
		return panel;
	}
	
	protected String getConfigurationTypeTag()
	{
		return PlanningViewConfiguration.TAG_COL_CONFIGURATION;
	}

	protected CodeList getVisibleTypes() throws Exception
	{
		return ColumnManager.getVisibleColumnCodes(getViewData());
	}
}
