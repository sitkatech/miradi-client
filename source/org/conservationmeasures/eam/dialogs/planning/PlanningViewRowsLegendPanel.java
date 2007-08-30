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
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewRowsLegendPanel extends PlanningViewLegendPanel
{
	public PlanningViewRowsLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	protected String getViewDataHiddenTypesTag()
	{
		return ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES;
	}
		
	
	
	protected void createLegendCheckBoxes()
	{
		CodeList masterList = PlanningView.getMasterRowList();
		for (int i = 0; i < masterList.size(); ++i)
		{
			createCheckBox(masterList.get(i));
		}
	}
	
	protected JPanel createLegendButtonPanel(Actions actions)
	{
		JPanel jPanel = new JPanel(new GridLayoutPlus(0,3));
		
		addTitleBar(jPanel, EAM.text("Rows"));
		addIconLineWithCheckBox(jPanel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jPanel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jPanel, Strategy.getObjectType(), Strategy.OBJECT_NAME, new StrategyIcon());
		addIconLineWithCheckBox(jPanel, Task.getObjectType(), Task.ACTIVITY_NAME, new ActivityIcon());
		addIconLineWithCheckBox(jPanel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		addIconLineWithCheckBox(jPanel, Task.getObjectType(), Task.METHOD_NAME, new MethodIcon());
		addIconLineWithCheckBox(jPanel, Task.getObjectType(), Task.OBJECT_NAME, new TaskIcon());
		
		return jPanel;
	}
}
