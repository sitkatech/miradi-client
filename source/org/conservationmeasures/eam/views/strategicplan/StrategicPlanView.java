/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.dialogs.GoalPoolManagementPanel;
import org.conservationmeasures.eam.dialogs.ObjectivePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.StrategyPoolManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.strategicplan.wizard.StrategicPlanWizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class StrategicPlanView extends TabbedView
{
	public StrategicPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new StrategicPlanToolBar(mainWindowToUse.getActions()));
		
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

	public void createTabs() throws Exception
	{
		strategicPlanPanel = StrategicPlanPanel.createForProject(getMainWindow());
		objectivePanel =  new ObjectivePoolManagementPanel(getProject(), getMainWindow(), getActions());
		goalPanel = new GoalPoolManagementPanel(getProject(), getMainWindow(), getActions());
		
		strategyPoolManagementPanel = new StrategyPoolManagementPanel(getProject(), getMainWindow());
		
		addTab(EAM.text("Strategic Plan"), strategicPlanPanel);
		addScrollableTab(goalPanel);
		addScrollableTab(objectivePanel);
		addScrollableTab(strategyPoolManagementPanel);
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

	public WizardPanel createWizardPanel() throws Exception
	{
		stratPlanWizardPanel = new StrategicPlanWizardPanel(getMainWindow());
		return stratPlanWizardPanel;
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		stratPlanWizardPanel.jump(stepMarker);
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
	
	StrategicPlanWizardPanel stratPlanWizardPanel;
	StrategicPlanPanel strategicPlanPanel;
	ObjectivePoolManagementPanel objectivePanel;
	GoalPoolManagementPanel goalPanel;
	StrategyPoolManagementPanel strategyPoolManagementPanel;
}

