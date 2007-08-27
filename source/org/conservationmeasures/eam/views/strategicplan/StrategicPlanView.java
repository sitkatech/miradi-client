/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.dialogs.GoalPoolManagementPanel;
import org.conservationmeasures.eam.dialogs.ObjectivePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.StrategyPoolManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;

public class StrategicPlanView extends TabbedView
{
	public StrategicPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addStrategicPlanDoersToMap();
	}
	
	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.STRATEGIC_PLAN_VIEW_NAME;
	}

	public JToolBar createToolBar()
	{
		return new StrategicPlanToolBar(getActions());
	}

	public void createTabs() throws Exception
	{
		strategicPlanPanel = StrategicPlanPanel.createForProject(getMainWindow());
		objectivePanel =  new ObjectivePoolManagementPanel(getProject(), getMainWindow());
		goalPanel = new GoalPoolManagementPanel(getProject(), getMainWindow());
		
		strategyPoolManagementPanel = new StrategyPoolManagementPanel(getProject(), getMainWindow());
		
		addTab(EAM.text("Strategic Plan"), strategicPlanPanel);
		addNonScrollableTab(goalPanel);
		addNonScrollableTab(objectivePanel);
		addNonScrollableTab(strategyPoolManagementPanel);
	}
	
	public void deleteTabs()
	{
		strategyPoolManagementPanel.dispose();
		strategyPoolManagementPanel = null;
		strategicPlanPanel.dispose();
		strategicPlanPanel = null;
		objectivePanel.dispose();
		objectivePanel = null;
		goalPanel.dispose();
		goalPanel = null;
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		
		goalPanel.updateSplitterLocation();
		objectivePanel.updateSplitterLocation();
		strategyPoolManagementPanel.updateSplitterLocation();
	}

	public StrategicPlanPanel getStrategicPlanPanel()
	{
		return strategicPlanPanel;
	}
	
	public ObjectivePoolManagementPanel getObjectivePanel()
	{
		return objectivePanel;
	}
	
	public GoalPoolManagementPanel getGoalPanel()
	{
		return goalPanel;
	}
	
	private void addStrategicPlanDoersToMap()
	{
	}
	
	StrategicPlanPanel strategicPlanPanel;
	ObjectivePoolManagementPanel objectivePanel;
	GoalPoolManagementPanel goalPanel;
	StrategyPoolManagementPanel strategyPoolManagementPanel;
}

