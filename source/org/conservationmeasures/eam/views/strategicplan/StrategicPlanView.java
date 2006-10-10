package org.conservationmeasures.eam.views.strategicplan;

import java.awt.Component;

import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteActivity;
import org.conservationmeasures.eam.actions.ActionDeleteGoal;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionModifyActivity;
import org.conservationmeasures.eam.actions.ActionModifyGoal;
import org.conservationmeasures.eam.actions.ActionModifyObjective;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.umbrella.CreateObjective;

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
		objectivePanel = new ObjectiveManagementPanel(this);
		resourcePanel = new ResourceManagementPanel(this);
		goalPanel = new GoalManagementPanel(this);

		addTab(EAM.text("Strategic Plan"), strategicPlanPanel);
		addTab(EAM.text("Objectives"), objectivePanel);
		addTab(EAM.text("Goals"), goalPanel);
		addTab(EAM.text("Resources"), resourcePanel);
	}
	
	public void deleteTabs()
	{
		if(strategicPlanPanel != null)
			strategicPlanPanel.close();
		strategicPlanPanel = null;
		objectivePanel = null;
		resourcePanel = null;
		goalPanel = null;
	}

	public StrategicPlanPanel getStrategicPlanPanel()
	{
		return strategicPlanPanel;
	}
	
	public ResourceManagementPanel getResourcePanel()
	{
		return resourcePanel;
	}
	
	public ObjectiveManagementPanel getObjectivePanel()
	{
		return objectivePanel;
	}
	
	public GoalManagementPanel getGoalPanel()
	{
		return goalPanel;
	}
	
	public void selectObject(EAMObject objectToSelect)
	{
		Component tab = getCurrentTabContents();
		if(tab.equals(getStrategicPlanPanel()))
		{
			getStrategicPlanPanel().selectObject(objectToSelect);
		}
		else
		{
			ObjectManagementPanel panel = (ObjectManagementPanel)tab;
			panel.selectObject(objectToSelect);
		}
	}
	
	public ModifyActivity getModifyActivityDoer()
	{
		return modifyActivityDoer;
	}
	
	public ModifyResource getModifyResourceDoer()
	{
		return modifyResourceDoer;
	}

	private void addStrategicPlanDoersToMap()
	{
		modifyActivityDoer = new ModifyActivity();
		modifyResourceDoer = new ModifyResource();
		
		addDoerToMap(ActionInsertActivity.class, new InsertActivity());
		addDoerToMap(ActionModifyActivity.class, modifyActivityDoer);
		addDoerToMap(ActionDeleteActivity.class, new DeleteActivity(this));
		
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionModifyResource.class, getModifyResourceDoer());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());
		
		addDoerToMap(ActionCreateObjective.class, new CreateObjective());
		addDoerToMap(ActionModifyObjective.class, new ModifyObjective());
		addDoerToMap(ActionDeleteObjective.class, new DeleteObjective());
		
		addDoerToMap(ActionCreateGoal.class, new CreateGoal());
		addDoerToMap(ActionModifyGoal.class, new ModifyGoal());
		addDoerToMap(ActionDeleteGoal.class, new DeleteGoal());

		addDoerToMap(ActionTreeNodeUp.class, new TreeNodeUp());
		addDoerToMap(ActionTreeNodeDown.class, new TreeNodeDown());
	}
	
	StrategicPlanPanel strategicPlanPanel;
	ResourceManagementPanel resourcePanel;
	ObjectiveManagementPanel objectivePanel;
	GoalManagementPanel goalPanel;
	
	ModifyActivity modifyActivityDoer;
	ModifyResource modifyResourceDoer;
}

