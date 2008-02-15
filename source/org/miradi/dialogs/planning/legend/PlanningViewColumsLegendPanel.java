/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
