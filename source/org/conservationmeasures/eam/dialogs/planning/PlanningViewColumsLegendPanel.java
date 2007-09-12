/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.ColumnManager;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewColumsLegendPanel extends AbstractPlanningViewLegendPanel
{
	public PlanningViewColumsLegendPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		updateCheckBoxesFromProjectSettings();

	}
	
	public String getBorderTitle()
	{
		return EAM.text("Columns");
	}
	
	protected String getViewDataVisibleTypesTag()
	{
		return ViewData.TAG_PLANNING_VISIBLE_COL_TYPES;
	}
	
	protected CodeList getMasterListToCreateCheckBoxesFrom()
	{
		return ColumnManager.getMasterColumnList();
	}
		
	protected JComponent createLegendButtonPanel(Actions actions)
	{
		JPanel panel = new JPanel(new GridLayoutPlus(0, 3));
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

	void updateCheckBoxes(Command command)
	{
		super.updateCheckBoxes(command);
		if(!command.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		CommandSetObjectData cmd = (CommandSetObjectData)command;
		if(!cmd.getFieldTag().equals(getViewDataVisibleTypesTag()))
			return;
		
		try
		{
			updateCheckBoxesFromProjectSettings();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void updateCheckBoxesFromProjectSettings() throws Exception
	{
		CodeList visibleTypes = ColumnManager.getVisibleColumnCodes(getViewData());
		updateCheckboxes(visibleTypes);
	}
}
