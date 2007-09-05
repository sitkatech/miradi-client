/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.ActivityIcon;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.MethodIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TaskIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewRowsLegendPanel extends AbstractPlanningViewLegendPanel
{
	public PlanningViewRowsLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public String getBorderTitle()
	{
		return EAM.text("Rows");
	}
	
	protected String getViewDataHiddenTypesTag()
	{
		return ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES;
	}
		
	protected CodeList getMasterListToCreateCheckBoxesFrom()
	{
		return PlanningView.getMasterRowList();
	}
	
	protected JPanel createLegendButtonPanel(Actions actions)
	{
		JPanel panel = new JPanel(new GridLayoutPlus(0, 3));
		
		addIconLineWithCheckBox(panel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(panel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(panel, Strategy.getObjectType(), Strategy.OBJECT_NAME, new StrategyIcon());
		addIconLineWithCheckBox(panel, Task.getObjectType(), Task.ACTIVITY_NAME, new ActivityIcon());
		addIconLineWithCheckBox(panel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		addIconLineWithCheckBox(panel, Task.getObjectType(), Task.METHOD_NAME, new MethodIcon());
		addIconLineWithCheckBox(panel, Task.getObjectType(), Task.OBJECT_NAME, new TaskIcon());
		
		return panel;
	}
	
	protected String getConfigurationTypeTag()
	{
		return PlanningViewConfiguration.TAG_ROW_CONFIGURATION;
	}
}
